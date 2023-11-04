package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.Payment;
import com.gustavohenning.dbecommercev1.entity.PaymentStatus;
import com.gustavohenning.dbecommercev1.entity.dto.StripeChargeDTO;
import com.gustavohenning.dbecommercev1.entity.dto.StripeTokenDTO;
import com.gustavohenning.dbecommercev1.entity.exception.CartNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.UserNotAuthorisedToMakePayment;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.repository.PaymentRepository;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
import com.gustavohenning.dbecommercev1.service.CartItemService;
import com.gustavohenning.dbecommercev1.service.EmailSenderService;
import com.gustavohenning.dbecommercev1.service.PaymentService;
import com.gustavohenning.dbecommercev1.util.CartOwner;
import com.gustavohenning.dbecommercev1.util.ExtractUserFromToken;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;
    private final CartOwner cartOwner;
    private final ExtractUserFromToken extractUserFromToken;
    private final EmailSenderService emailSenderService;

    @Value("${stripe.apikey}")
    String stripeApiKey;

    @PostConstruct
    public void init(){

        Stripe.apiKey = stripeApiKey;
    }



    @Autowired
    public PaymentServiceImpl(CartRepository cartRepository, PaymentRepository paymentRepository, UserRepository userRepository, CartItemService cartItemService, CartOwner cartOwner, ExtractUserFromToken extractUserFromToken, EmailSenderService emailSenderService) {
        this.cartRepository = cartRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.cartItemService = cartItemService;
        this.cartOwner = cartOwner;
        this.extractUserFromToken = extractUserFromToken;
        this.emailSenderService = emailSenderService;
    }

    @Transactional
    public Payment makePayment(Long cartId, @RequestHeader("Authorization") String token) throws StripeException {
        String userIdentifier = extractUserFromToken.extractUserIdentifier(token);


        if (cartOwner.isCartOwner(cartId, userIdentifier)) {
            // Create user to send data to stripe
            Optional<ApplicationUser> userOptional = userRepository.findByUsername(userIdentifier);
            ApplicationUser user = userOptional.orElse(null);

            if (user != null) {
                // Create user in stripe
                Stripe.apiKey = stripeApiKey;
                Map<String, Object> params = new HashMap<>();
                params.put("name", user.getName());
                params.put("email", user.getUsername());

                Map<String, Object> addressParams = new HashMap<>();
                addressParams.put("line1", user.getStreet());
                addressParams.put("line2", user.getNeighborhood());
                addressParams.put("city", user.getCity());
                addressParams.put("state", user.getState());
                addressParams.put("postal_code", user.getPostalCode());

                params.put("address", addressParams);

                Customer customer = Customer.create(params);


                Cart cart = cartRepository.findById(cartId)
                        .orElseThrow(() -> new CartNotFoundException(cartId));

                // Calculate Total of Items
                double totalItemsPrice = cart.getCartItems().stream()
                        .mapToDouble(cartItem -> cartItem.getItem().getSalePrice() * cartItem.getItemQuantity())
                        .sum();
                //Calculate Delivery Price
                double deliveryPrice = calculateDeliveryPrice(cartId);
                //Calculate Total of operation
                double totalPrice = totalItemsPrice + deliveryPrice;

                Payment payment = new Payment();
                payment.setCart(cart);
                payment.setStatus(PaymentStatus.PAID);
                payment.setStripeCustomerId(customer.getId());
                payment.setItemsPrice(totalItemsPrice);
                payment.setDeliveryPrice(deliveryPrice);
                payment.setTotalPrice(totalPrice);
                payment.setPaymentDate(LocalDateTime.now());


                cartItemService.removeCartItemsAndDeleteFromCartAfterPayment(cartId);
                paymentRepository.save(payment);

//                emailSenderService.sendEmail(userIdentifier, "Items Price: " + totalItemsPrice + " Delivery Price: " + deliveryPrice + " Total Price: " + totalPrice, "Payment Complete");
//
                return payment;
            }

            } else throw new UserNotAuthorisedToMakePayment(userIdentifier, cartId);
        return null;
    }


    public StripeTokenDTO createCardToken(StripeTokenDTO model) {
        try {
            Stripe.apiKey = stripeApiKey;


            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("payment_method", "pm_card_visa");

            Customer customer = Customer.create(customerParams);

            if (customer != null && customer.getId() != null) {
                model.setSuccess(true);
                model.setToken(customer.getId());
            }
            return model;
        } catch (StripeException e) {
            // Handle exceptions or log errors here
            throw new RuntimeException(e.getMessage());
        }
    }


    public StripeChargeDTO charge(StripeChargeDTO chargeRequest) {
        try {
            chargeRequest.setSuccess(false);

            Stripe.apiKey = stripeApiKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) chargeRequest.getAmount() * 100) //100 converts cents to 1 100cents = 1
                    .setCurrency("eur")
                    .setPaymentMethod(chargeRequest.getStripeToken())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Confirm the PaymentIntent to complete the payment
            PaymentIntent confirmedIntent = paymentIntent.confirm();

            if ("succeeded".equals(confirmedIntent.getStatus())) {
                chargeRequest.setChargeId(confirmedIntent.getId());
                chargeRequest.setSuccess(true);
            }

            return chargeRequest;
        } catch (StripeException e) {
            // Handle exceptions or log errors here
            throw new RuntimeException(e.getMessage());
        }
    }



    // TODO
    private double calculateDeliveryPrice(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
        Long userId = cart.getUserId();
        String postalCode = userRepository.findById(userId).get().getPostalCode();
        if (postalCode.length() > 5)

            return 10.0;
        else {
            return 15.0;
        }
    }
}
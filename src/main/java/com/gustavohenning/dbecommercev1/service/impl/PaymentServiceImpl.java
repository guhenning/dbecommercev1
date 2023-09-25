package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.Payment;
import com.gustavohenning.dbecommercev1.entity.PaymentStatus;
import com.gustavohenning.dbecommercev1.entity.exception.CartNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.UserNotFoundException;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.repository.PaymentRepository;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
import com.gustavohenning.dbecommercev1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentServiceImpl(CartRepository cartRepository, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public Payment makePayment(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        double totalItemsPrice = cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getItem().getSalePrice() * cartItem.getItemQuantity())
                .sum();
        double deliveryPrice = calculateDeliveryPrice(cartId);
        double totalPrice = totalItemsPrice + deliveryPrice;


        Payment payment = new Payment();
        payment.setCart(cart);
        payment.setStatus(PaymentStatus.PAID);
        payment.setItemsPrice(totalItemsPrice);
        payment.setDeliveryPrice(deliveryPrice);
        payment.setTotalPrice(totalPrice);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
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
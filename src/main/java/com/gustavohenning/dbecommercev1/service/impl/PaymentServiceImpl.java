package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.Payment;
import com.gustavohenning.dbecommercev1.entity.PaymentStatus;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.repository.PaymentRepository;
import com.gustavohenning.dbecommercev1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(CartRepository cartRepository, PaymentRepository paymentRepository) {
        this.cartRepository = cartRepository;
        this.paymentRepository = paymentRepository;
    }

    public Payment makePayment(Long cartId) {
        // Retrieve the cart from the database based on the cartId
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Calculate the total price from the cart
        double totalItemsPrice = cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getItem().getSalePrice() * cartItem.getItemQuantity())
                .sum();
        double deliveryPrice = calculateDeliveryPrice();
        double totalPrice = totalItemsPrice + deliveryPrice;

        // Create a new Payment entity
        Payment payment = new Payment();
        payment.setCart(cart);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setItemsPrice(totalItemsPrice);
        payment.setDeliveryPrice(deliveryPrice);
        payment.setTotalPrice(totalPrice);
        payment.setPaymentDate(LocalDateTime.now()); // Set the current date and time

        // Save the payment to the database
        return paymentRepository.save(payment);
    }

    // TODO: Implement your logic for calculating delivery price
    private double calculateDeliveryPrice() {
        return 10.0; // Example: Fixed delivery price of $10
    }
}
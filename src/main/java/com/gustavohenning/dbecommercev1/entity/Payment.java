package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.PaymentDTO;
import com.stripe.Stripe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private String stripeCustomerId;

    private double itemsPrice;
    private double deliveryPrice;
    private double totalPrice;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static Payment from(PaymentDTO paymentDto) {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.valueOf(paymentDto.getStatus()));
        payment.setStripeCustomerId(paymentDto.getStripeCustomerId());
        payment.setTotalPrice(paymentDto.getTotalPrice());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setCreatedDate(paymentDto.getCreatedDate());
        payment.setUpdatedDate(paymentDto.getUpdatedDate());

        return payment;
    }
}
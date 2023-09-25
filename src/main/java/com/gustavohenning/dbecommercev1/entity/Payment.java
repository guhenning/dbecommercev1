package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.CategoryDTO;
import com.gustavohenning.dbecommercev1.entity.dto.PaymentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private PaymentStatus status;

    @OneToOne
    private Cart cart;

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
        payment.setTotalPrice(paymentDto.getTotalPrice());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setCreatedDate(paymentDto.getCreatedDate());
        payment.setUpdatedDate(paymentDto.getUpdatedDate());
        return payment;

    }


}

package com.gustavohenning.dbecommercev1.entity;

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
    private Cart cartId;

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

}

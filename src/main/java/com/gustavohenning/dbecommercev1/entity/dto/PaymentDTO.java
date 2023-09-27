package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Payment;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long paymentId;
    private String status;
    private double totalPrice;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static PaymentDTO from(Payment payment) {
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setPaymentId(payment.getId());
        paymentDto.setStatus(payment.getStatus().toString());
        paymentDto.setTotalPrice(payment.getTotalPrice());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setCreatedDate(payment.getCreatedDate());
        paymentDto.setUpdatedDate(payment.getUpdatedDate());

        return paymentDto;
    }
}

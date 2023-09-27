package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Payment;
import com.gustavohenning.dbecommercev1.entity.dto.BrandDTO;
import com.gustavohenning.dbecommercev1.entity.dto.PaymentDTO;
import com.gustavohenning.dbecommercev1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "{cartId}")
    @Operation(summary = "Make Payment", description = "Make Payment Using cartId")
    public ResponseEntity<PaymentDTO> makePayment(@PathVariable Long cartId, @RequestHeader("Authorization") String token) {
            Payment payment = paymentService.makePayment(cartId, token);
            return new ResponseEntity<>(PaymentDTO.from(payment), HttpStatus.CREATED);
    }
}


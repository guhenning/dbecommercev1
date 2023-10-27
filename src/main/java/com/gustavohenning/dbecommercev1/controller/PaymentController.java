package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Payment;
import com.gustavohenning.dbecommercev1.entity.dto.BrandDTO;
import com.gustavohenning.dbecommercev1.entity.dto.PaymentDTO;
import com.gustavohenning.dbecommercev1.entity.dto.StripeChargeDTO;
import com.gustavohenning.dbecommercev1.entity.dto.StripeTokenDTO;
import com.gustavohenning.dbecommercev1.service.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${stripe.apikey}")
    String stripekey;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "{cartId}")
    @Operation(summary = "Make Payment", description = "Make Payment Using cartId")
    public ResponseEntity<PaymentDTO> makePayment(@PathVariable Long cartId, @RequestHeader("Authorization") String token) throws StripeException {
            Payment payment = paymentService.makePayment(cartId, token);
            return new ResponseEntity<>(PaymentDTO.from(payment), HttpStatus.CREATED);
    }

    @PostMapping("/card/token")
    @ResponseBody
    public StripeTokenDTO createCardToken(@RequestBody StripeTokenDTO model) {
        return paymentService.createCardToken(model);
    }

    @PostMapping("/charge")
    @ResponseBody
    public StripeChargeDTO charge(@RequestBody StripeChargeDTO model) {
        return paymentService.charge(model);
    }


}


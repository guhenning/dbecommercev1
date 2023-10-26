package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Payment;
import com.stripe.exception.StripeException;

public interface PaymentService {

     Payment makePayment(Long cartId, String token) throws StripeException;
}

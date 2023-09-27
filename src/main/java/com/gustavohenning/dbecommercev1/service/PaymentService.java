package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Payment;

public interface PaymentService {

     Payment makePayment(Long cartId, String token);
}

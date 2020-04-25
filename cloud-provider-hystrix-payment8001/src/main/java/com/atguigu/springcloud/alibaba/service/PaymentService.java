package com.atguigu.springcloud.alibaba.service;


public interface PaymentService {
    String paymentInfoOK(Integer id);

    String paymentInfoTimeout(Integer id);

    String paymentCircuitBreaker(Integer id);
}

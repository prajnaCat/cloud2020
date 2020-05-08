package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    public static HashMap<Long, Payment> maps = new HashMap<>(8);

    static {
        maps.put(1L, new Payment(1L, "11111"));
        maps.put(2L, new Payment(2L, "22222"));
        maps.put(3L, new Payment(3L, "33333"));
    }

    @GetMapping("/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL (@PathVariable(value = "id") Long id) {
        Payment payment = maps.get(id);
        CommonResult<Payment> result = new CommonResult<>(200, "from map port=" + serverPort, payment);
        return result;
    }
}

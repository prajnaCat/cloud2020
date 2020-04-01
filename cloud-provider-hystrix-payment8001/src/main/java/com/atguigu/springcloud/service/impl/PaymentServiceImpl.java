package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String paymentInfoOK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + " paymentInfo_OK, id: " + id + "\t" + "哈哈";
    }

    @Override
    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler", commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    public String paymentInfoTimeout(Integer id) {
        int timenumber = 5000;
        //int timenumber = 10/0;
        try { TimeUnit.MILLISECONDS.sleep(timenumber); } catch (InterruptedException e) { e.printStackTrace(); }
        return "线程池：" + Thread.currentThread().getName() +
                " paymentInfo_Timeout, id: " + id + "\t" +
                "泪奔,耗时(ms):" + timenumber;
    }

    public String paymentInfoTimeoutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() +
                " paymentInfo_TimeoutHandler, id: " + id + "\t" +
                "抱歉系统繁忙或者报错，请稍后再试！";
    }
}

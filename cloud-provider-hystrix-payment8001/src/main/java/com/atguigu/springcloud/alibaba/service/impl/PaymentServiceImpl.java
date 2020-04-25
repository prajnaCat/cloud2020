package com.atguigu.springcloud.alibaba.service.impl;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.alibaba.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String paymentInfoOK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + " paymentInfo_OK, id: " + id + "\t" + "哈哈";
    }

    //=====服务降级
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

    //=====服务熔断
    @Override
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            //请求次数
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            //时间窗口期
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            //失败率达到多少后跳闸
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;

    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id")Integer id) {
        return "id不能为负数，请请稍后再试，滴滴——   id:" + id;
    }


}

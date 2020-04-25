package com.atguigu.springcloud.alibaba.lb.impl;

import com.atguigu.springcloud.alibaba.lb.LoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLB implements LoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;

        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("******第几次访问，次数next:" + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        //负载均衡算法：rest接口请求次数 % 服务器集群的总数量 = 实际调用服务器位置下标，
        //每次服务重启后rest接口技术从1开始
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}

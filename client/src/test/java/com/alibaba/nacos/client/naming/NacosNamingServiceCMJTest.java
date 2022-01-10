package com.alibaba.nacos.client.naming;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import io.grpc.internal.GrpcUtil;
import org.junit.Test;

import java.util.List;

public class NacosNamingServiceCMJTest {


    @Test
    public void registerInstance() throws Exception {
        String serviceName = "serviceOrderStu";
        NacosNamingService nacosNamingService = new NacosNamingService("10.236.152.28:8847");
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8080);
        nacosNamingService.registerInstance("orderGroup", instance);
        List<Instance> allInstances = nacosNamingService.getAllInstances(serviceName);
        Thread.sleep(1000*1000);


    }

    @Test
    public void test(){
        GrpcUtil.authorityFromHostAndPort("nacos-cluste.r-21362-dev-2484",8080);
    }

}

/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.naming.healthcheck;

import com.alibaba.nacos.naming.core.Instance;
import com.alibaba.nacos.naming.misc.Loggers;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Health check status.
 *
 * @author nacos
 */
public class HealthCheckStatus implements Serializable {
    
    private static final long serialVersionUID = -5791320072773064978L;
    //是否正在检查
    public AtomicBoolean isBeingChecked = new AtomicBoolean(false);
    //检查失败的次数
    public AtomicInteger checkFailCount = new AtomicInteger(0);
    //检查成功的次数
    public AtomicInteger checkOkCount = new AtomicInteger(0);
    
    public long checkRt = -1L;
    //内存中存放
    private static ConcurrentMap<String, HealthCheckStatus> statusMap = new ConcurrentHashMap<>();
    
    public static void reset(Instance instance) {
        statusMap.put(buildKey(instance), new HealthCheckStatus());
    }
    
    /**
     * Get health check status of instance.
     *
     * @param instance instance
     * @return health check status
     */
    public static HealthCheckStatus get(Instance instance) {
        String key = buildKey(instance);
        
        if (!statusMap.containsKey(key)) {
            statusMap.putIfAbsent(key, new HealthCheckStatus());
        }
        
        return statusMap.get(key);
    }
    
    public static void remv(Instance instance) {
        statusMap.remove(buildKey(instance));
    }
    
    private static String buildKey(Instance instance) {
        try {
            
            String clusterName = instance.getClusterName();
            String serviceName = instance.getServiceName();
            String datumKey = instance.getDatumKey();//格式为: ip + [port] + unknown + clusterName
            return serviceName + ":" + clusterName + ":" + datumKey;
        } catch (Throwable e) {
            Loggers.SRV_LOG.error("[BUILD-KEY] Exception while set rt, ip {}, error: {}", instance.toJson(), e);
        }
        
        return instance.getDefaultKey();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbossas.test.cdi.interceptors;

import org.jbossas.test.cdi.annotations.MetricParam;
import org.jbossas.test.cdi.annotations.Metric;
import java.lang.reflect.Field;
import java.util.HashSet;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.jboss.as.test.cdi.CdiTestCase;
import org.jboss.as.test.cdi.instanceCash.ClassInstanceCacheSingleton;
import org.jboss.logging.Logger;

/**
 *
 * @author panos
 */
@Interceptor
@Metric
public class MetricInterceptor {
    private static final Logger logger =
            Logger.getLogger(MetricInterceptor.class);
    @AroundInvoke
     public Object logMethodEntry(InvocationContext ctx) throws Exception {
        Object result = null;
        result = ctx.proceed();

        HashSet<Object> cache = ClassInstanceCacheSingleton.getInstance().getInstanceCache();

        Field[] fields = ctx.getMethod().getDeclaringClass().getFields();
        Class<?> clazz = ctx.getMethod().getDeclaringClass();
        for(Field f : fields){
            MetricParam annos = f.getAnnotation(MetricParam.class);
            if (annos != null){
                try{
                    Field field = clazz.getField(f.getName());
                    logger.info("Field " + f.getName() + " : " + field.get(cache.iterator().next()));
                    CdiTestCase.fieldInterceptorLog = true;
                }catch (Exception e){
                    logger.info("Error during field retrieval... " + e.getMessage());
                }
            }
        }
        
        return result;
    }
}
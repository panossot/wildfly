/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.as.metrics.store;

import java.lang.reflect.Field;
import org.jboss.as.metrics.component.metricsCache.MetricObject;
import org.jboss.as.metrics.component.metricsCache.MetricsCacheSingleton;

/**
 *
 * @author panos
 */
public class Store {

    public static void CacheStore(Object target, Field field) throws IllegalArgumentException, IllegalAccessException {
        String name = field.getName() + "_" + target;
        MetricObject mo = MetricsCacheSingleton.getCache().searchMetricObject(name);
        if (mo != null) {
            mo.metric.add(field.get(target));
        } else {
            MetricObject newMo = new MetricObject();
            newMo.metric.add(field.get(target));
            newMo.name = name;
            MetricsCacheSingleton.getCache().addMetricCacheObject(newMo);
        }
    }

}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.test.integration.metrics;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.metrics.component.annotations.Metric;
import org.jboss.as.metrics.component.interceptors.MetricInterceptor;
import org.jboss.as.metrics.component.metricsCache.MetricObject;
import org.jboss.as.metrics.component.metricsCache.MetricsCacheSingleton;
import org.jboss.as.metrics.metricsMonitoringRhq.MonitoringRhq;
import org.jboss.as.metrics.store.Store;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="psotirop@redhat.com">Panagiotis Sotiropoulos</a>
 * @version $Revision: 1.1 $
 */
@RunWith(Arquillian.class)
public class MetricsTimeTestCase {
    
    @Deployment 
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
         .addClass(Foo.class).addClass(FooClass.class).addClass(MetricInterceptor.class)
         .addClass(Metric.class).addClass(MetricObject.class)
         .addClass(MetricsCacheSingleton.class).addClass(Store.class)
         .addClass(MonitoringRhq.class)
         .addAsManifestResource(new StringAsset("<beans><interceptors><class>"+ MetricInterceptor.class.getName() + "</class></interceptors></beans>"), "beans.xml");
      //   .addAsManifestResource(new StringAsset("<jboss-deployment-structure><deployment><dependencies><module name=\"org.jboss.dmr\"  export=\"true\"/><module name=\"org.jboss.as.controller\"  export=\"true\"/></dependencies></deployment></jboss-deployment-structure>"), "jboss-deployment-structure.xml");
    }

    @Inject FooClass fooClass;
   // @Inject FooClass fooClass2;
   // @Inject FooClass fooClass3;
     
    @Test
    public void testMetrics() throws Exception {
        System.out.println("Starting ... ");
        long timeInit, timeNow, firstRunTimeNeeded, averageTimeNeeded, averageTimeNeededExceptFirstRun;
        timeInit = System.nanoTime();
        fooClass.countClass();
        timeNow = System.nanoTime();
        firstRunTimeNeeded = timeNow - timeInit;
        
        
        for (int i=0; i<1000000; i++) {
            fooClass.countClass();
        }

        timeNow = System.nanoTime();
        
        averageTimeNeeded = (timeNow - timeInit)/1000001;
        averageTimeNeededExceptFirstRun = (timeNow - timeInit - firstRunTimeNeeded)/1000000;
     //   MetricsCacheSingleton.getCache().printMetricObjects();
        System.out.println("firstRunTimeNeeded : " + firstRunTimeNeeded);
        System.out.println("averageTimeNeeded : " + averageTimeNeeded);
        System.out.println("averageTimeNeededExceptFirstRun : " + averageTimeNeededExceptFirstRun);
        System.out.println("Done ...");
    }

}

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
import org.jboss.metrics.automatedmetricsapi.MetricsPropertiesApi;
import org.jboss.metrics.jbossautomatedmetricsproperties.MetricProperties;
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
    
    private String deploymentName = "myTestDeployment";
    
    @Deployment 
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
         .addClass(Foo.class).addClass(FooClass.class)
         .addAsManifestResource(new StringAsset("<jboss-deployment-structure><deployment><dependencies><module name=\"org.jboss.metrics.AutomatedMetrics\"  export=\"true\"/>"
                 + "<module name=\"org.jboss.metrics.AutomatedMetricsApi\"  export=\"true\"/><module name=\"org.jboss.metrics.JbossAutomatedMetricsLibray\"  export=\"true\"/>"
                 + "<module name=\"org.jboss.metrics.JBossAutomatedMetricsProperties\"  export=\"true\"/>"
                 + "</dependencies></deployment></jboss-deployment-structure>"), "jboss-deployment-structure.xml")
         .addAsManifestResource(new StringAsset("<beans><interceptors><class>org.jboss.metrics.automatedmetrics.MetricInterceptor</class></interceptors></beans>"), "beans.xml");
    }

    @Inject FooClass fooClass;
     
    @Test
    public void testMetrics() throws Exception {
        initializeMetricProperties();
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

        System.out.println("firstRunTimeNeeded : " + firstRunTimeNeeded);
        System.out.println("averageTimeNeeded : " + averageTimeNeeded);
        System.out.println("averageTimeNeededExceptFirstRun : " + averageTimeNeededExceptFirstRun);
        System.out.println("Done ...");
    }
    
    private void initializeMetricProperties() {
        MetricProperties metricProperties = new MetricProperties();
        metricProperties.setRhqMonitoring("true");
        metricProperties.setCacheStore("true");
        MetricsPropertiesApi.storeProperties(deploymentName, metricProperties);
    }

}

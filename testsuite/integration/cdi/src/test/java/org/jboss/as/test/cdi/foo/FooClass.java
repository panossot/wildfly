/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

package org.jboss.as.test.cdi.foo;

import org.jbossas.test.cdi.interceptors.MetricInterceptor;
import org.jbossas.test.cdi.annotations.MetricParam;
import javax.interceptor.Interceptors;
import org.jboss.as.test.cdi.instanceCash.ClassInstanceCacheSingleton;
import org.jboss.logging.Logger;

/**
 *
 * @author Panagiotis Sotiropoulos
 */
//@Interceptors(MetricInterceptor.class)
public class FooClass implements Foo {
    
    @MetricParam
    public int count = 0;
    
     private final Logger logger =
            Logger.getLogger(FooClass.class);
     
    public FooClass(){
        ClassInstanceCacheSingleton.getInstance().addInstanceCacheObject(this);
    }
    
    @Interceptors(MetricInterceptor.class)
    public int countClass(){
       count = 1;
       
       return count;
    }

    /**
     * @return the count
     */
    @Interceptors(MetricInterceptor.class)
    public int getCount() {
        return count;
    }
}

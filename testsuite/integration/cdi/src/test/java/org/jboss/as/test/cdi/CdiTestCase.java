/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.cdi;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.cdi.foo.*;
import org.jboss.as.test.cdi.instanceCash.*;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jbossas.test.cdi.annotations.*;
import org.jbossas.test.cdi.interceptors.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="psotirop@redhat.com">Panagiotis Sotiropoulos</a>
 * @version $Revision: 1.1 $
 */
@RunWith(Arquillian.class)
public class CdiTestCase {

    public static boolean fieldInterceptorLog= false;
    
    @Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
         .addClass(Foo.class).addClass(FooClass.class).addClass(MetricInterceptor.class)
         .addClass(ClassInstanceCacheSingleton.class).addClass(MetricParam.class)
         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject FooClass fooClass;
     
    @Test
    public void testCdi() throws Exception {
        fooClass.countClass();
        assertTrue("The interceptor logged the field update correctly... ",fieldInterceptorLog);
    }

}

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
package org.jboss.as.metrics.log;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.Param;

/**
 * @author panos
 */
@MessageBundle(projectCode = "Metrics")
public interface MetricsMessages {

    /**
     * The messages.
     */
    MetricsMessages MESSAGES = Messages.getBundle(MetricsMessages.class);

    /**
     * Creates an exception indicating the value for the element is invalid.
     *
     * @param value the invalid value.
     * @param element the element.
     * @param location the location of the error.
     *
     * @return {@link XMLStreamException} for the error.
     */
    @Message(id = 11072, value = "Invalid value: %s for '%s' element")
    XMLStreamException invalidValue(String value, String element, @Param Location location);

    @Message(id = 11073, value = "Error during metric interceptor run : %s , caused by %s")
    Exception metricInterceptorException(String message, String cause);
}

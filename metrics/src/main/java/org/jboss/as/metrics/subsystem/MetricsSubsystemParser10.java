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
package org.jboss.as.metrics.subsystem;

import java.util.EnumSet;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLExtendedStreamReader;

import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;
import static org.jboss.as.metrics.log.MetricsMessages.MESSAGES;

/**
 */
class MetricsSubsystemParser10 implements XMLStreamConstants, XMLElementReader<List<ModelNode>> {

    public static final MetricsSubsystemParser10 INSTANCE = new MetricsSubsystemParser10();

    private MetricsSubsystemParser10() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
        // METRICS subsystem doesn't have any attributes, so make sure that the xml doesn't have any
        requireNoAttributes(reader);

        final ModelNode metricsSubSystem = Util.createAddOperation(PathAddress.pathAddress(MetricsExtension.PATH_SUBSYSTEM));
        // add the subsystem to the ModelNode(s)
        list.add(metricsSubSystem);

        // elements
        final EnumSet<Element> encountered = EnumSet.noneOf(Element.class);
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case METRICS_1_0: {
                    final Element element = Element.forName(reader.getLocalName());
                    if (!encountered.add(element)) {
                        throw unexpectedElement(reader);
                    }
                    switch (element) {
                        case DATASOURCE: {
                            final String metricsDatasource = parseMetricsDatasourceElement(reader);
                            MetricsSubsystemRootResource.DATASOURCE.parseAndSetParameter(metricsDatasource, metricsSubSystem, reader);
                            break;
                        }
                        case LIST_OF_METRICS: {
                            final String listOfMetrics = parseListfMetricsElement(reader);
                            MetricsSubsystemRootResource.LIST_OF_METRICS.parseAndSetParameter(listOfMetrics, metricsSubSystem, reader);
                            break;
                        }
                        default: {
                            throw unexpectedElement(reader);
                        }
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }
    }

    static String parseMetricsDatasourceElement(XMLExtendedStreamReader reader) throws XMLStreamException {

        // we don't expect any attributes for this element.
        requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw MESSAGES.invalidValue(value, Element.DATASOURCE.getLocalName(), reader.getLocation());
        }
        return value.trim();
    }

    static String parseListfMetricsElement(XMLExtendedStreamReader reader) throws XMLStreamException {

        // we don't expect any attributes for this element.
        requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw MESSAGES.invalidValue(value, Element.LIST_OF_METRICS.getLocalName(), reader.getLocation());
        }
        return value.trim();
    }
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
import org.jboss.as.controller.AttributeDefinition;

import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.DefaultResourceAddDescriptionProvider;
import org.jboss.as.controller.descriptions.DefaultResourceRemoveDescriptionProvider;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import org.jboss.as.metrics.structure.Attachments;
import org.jboss.as.metrics.structure.DescriptorPropertyReplacementProcessor;

/**
 * {@link ResourceDefinition} for the Metrics subsystem's root management resource.
 */
public class MetricsSubsystemRootResource extends SimpleResourceDefinition {

    public static final SimpleAttributeDefinition LIST_OF_METRICS
            = new SimpleAttributeDefinitionBuilder(MetricsSubsystemModel.LIST_OF_METRICS, ModelType.BOOLEAN, true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(true))
            .build();

    public static final SimpleAttributeDefinition DATASOURCE
            = new SimpleAttributeDefinitionBuilder(MetricsSubsystemModel.DATASOURCE, ModelType.BOOLEAN, true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(true))
            .build();

    static final AttributeDefinition[] ATTRIBUTES = {LIST_OF_METRICS, DATASOURCE};

    public static final MetricsSubsystemRootResource INSTANCE = new MetricsSubsystemRootResource();

    private MetricsSubsystemRootResource() {
        super(MetricsExtension.PATH_SUBSYSTEM,
                MetricsExtension.getResourceDescriptionResolver(MetricsExtension.SUBSYSTEM_NAME));
    }

    private final DescriptorPropertyReplacementProcessor datasourceProcessor = new DescriptorPropertyReplacementProcessor(Attachments.METRICS_DATASOURCE);
    private final DescriptorPropertyReplacementProcessor listOfMetricsProcessor = new DescriptorPropertyReplacementProcessor(Attachments.LIST_OF_METRICS);

    @Override
    public void registerOperations(final ManagementResourceRegistration rootResourceRegistration) {

        final ResourceDescriptionResolver rootResolver = getResourceDescriptionResolver();

        // Ops to add and remove the root resource
        final MetricsSubsystemAdd subsystemAdd = new MetricsSubsystemAdd(datasourceProcessor, listOfMetricsProcessor);
        final DescriptionProvider subsystemAddDescription = new DefaultResourceAddDescriptionProvider(rootResourceRegistration, rootResolver);
        rootResourceRegistration.registerOperationHandler(ADD, subsystemAdd, subsystemAddDescription, EnumSet.of(OperationEntry.Flag.RESTART_ALL_SERVICES));
        final DescriptionProvider subsystemRemoveDescription = new DefaultResourceRemoveDescriptionProvider(rootResolver);
        rootResourceRegistration.registerOperationHandler(REMOVE, ReloadRequiredRemoveStepHandler.INSTANCE, subsystemRemoveDescription, EnumSet.of(OperationEntry.Flag.RESTART_ALL_SERVICES));

    }

}

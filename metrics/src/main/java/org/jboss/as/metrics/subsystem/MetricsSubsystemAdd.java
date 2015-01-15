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

import java.util.List;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

//import static org.jboss.as.metrics.log.MetricsLogger.ROOT_LOGGER;
import org.jboss.as.metrics.structure.DescriptorPropertyReplacementProcessor;

/**
 * Handler for adding the metrics subsystem.
 */
public class MetricsSubsystemAdd extends AbstractBoottimeAddStepHandler {

    private final DescriptorPropertyReplacementProcessor datasourceProcessor;
    private final DescriptorPropertyReplacementProcessor listOfMetricsProcessor;


    public MetricsSubsystemAdd(final DescriptorPropertyReplacementProcessor datasourceProcessor, final DescriptorPropertyReplacementProcessor listOfMetricsProcessor) {
        this.datasourceProcessor = datasourceProcessor;
        this.listOfMetricsProcessor = listOfMetricsProcessor;
    }

    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {

        for (AttributeDefinition ad : MetricsSubsystemRootResource.ATTRIBUTES) {
            ad.validateAndSet(operation, model);
        }
    }

    protected void performBoottime(final OperationContext context, final ModelNode operation, final ModelNode model,
                                   final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers) throws OperationFailedException {

        final boolean datasource = MetricsSubsystemRootResource.DATASOURCE.resolveModelAttribute(context, model).asBoolean();
        final boolean listOfMetrics = MetricsSubsystemRootResource.LIST_OF_METRICS.resolveModelAttribute(context, model).asBoolean();

        datasourceProcessor.setDescriptorPropertyReplacement(datasource);
        listOfMetricsProcessor.setDescriptorPropertyReplacement(listOfMetrics);

        context.addStep(new AbstractDeploymentChainStep() {
            protected void execute(DeploymentProcessorTarget processorTarget) {

        //        ROOT_LOGGER.debug("Activating Metrics subsystem");

                processorTarget.addDeploymentProcessor(MetricsExtension.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_DATASOURCE, datasourceProcessor);
                processorTarget.addDeploymentProcessor(MetricsExtension.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_LIST_OF_METRICS, listOfMetricsProcessor);
            }
        }, OperationContext.Stage.RUNTIME);

    }

    protected boolean requiresRuntimeVerification() {
        return false;
    }
}

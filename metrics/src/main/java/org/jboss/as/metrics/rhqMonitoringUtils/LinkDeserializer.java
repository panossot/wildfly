/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.as.metrics.rhqMonitoringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 *
 * @author panos
 */
public class LinkDeserializer extends JsonDeserializer<Link> {

    @Override
    public Link deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String rel = node.fieldNames().next();
        String href = node.elements().next().get("href").textValue();
        return new Link(rel, href);
    }
}

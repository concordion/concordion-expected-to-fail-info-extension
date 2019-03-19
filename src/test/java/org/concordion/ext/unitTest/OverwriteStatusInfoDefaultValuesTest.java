package org.concordion.ext.unitTest;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.ext.statusinfo.StatusInfo;
import org.concordion.ext.statusinfo.StatusInfoExtension;
import org.junit.Test;

import java.io.*;

public class OverwriteStatusInfoDefaultValuesTest {

    Document document;
    SpecificationProcessingEvent event;
    Resource resource;


    private void initialize() throws IOException, ParsingException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("org/concordion/ext/unitTest/SpecificationProcessingEvent.xml").getFile());

        BufferedReader br = new BufferedReader(new FileReader(file));
        this.resource = new Resource("/" + file.getPath());
        this.document = new Builder().build(br);

        Element rootElement = new Element(document.getRootElement());
        this.event = new SpecificationProcessingEvent(resource, rootElement);
    }

    @Test
    public void differentNotePrefixTest() throws IOException, ParsingException {
        initialize();

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix("This was set from a unit test")
                .build();
        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        String xml = event.getRootElement().toXML();
        assert xml.contains("This was set from a unit test");
        assert xml.contains("This was set from a unit testThis example has been marked as EXPECTED TO FAIL");

        // todo could try checking based on an element/document model...
    }


}
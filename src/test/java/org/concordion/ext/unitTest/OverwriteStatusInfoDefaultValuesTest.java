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
import java.util.Iterator;
import java.util.Objects;

public class OverwriteStatusInfoDefaultValuesTest {

    private SpecificationProcessingEvent event;

    private void initialize() throws IOException, ParsingException {
        ClassLoader classLoader = getClass().getClassLoader();
        String getResource = Objects.requireNonNull(
                classLoader.getResource("org/concordion/ext/unitTest/SpecificationProcessingEvent.xml"))
                .getFile();

        File file = new File(getResource);

        BufferedReader br = new BufferedReader(new FileReader(file));
        Resource resource = new Resource("/" + file.getPath());
        Document document = new Builder().build(br);

        Element rootElement = new Element(document.getRootElement());
        this.event = new SpecificationProcessingEvent(resource, rootElement);
    }

    @Test
    public void differentNotePrefixTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "This was set from a unit testThis example has been marked as EXPECTED TO FAIL";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix("This was set from a unit test")
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedTitlePrefixWasApplied = false;
        for (Element e :elements) {

            if(e.getFirstChildElement("h5") != null && e.getFirstChildElement("h5").hasChildren()) {

                if(e.getFirstChildElement("h5").getText().equals(expectedResult)){
                    expectedTitlePrefixWasApplied = true;
                }
            }
        }

        assert expectedTitlePrefixWasApplied;
    }

    @Test
    public void differentStyleTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "font-weight: normal; text-decoration: none; color: #FFFF00;";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .withStyle(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedStyleWasApplied = false;
        for (Element e :elements) {

            if(e.getFirstChildElement("h5") != null && e.getFirstChildElement("h5").hasChildren()) {

                if(e.getFirstChildElement("h5").getAttributeValue("style").equals(expectedResult)){
                    expectedStyleWasApplied = true;
                }
            }
        }

        assert expectedStyleWasApplied;
    }

    @Test
    public void differentReasonPrefixTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "New Reason: ";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setReasonPrefixMessage(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedReasonPrefixWasApplied = false;
        for (Element e :elements) {

            Element[] children = e.getChildElements("h5");
            for (Element c : children) {
                if (c.getText().contains(expectedResult)) {
                    expectedReasonPrefixWasApplied = true;
                }
            }
        }

        assert expectedReasonPrefixWasApplied;
    }

    @Test
    public void differentTitleTextPrefixTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "New Note: ";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedReasonPrefixWasApplied = false;
        for (Element e :elements) {

            Element[] children = e.getChildElements("h5");
            for (Element c : children) {
                if (c.getText().contains(expectedResult)) {
                    expectedReasonPrefixWasApplied = true;
                }
            }
        }

        assert expectedReasonPrefixWasApplied;
    }

    @Test
    public void differentMessageSizeTest() throws IOException, ParsingException {
        initialize();

        String expectedMessageSize = "h1";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix("New Note: ")
                .setReasonPrefixMessage("New Reason: ")
                .setMessageSize(expectedMessageSize)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedMessageSize);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedNoteSizeWasApplied = false;
        boolean expectedReasonSizeWasApplied = false;
        for (Element e :elements) {

            Element[] children = e.getChildElements(expectedMessageSize);
            for (Element c : children) {
                if (c.getText().contains("New Note: ")) {
                    expectedNoteSizeWasApplied = true;
                }
                if (c.getText().contains("New Reason: ")) {
                    expectedReasonSizeWasApplied = true;
                }
            }
        }

        assert expectedNoteSizeWasApplied;
        assert expectedReasonSizeWasApplied;
    }

    @Test
    public void differentExpectedToFailTitleTextPrefixTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "A Unit Test Set this Expected To Fail text";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setExpectedToFailTitleText(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedReasonPrefixWasApplied = false;
        for (Element e :elements) {

            Element[] children = e.getChildElements("h5");
            for (Element c : children) {
                if (c.getText().contains(expectedResult)) {
                    expectedReasonPrefixWasApplied = true;
                }
            }
        }

        assert expectedReasonPrefixWasApplied;
    }

    @Test
    public void differentIgnoreTitleTextPrefixTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "A Unit Test Set this Ignore text";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setIgnoredTitleText(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedReasonPrefixWasApplied = false;
        for (Element e :elements) {

            Element[] children = e.getChildElements("h5");
            for (Element c : children) {
                if (c.getText().contains(expectedResult)) {
                    expectedReasonPrefixWasApplied = true;
                }
            }
        }

        assert expectedReasonPrefixWasApplied;
    }

    @Test
    public void differentUnimplementedTitleTextPrefixTest() throws IOException, ParsingException {
        initialize();

        String expectedResult = "A Unit Test Set this Unimplemented text";

        StatusInfoExtension statusInfoExtension = new StatusInfoExtension();
        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setUnimplementedTitleText(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = event.getRootElement().getFirstChildElement("body").getChildElements("div");
        boolean expectedReasonPrefixWasApplied = false;
        for (Element e :elements) {

            Element[] children = e.getChildElements("h5");
            for (Element c : children) {
                if (c.getText().contains(expectedResult)) {
                    expectedReasonPrefixWasApplied = true;
                }
            }
        }

        assert expectedReasonPrefixWasApplied;
    }



}
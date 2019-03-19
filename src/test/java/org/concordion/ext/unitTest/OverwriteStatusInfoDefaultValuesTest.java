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
import java.util.Objects;

public class OverwriteStatusInfoDefaultValuesTest {

    private SpecificationProcessingEvent event;

    private StatusInfoExtension initialize() throws IOException, ParsingException {
        ClassLoader classLoader = getClass().getClassLoader();
        String getResource = Objects.requireNonNull(
                classLoader.getResource("org/concordion/ext/unitTest/SpecificationProcessingEventTestDocument.xml"))
                .getFile();

        File file = new File(getResource);

        BufferedReader br = new BufferedReader(new FileReader(file));
        Resource resource = new Resource("/" + file.getPath());
        Document document = new Builder().build(br);

        Element rootElement = new Element(document.getRootElement());
        this.event = new SpecificationProcessingEvent(resource, rootElement);

        return new StatusInfoExtension();
    }

    @Test
    public void notePrefixTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "This was set from a unit testThis example has been marked as EXPECTED TO FAIL";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix("This was set from a unit test")
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForTitlePrefix(expectedResult, elements, false);
    }

    private boolean doCheckForTitlePrefix(String expectedResult, Element[] elements, boolean expectedTitlePrefixWasApplied) {
        for (Element e :elements) {
            if(checkElementExists(e)) {
                expectedTitlePrefixWasApplied = checkElementHasText(expectedResult, expectedTitlePrefixWasApplied, e);
            }
        }
        return expectedTitlePrefixWasApplied;
    }

    @Test
    public void styleTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "font-weight: normal; text-decoration: none; color: #FFFF00;";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .withStyle(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForStyle(expectedResult, elements, false);
    }

    private boolean doCheckForStyle(String expectedResult, Element[] elements, boolean expectedStyleWasApplied) {
        for (Element e :elements) {
            if(checkElementExists(e)) {
                expectedStyleWasApplied = checkElementHasAttribute(expectedResult, expectedStyleWasApplied, e);
            }
        }
        return expectedStyleWasApplied;
    }

    @Test
    public void messageSizeTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedMessageSize = "h1";
        String note = "New Note: ";
        String reason = "New Reason: ";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix(note)
                .setReasonPrefixMessage(reason)
                .setMessageSize(expectedMessageSize)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedMessageSize);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");
        boolean expectedNoteSizeWasApplied = false;
        boolean expectedReasonSizeWasApplied = false;
        for (Element e :elements) {

            Element[] children = getElements(e, expectedMessageSize);
            for (Element c : children) {
                expectedNoteSizeWasApplied = checkAnExpectedValueWasApplied(c, note, expectedNoteSizeWasApplied);
                expectedReasonSizeWasApplied = checkAnExpectedValueWasApplied(c, reason, expectedReasonSizeWasApplied);
            }
        }

        assert expectedNoteSizeWasApplied;
        assert expectedReasonSizeWasApplied;
    }

    @Test
    public void reasonPrefixTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "New Reason: ";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setReasonPrefixMessage(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForExpectedValueInChildElement(expectedResult, elements);
    }

    @Test
    public void titleTextPrefixTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "New Note: ";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setTitleTextPrefix(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForExpectedValueInChildElement(expectedResult, elements);
    }

    @Test
    public void expectedToFailTitleTextPrefixTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "A Unit Test Set this Expected To Fail text";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setExpectedToFailTitleText(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForExpectedValueInChildElement(expectedResult, elements);
    }

    @Test
    public void ignoreTitleTextPrefixTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "A Unit Test Set this Ignore text";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setIgnoredTitleText(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForExpectedValueInChildElement(expectedResult, elements);
    }

    @Test
    public void unimplementedTitleTextPrefixTest() throws IOException, ParsingException {
        StatusInfoExtension statusInfoExtension = initialize();

        String expectedResult = "A Unit Test Set this Unimplemented text";

        StatusInfo statusInfo = new StatusInfo.StatusInfoBuilder()
                .setUnimplementedTitleText(expectedResult)
                .build();

        statusInfoExtension.setStatusInfo(statusInfo);

        statusInfoExtension.afterProcessingSpecification(event);

        assert event.getRootElement().toXML().contains(expectedResult);

        Element[] elements = getElements(getBody(event.getRootElement(), "body"), "div");

        assert doCheckForExpectedValueInElement(expectedResult, elements);
    }

    private boolean doCheckForExpectedValueInElement(String expectedResult, Element[] elements) {
        boolean result = false;

        for (Element e :elements) {
            result = searchElementsForChildren(expectedResult, result, e);
        }
        return result;
    }

    private boolean doCheckForExpectedValueInChildElement(String expectedResult, Element[] elements) {
        boolean result = false;

        for (Element e : elements) {

            Element[] children = getElements(e, "h5");
            for (Element c : children) {
                result = checkAnExpectedValueWasApplied(c, expectedResult, result);
            }
        }
        return result;
    }

    private boolean searchElementsForChildren(String expectedResult, boolean expectedReasonPrefixWasApplied, Element e) {
        Element[] children = getElements(e, "h5");
        for (Element c : children) {
            expectedReasonPrefixWasApplied = checkAnExpectedValueWasApplied(c, expectedResult, expectedReasonPrefixWasApplied);
        }
        return expectedReasonPrefixWasApplied;
    }

    private boolean checkElementHasAttribute(String expectedResult, boolean expectedStyleWasApplied, Element e) {
        if(getBody(e, "h5").getAttributeValue("style").equals(expectedResult)){
            expectedStyleWasApplied = true;
        }
        return expectedStyleWasApplied;
    }

    private boolean checkElementHasText(String expectedResult, boolean expectedTitlePrefixWasApplied, Element e) {
        if(getBody(e,"h5").getText().equals(expectedResult)){
            expectedTitlePrefixWasApplied = true;
        }
        return expectedTitlePrefixWasApplied;
    }

    private boolean checkAnExpectedValueWasApplied(Element e, String s, boolean alreadyFoundExpected) {
        if (alreadyFoundExpected){
            return true;
        }
        return e.getText().contains(s);
    }

    private boolean checkElementExists(Element e) {
        return getBody(e,"h5") != null && getBody(e,"h5").hasChildren();
    }

    private Element[] getElements(Element e, String div) {
        return e.getChildElements(div);
    }

    private Element getBody(Element e, String name) {
        return e.getFirstChildElement(name);
    }

}
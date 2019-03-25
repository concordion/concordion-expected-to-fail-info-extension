package org.concordion.ext.unitTest;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.ext.statusinfo.StatusInfo;
import org.concordion.ext.statusinfo.StatusInfoExtension;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class OverwriteStatusInfoDefaultValuesTest {

    private static final String PATH_TO_DOCUMENT = "org/concordion/ext/unitTest/SpecificationProcessingEventTestDocument.xml";
    private SpecificationProcessingEvent event;
    private StatusInfoExtension statusInfoExtension;


    @Before
    public void initialize() throws IOException, ParsingException {
        statusInfoExtension = new StatusInfoExtension();
        event = setSpecificationProcessingEvent(getResourceFile());
    }

    private SpecificationProcessingEvent setSpecificationProcessingEvent(File file) throws ParsingException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        Document document = new Builder().build(br);
        Element rootElement = new Element(document.getRootElement());
        Resource resource = new Resource("/" + file.getPath());
        return new SpecificationProcessingEvent(resource, rootElement);
    }

    private File getResourceFile() {
        return new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource(PATH_TO_DOCUMENT))
                .getFile());
    }

    @Test
    public void notePrefixTest() {
        String expectedResult = "This was set from a unit test This example has been marked as EXPECTED TO FAIL";
        StatusInfo statusInfo = new StatusInfo()
                .setTitleTextPrefix("This was set from a unit test");

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForTitlePrefix(expectedResult, elements);
    }

    private boolean doCheckForTitlePrefix(String expectedResult, Element[] elements) {
        boolean expectedTitlePrefixWasApplied = false;
        for (Element e :elements) {
            if(checkElementExists(e)) {
                expectedTitlePrefixWasApplied = checkElementHasText(expectedResult, expectedTitlePrefixWasApplied, e);
            }
        }
        return expectedTitlePrefixWasApplied;
    }

    @Test
    public void styleTest() {
        String expectedResult = "font-weight: normal; text-decoration: none; color: #FFFF00;";
        StatusInfo statusInfo = new StatusInfo()
                .withStyle(expectedResult);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForStyle(expectedResult, elements);
    }

    @Test
    public void messageSizeTest() {
        String expectedMessageSize = "h1";
        String note = "New Note: ";
        String reason = "New Reason: ";

        StatusInfo statusInfo = new StatusInfo()
                .setTitleTextPrefix(note)
                .setReasonPrefixMessage(reason)
                .setMessageSize(expectedMessageSize);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedMessageSize);
        assert doCheckForNote(expectedMessageSize, note, elements);
        assert doCheckForNote(expectedMessageSize, reason, elements);
    }

    @Test
    public void reasonPrefixTest() {
        String expectedResult = "New Reason: ";
        StatusInfo statusInfo = new StatusInfo()
                .setReasonPrefixMessage(expectedResult);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForNote("h5", expectedResult, elements);
    }

    @Test
    public void titleTextPrefixTest() {
        String expectedResult = "New Note: ";
        StatusInfo statusInfo = new StatusInfo()
                .setTitleTextPrefix(expectedResult);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForNote("h5", expectedResult, elements);
    }

    @Test
    public void expectedToFailTitleTextPrefixTest() {
        String expectedResult = "A Unit Test Set this Expected To Fail text";
        StatusInfo statusInfo = new StatusInfo()
                .setExpectedToFailTitleText(expectedResult);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForNote("h5", expectedResult, elements);
    }

    @Test
    public void ignoreTitleTextPrefixTest() {
        String expectedResult = "A Unit Test Set this Ignore text";
        StatusInfo statusInfo = new StatusInfo()
                .setIgnoredTitleText(expectedResult);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForNote("h5", expectedResult, elements);
    }

    @Test
    public void unimplementedTitleTextPrefixTest() {
        String expectedResult = "A Unit Test Set this Unimplemented text";
        StatusInfo statusInfo = new StatusInfo()
                .setUnimplementedTitleText(expectedResult);

        statusInfoExtension.setStatusInfo(statusInfo);
        statusInfoExtension.afterProcessingSpecification(event);

        Element[] elements = getElements();

        assert event.getRootElement().toXML().contains(expectedResult);
        assert doCheckForExpectedValueInElement(expectedResult, elements);
    }

    private boolean doCheckForStyle(String expectedResult, Element[] elements) {
        boolean expectedStyleWasApplied = false;
        for (Element e :elements) {
            if(checkElementExists(e)) {
                expectedStyleWasApplied = checkElementHasAttribute(expectedResult, expectedStyleWasApplied, e);
            }
        }
        return expectedStyleWasApplied;
    }

    private boolean doCheckForNote(String expectedMessageSize, String note, Element[] elements) {
        boolean expectedNoteSizeWasApplied = false;
        for (Element e : elements) {
            Element[] children = getChildElementsOf(e, expectedMessageSize);
            for (Element c : children) {
                expectedNoteSizeWasApplied = checkAnExpectedValueWasApplied(c, note, expectedNoteSizeWasApplied);
            }
        }
        return expectedNoteSizeWasApplied;
    }

    private boolean doCheckForExpectedValueInElement(String expectedResult, Element[] elements) {
        boolean result = false;
        for (Element e :elements) {
            result = searchElementsForChildren(expectedResult, result, e);
        }
        return result;
    }

    private boolean searchElementsForChildren(String expectedResult, boolean result, Element e) {
        Element[] children = getChildElementsOf(e, "h5");
        for (Element c : children) {
            result = checkAnExpectedValueWasApplied(c, expectedResult, result);
        }
        return result;
    }

    private boolean checkElementHasAttribute(String expectedResult, boolean result, Element e) {
        if(getBody(e, "h5").getAttributeValue("style").equals(expectedResult)){
            result = true;
        }
        return result;
    }

    private boolean checkElementHasText(String expectedResult, boolean result, Element e) {
        if(getBody(e,"h5").getText().equals(expectedResult)){
            result = true;
        }
        return result;
    }

    private boolean checkAnExpectedValueWasApplied(Element e, String s, boolean alreadyFoundExpected) {
        if (alreadyFoundExpected){
            return true;
        }
        return e.getText().contains(s);
    }

    private boolean checkElementExists(Element e) {
        return (getBody(e,"h5") != null) && (getBody(e,"h5").hasChildren());
    }

    private Element[] getElements() {
        Element body = getBody(event.getRootElement(), "body");
        return getChildElementsOf(body, "div");
    }

    private Element[] getChildElementsOf(Element e, String div) {
        return e.getChildElements(div);
    }

    private Element getBody(Element e, String name) {
        return e.getFirstChildElement(name);
    }

}
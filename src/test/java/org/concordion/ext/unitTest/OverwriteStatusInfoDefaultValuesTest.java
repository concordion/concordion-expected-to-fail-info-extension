package org.concordion.ext.unitTest;

import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.ext.statusinfo.StatusInfo;
import org.concordion.ext.statusinfo.StatusInfoExtension;
import org.junit.Before;
import org.junit.Test;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

public class OverwriteStatusInfoDefaultValuesTest {

    // Expected Values for Default StatusInfo
    private static final String DEFAULT_MESSAGE_SIZE_H3_ELEMENT = "h3";
    private static final String DEFAULT_STYLE = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private static final String DEFAULT_TITLE_TEXT_PREFIX = "Note:";
    private static final String DEFAULT_REASON_PREFIX = "Reason:";

    private static final String PATH_TO_TEMPLATE = "org/concordion/ext/unitTest/SpecificationProcessingEventTestDocument.xml";
    private static final String h1_ELEMENT = "h1";
    private static final String h5_ELEMENT = "h5";
    private static final String NEW_NOTE = "New Note: ";
    private static final String NEW_REASON = "New Reason: ";
    private static final String SPACE = " ";

    private SpecificationProcessingEvent event;
    private StatusInfoExtension statusInfo;

    @Before
    public void initialize() throws IOException, ParsingException {
        statusInfo = new StatusInfoExtension();
        File specificationTemplateFile = loadTemplateFile();
        event = setSpecificationProcessingEvent(specificationTemplateFile);
    }

    private File loadTemplateFile() {
        return new File(Objects.requireNonNull(getClass()
                .getClassLoader()
                .getResource(PATH_TO_TEMPLATE))
                .getFile());
    }

    private SpecificationProcessingEvent setSpecificationProcessingEvent(File file) throws ParsingException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        Document document = new Builder().build(br);
        Element rootElement = new Element(document.getRootElement());
        Resource resource = new Resource("/" + file.getPath());
        return new SpecificationProcessingEvent(resource, rootElement);
    }

    @Test
    public void defaultStatusInfoTest() {
        StatusInfo statusInfo = new StatusInfo();
        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), DEFAULT_STYLE);
        assert elementHasText(resultToXml(), DEFAULT_TITLE_TEXT_PREFIX + " This example has been marked as EXPECTED TO FAIL");
        assert elementHasText(resultToXml(), DEFAULT_TITLE_TEXT_PREFIX + " This example has been marked as IGNORED");
        assert elementHasText(resultToXml(), DEFAULT_TITLE_TEXT_PREFIX + " This example has been marked as UNIMPLEMENTED");
        assert elementHasText(resultToXml(), DEFAULT_REASON_PREFIX);
        assert resultWithElementContainsValue(result, DEFAULT_MESSAGE_SIZE_H3_ELEMENT, DEFAULT_STYLE, true);
        assert resultWithElementContainsValue(result, DEFAULT_MESSAGE_SIZE_H3_ELEMENT, DEFAULT_TITLE_TEXT_PREFIX, true);
        assert resultWithElementContainsValue(result, DEFAULT_MESSAGE_SIZE_H3_ELEMENT, DEFAULT_REASON_PREFIX, true);
    }

    @Test
    public void statusInfoTextIsEscaped() {
        String script = "<script type =\"text/JavaScript\">function Hello(){alert(\"Hello, World\");}</script>";
        String escapedScript = "&lt;script type =\"text/JavaScript\"&gt;function Hello(){alert(\"Hello, World\");}&lt;/script&gt;";
        StatusInfo statusInfo = new StatusInfo().withTitleTextPrefix(script);

        executeStatusInfoExtension(statusInfo);

        String xml = resultToXml();
        assert elementHasText(xml, escapedScript);
        assertFalse(xml.contains(script));
    }

    @Test
    public void notePrefixTest() {
        String titlePrefix = "This was set from a unit test";
        String title = titlePrefix + SPACE + "This example has been marked as EXPECTED TO FAIL";
        StatusInfo statusInfo = new StatusInfo().withTitleTextPrefix(titlePrefix);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), title);
        assert resultWithElementContainsValue(result, h5_ELEMENT, title, false);
    }

    @Test
    public void styleTest() {
        String style = "font-weight: normal; text-decoration: none; color: #FFFF00;";
        StatusInfo statusInfo = new StatusInfo().withStyle(style);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), style);
        assert resultWithElementContainsValue(result, DEFAULT_MESSAGE_SIZE_H3_ELEMENT, style, true);
    }

    @Test
    public void messageSizeTest() {
        StatusInfo statusInfo = new StatusInfo()
                .withTitleTextPrefix(NEW_NOTE)
                .withReasonPrefixMessage(NEW_REASON)
                .withMessageSize(h1_ELEMENT);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), h1_ELEMENT);
        assert resultWithElementContainsValue(result, h1_ELEMENT, NEW_NOTE, false);
        assert resultWithElementContainsValue(result, h1_ELEMENT, NEW_REASON, false);
    }

    @Test
    public void reasonPrefixTest() {
        StatusInfo statusInfo = new StatusInfo().withReasonPrefixMessage(NEW_REASON);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), NEW_REASON);
        assert resultWithElementContainsValue(result, h5_ELEMENT, NEW_REASON, false);
    }

    @Test
    public void titleTextPrefixTest() {
        StatusInfo statusInfo = new StatusInfo().withTitleTextPrefix(NEW_NOTE);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), NEW_NOTE);
        assert resultWithElementContainsValue(result, h5_ELEMENT, NEW_NOTE, false);
    }

    @Test
    public void expectedToFailTitleTextPrefixTest() {
        String titleText = "A Unit Test Set this Expected To Fail text";
        StatusInfo statusInfo = new StatusInfo().withExpectedToFailTitleText(titleText);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), titleText);
        assert resultWithElementContainsValue(result, h5_ELEMENT, titleText, false);
    }

    @Test
    public void ignoreTitleTextPrefixTest() {
        String titleText = "A Unit Test Set this Ignore text";
        StatusInfo statusInfo = new StatusInfo().withIgnoredTitleText(titleText);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), titleText);
        assert resultWithElementContainsValue(result, h5_ELEMENT, titleText, false);
    }

    @Test
    public void unimplementedTitleTextPrefixTest() {
        String titleText = "A Unit Test Set this Unimplemented text";
        StatusInfo statusInfo = new StatusInfo().withUnimplementedTitleText(titleText);

        executeStatusInfoExtension(statusInfo);

        Element[] result = getStatusInfoExtensionProcessingResult();

        assert elementHasText(resultToXml(), titleText);
        assert resultWithElementContainsValue(result, h5_ELEMENT, titleText, false);
    }

    private void executeStatusInfoExtension(StatusInfo statusInfo) {
        this.statusInfo.setStatusInfo(statusInfo);
        this.statusInfo.afterProcessingSpecification(event);
    }

    private String resultToXml() {
        return event.getRootElement().toXML();
    }

    private boolean resultWithElementContainsValue(Element[] elements, String name, String value, boolean isAttribute) {
        for (Element element : elements) {
            if (elementWithNameExistsWithChildren(element, name)) {
                if (elementAtHasValue(element, name, value, isAttribute)) {
                    return true;
                }

                resultWithElementContainsValue(element.getChildElements(name), name, value, isAttribute);
            }
            if (element.getText().contains(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean elementAtHasValue(Element element, String name, String value, boolean isAttribute) {
        if (isAttribute) {
            return elementHasAttribute(element, name, value);
        } else {
            return elementHasText(element, name, value);
        }
    }

    private boolean elementHasText(String text, String value) {
        return text.contains(value);
    }

    private boolean elementHasAttribute(Element element, String name, String expectedValue) {
        String elementAttribute = element.getFirstChildElement(name).getAttributeValue("style");
        return elementAttribute.equals(expectedValue);
    }

    private boolean elementHasText(Element element, String name, String expectedValue) {
        String elementValue = element.getFirstChildElement(name).getText();
        return elementValue.equals(expectedValue);
    }

    private boolean elementWithNameExistsWithChildren(Element element, String name) {
        return (element.getFirstChildElement(name) != null) && (element.getFirstChildElement(name).hasChildren());
    }

    private Element[] getStatusInfoExtensionProcessingResult() {
        Element element = event.getRootElement().getFirstChildElement("body");
        return element.getChildElements("div");
    }

}
package org.concordion.ext.statusinfo;

import org.concordion.api.Element;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Displays a message containing a Note and a Reason in the corresponding output specification
 * when an example is annotated with a status modifier with additional reason text.
 *
 * <p></p>
 * Sample usage:
 * <p></p>
 * <pre>
 * In a Fixture class annotate the class with:
 * \@Extensions({ StatusInfoExtension.class })
 *
 * To a specification add:
 * ## [My Specification Name](- "Reason my specification is failing c:status=expectedToFail")
 * Or
 * ## [My Specification Name](- "Reason my specification is ignored c:status=ignored")
 * Or
 * ## [My Specification Name](- "Reason my specification is unimplemented c:status=unimplemented")
 * </pre>
 * <p></p>
 * In the completed specification:
 * <ul>
 * <li>Note: resolves to 'This example has been marked as EXPECTED_TO_FAIL'</li>
 * <li>Reason: Based on the sample usage above, would resolve to 'Reason my specification is failing'</li>
 * </ul>
 * <p></p>
 * This can be applied to the <code>expectedToFail</code>, <code>ignored</code> and <code>unimplemented</code>
 * status modifiers.
 * <p></p>
 * The text for each part of the message can be overridden, as well as the message size and style.
 * <p></p>
 * Please see the demo of this project for more examples.
 *
 * @author Luke Pearson
 *
 */
public class StatusInfoExtension implements SpecificationProcessingListener, ConcordionExtension {

    private static final String NAMESPACE_URI = "http://www.concordion.org/2007/concordion";
    private String style = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private String messageSize = "h5";

    private String expectedToFailTitleText = "This example has been marked as EXPECTED TO FAIL";
    private String ignoredTitleText = "This example has been marked as IGNORED";
    private String unimplementedTitleText = "This example has been marked as UNIMPLEMENTED";
    private String titleTextPrefix = "";
    private String reasonPrefix = "Reason: ";

    private static final List<String> ALLOWED_MESSAGE_SIZES = new ArrayList<>(Arrays.asList("h1","h2","h3","h4","h5","h6"));


    public StatusInfoExtension setStyle(String style) {
        this.style = style;
        return this;
    }

    public StatusInfoExtension setHeaderElementSize(String value) {
        this.messageSize = ALLOWED_MESSAGE_SIZES.contains(value.toLowerCase())?value:messageSize;
        return this;
    }

    public StatusInfoExtension setNoteMessage(String value) {
        this.titleTextPrefix = value;
        return this;
    }

    public StatusInfoExtension setReasonPrefixMessage(String value) {
        this.reasonPrefix = value;
        return this;
    }

    public StatusInfoExtension setExpectedToFailTest(String value) {
        this.expectedToFailTitleText = value;
        return this;
    }

    public StatusInfoExtension setIgnoredTitleText(String value) {
        this.ignoredTitleText = value;
        return this;
    }

    public StatusInfoExtension setUnimplementedTitleText(String value) {
        this.unimplementedTitleText = value;
        return this;
    }

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationProcessingListener(this);
    }

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {}

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
        Element body = getSpecification(event);

        if (body != null) {
            Element[] examples = getAllExamplesInSpecification(body);

            for (Element example : examples) {
                String status = example.getAttributeValue("status", NAMESPACE_URI);
                String statusText = example.getAttributeValue("example", NAMESPACE_URI);

                if (exampleStatusIsIgnoredOrUnimplementedOrExpectedToFail(status, statusText)) {
                    setExampleStatus(example, status, statusText);
                }
            }
        }
    }

    private Element[] getAllExamplesInSpecification(Element body) {
        return body.getChildElements("div");
    }

    private void setExampleStatus(Element div, String status, String statusText) {
        Element exampleStatus = getExampleStatus(div);

        switch (status.toLowerCase().trim()) {
            case "expectedtofail":
                setStatus(statusText, exampleStatus, expectedToFailTitleText);
                break;
            case "ignored":
                setStatus(statusText, exampleStatus, ignoredTitleText);
                break;
            case "unimplemented":
                setStatus(statusText, exampleStatus, unimplementedTitleText);
                break;
            default:
        }

        removeOriginalElement(div);
    }

    private Element getSpecification(SpecificationProcessingEvent event) {
        return event.getRootElement().getFirstChildElement("body");
    }

    private void removeOriginalElement(Element div) {
        div.removeChild(getExampleStatus(div));
    }

    private Element getExampleStatus(Element div) {
        return div.getFirstChildElement("p");
    }

    private void setStatus(String statusText, Element exampleStatus, String status) {
        exampleStatus.appendSister(newMessage(reasonPrefix, reasonPrefix + statusText));
        exampleStatus.appendSister(
                newMessage(titleTextPrefix, titleTextPrefix + status));
    }

    private boolean exampleStatusIsIgnoredOrUnimplementedOrExpectedToFail(String status, String statusText) {
        return status != null && statusText != null;
    }

    private Element newMessage(String styleClass, String message) {
        Element statusNote = new Element(messageSize);
        statusNote.appendText(message);
        statusNote.addStyleClass(styleClass);
        statusNote.addAttribute("style", style);

        return statusNote;
    }

}

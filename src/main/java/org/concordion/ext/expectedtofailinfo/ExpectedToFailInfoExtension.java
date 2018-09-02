package org.concordion.ext.expectedtofailinfo;

import org.concordion.api.Element;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

/**
 * Displays the Note and a Reason in the corresponding specification/markdown,
 * when the annotation expectedToFail is used.
 *
 * <p></p>
 * Sample usage:
 * <p></p>
 * <pre>
 * In a Fixture class annotate the class with:
 * \@Extensions({ ExpectedToFailInfoExtension.class })
 *
 * To a specification add:
 * ## [My Specification Name](- "Reason my specification is failing c:status=expectedToFail")
 * </pre>
 * <p></p>
 * In the completed specification:
 * <ul>
 * <li>Note: resolves to 'This example has been marked as EXPECTED_TO_FAIL'</li>
 * <li>Reason: Based on the sample usage above, would resolve to 'Reason my specification is failing'</li>
 * </ul>
 * <p></p>
 * This will be the case for the ignored and unimplemented statuses also. Please see the demo of the is project for more examples.
 *
 * @author Luke Pearson
 *
 */
public class ExpectedToFailInfoExtension implements SpecificationProcessingListener, ConcordionExtension {

    private static final String NAMESPACE_URI = "http://www.concordion.org/2007/concordion";
    private String STYLE = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private String MESSAGE_SIZE = "h3";

    private String EXPECTED_TO_FAIL_TEXT = "This example has been marked as - EXPECTED TO FAIL";
    private String IGNORED_TEXT = "This example has been marked as - IGNORED";
    private String UNIMPLEMENTED_TEXT = "This example has been marked as - UNIMPLEMENTED";
    private String NOTE = "Note";
    private String REASON = "Reason";


    public ExpectedToFailInfoExtension setStyle(String style) {
        this.STYLE = style;
        return this;
    }

    public ExpectedToFailInfoExtension setHeaderElementSize(String value) {
        this.MESSAGE_SIZE = value;
        return this;
    }

    public ExpectedToFailInfoExtension setNoteMessage(String value) {
        this.NOTE = value;
        return this;
    }

    public ExpectedToFailInfoExtension setReasonMessage(String value) {
        this.REASON = value;
        return this;
    }

    public ExpectedToFailInfoExtension setExpectedToFailTest(String value) {
        this.EXPECTED_TO_FAIL_TEXT = value;
        return this;
    }

    public ExpectedToFailInfoExtension setIgnoredText(String value) {
        this.IGNORED_TEXT = value;
        return this;
    }

    public ExpectedToFailInfoExtension setUnimplementedText(String value) {
        this.UNIMPLEMENTED_TEXT = value;
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
        Element body = event.getRootElement().getFirstChildElement("body");

        if (body != null) {
            Element[] divs = body.getChildElements("div");

            for (Element div : divs) {
                String status = div.getAttributeValue("status", NAMESPACE_URI);
                String statusReason = div.getAttributeValue("example", NAMESPACE_URI);

                if (status != null && statusReason != null) {
                    Element e = div.getFirstChildElement("p");

                    switch(status.toLowerCase()) {
                        case "expectedtofail":
                            e.appendSister(newMessage(REASON, REASON + ": " + statusReason));
                            e.appendSister(newMessage(NOTE, NOTE + ": " + EXPECTED_TO_FAIL_TEXT));
                            break;
                        case "ignored":
                            e.appendSister(newMessage(REASON, REASON + ": " + statusReason));
                            e.appendSister(newMessage(NOTE, NOTE + ": " + IGNORED_TEXT));
                            break;
                        case "unimplemented":
                            e.appendSister(newMessage(REASON, REASON + ": " + statusReason));
                            e.appendSister(newMessage(NOTE, NOTE + ": " + UNIMPLEMENTED_TEXT));
                            break;
                        default:
                    }

                    div.removeChild(div.getFirstChildElement("p"));
                }
            }
        }
    }

    private Element newMessage(String styleClass, String message) {
        Element originalExpectedToFailNote = new Element(MESSAGE_SIZE);
        originalExpectedToFailNote.appendText(message);
        originalExpectedToFailNote.addStyleClass(styleClass);
        originalExpectedToFailNote.addAttribute("style", STYLE);

        return originalExpectedToFailNote;
    }

}



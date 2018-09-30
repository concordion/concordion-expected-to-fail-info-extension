package org.concordion.ext.expectedtofailinfo;

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
public class ExpectedToFailInfoExtension implements SpecificationProcessingListener, ConcordionExtension {

    private static final String NAMESPACE_URI = "http://www.concordion.org/2007/concordion";
    private String style = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private String messageSize = "h3";

    private String expectedToFailText = "This example has been marked as EXPECTED TO FAIL";
    private String ignoredText = "This example has been marked as IGNORED";
    private String unimplementedText = "This example has been marked as UNIMPLEMENTED";
    private String note = "Note";
    private String reason = "Reason";

    private static final List<String> ALLOWED_MESSAGE_SIZES = new ArrayList<String>(Arrays.asList("h1","h2","h3","h4","h5","h6"));


    public ExpectedToFailInfoExtension setStyle(String style) {
        this.style = style;
        return this;
    }

    public ExpectedToFailInfoExtension setHeaderElementSize(String value) {
        this.messageSize = ALLOWED_MESSAGE_SIZES.contains(value.toLowerCase())?value:null;
        return this;
    }

    public ExpectedToFailInfoExtension setNoteMessage(String value) {
        this.note = value;
        return this;
    }

    public ExpectedToFailInfoExtension setReasonMessage(String value) {
        this.reason = value;
        return this;
    }

    public ExpectedToFailInfoExtension setExpectedToFailTest(String value) {
        this.expectedToFailText = value;
        return this;
    }

    public ExpectedToFailInfoExtension setIgnoredText(String value) {
        this.ignoredText = value;
        return this;
    }

    public ExpectedToFailInfoExtension setUnimplementedText(String value) {
        this.unimplementedText = value;
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

                    switch (status.toLowerCase()) {
                        case "expectedtofail":
                            e.appendSister(newMessage(reason, reason + ": " + statusReason));
                            e.appendSister(newMessage(note, note + ": " + expectedToFailText));
                            break;
                        case "ignored":
                            e.appendSister(newMessage(reason, reason + ": " + statusReason));
                            e.appendSister(newMessage(note, note + ": " + ignoredText));
                            break;
                        case "unimplemented":
                            e.appendSister(newMessage(reason, reason + ": " + statusReason));
                            e.appendSister(newMessage(note, note + ": " + unimplementedText));
                            break;
                        default:
                    }

                    div.removeChild(div.getFirstChildElement("p"));
                }
            }
        }
    }

    private Element newMessage(String styleClass, String message) {
        Element embellishedStatusNote = new Element(messageSize);
        embellishedStatusNote.appendText(message);
        embellishedStatusNote.addStyleClass(styleClass);
        embellishedStatusNote.addAttribute("style", style);

        return embellishedStatusNote;
    }

}

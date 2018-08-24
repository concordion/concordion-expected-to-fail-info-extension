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
 * @author Luke Pearson
 *
 */
public class ExpectedToFailInfoExtension implements ConcordionExtension, SpecificationProcessingListener {

    public static final String NAMESPACE_URI = "http://www.concordion.org/2007/concordion";
    private String STYLE = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private String EXPECTED_TO_FAIL_MESSAGE_SIZE = "h3";

    private final String EXPECTED_TO_FAIL_TEXT = "This example has been marked as - EXPECTED TO FAIL";
    private final String IGNORED_TEXT = "This example has been marked as - IGNORED";
    private final String UNIMPLEMENTED_TEXT = "This example has been marked as - UNIMPLEMENTED";

    private String NOTE = "Note";
    private String REASON = "Reason";


    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withExampleListener(this);
    }

    public ExpectedToFailInfoExtension setStyle(String style) {
        this.STYLE = style;
        return this;
    }

    /**
     * Takes a string value to set as the html tag for the message (refer to default for baseline).
     * @param headerElementSize
     * @return
     */
    public ExpectedToFailInfoExtension setHeaderElementSize(String headerElementSize) {
        this.EXPECTED_TO_FAIL_MESSAGE_SIZE = headerElementSize;
        return this;
    }

    public ExpectedToFailInfoExtension setNoteMessage(String newNoteMessage) {
        this.NOTE = newNoteMessage;
        return this;
    }

    public ExpectedToFailInfoExtension setReasonMessage(String newReasonMessage) {
        this.REASON = newReasonMessage;
        return this;
    }

    private Element newMessage(String message, String className) {
        Element originalExpectedToFailNote = new Element(EXPECTED_TO_FAIL_MESSAGE_SIZE);

        originalExpectedToFailNote.appendText(message);
        originalExpectedToFailNote.addStyleClass(className);
        originalExpectedToFailNote.addAttribute("style", STYLE);

        return originalExpectedToFailNote;
    }

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {

    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
        //TODO fix specStatusReason
        String specStatusReason = event.getExampleName();
        Element body = event.getRootElement().getFirstChildElement("body");

        if (body != null) {
            Element[] divs = body.getChildElements("div");

            for (Element div : divs) {
                String concordionStatusAttribute = div.getAttributeValue("status", NAMESPACE_URI);
                String concordionExampleAttribute = div.getAttributeValue("example", NAMESPACE_URI);

                // TODO: LukeSP - Add in support for additional 'Reason' and 'Note' for the 'Ignored' and 'Unimplemented' statuses
                if (concordionStatusAttribute != null && concordionExampleAttribute != null) {
                    if (concordionStatusAttribute.equalsIgnoreCase("expectedToFail") && concordionExampleAttribute.equals(specStatusReason)) {
                        Element failingDiv = div.getFirstChildElement("p");

                        failingDiv.appendSister(newMessage(REASON + ": " + specStatusReason, REASON));
                        failingDiv.appendSister(newMessage(NOTE + ": " + EXPECTED_TO_FAIL_TEXT, NOTE));

                        div.removeChild(div.getFirstChildElement("p"));
                    }
                }
            }
        }

    }
}

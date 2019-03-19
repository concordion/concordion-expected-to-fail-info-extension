package org.concordion.ext.statusinfo;

import org.concordion.api.Element;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

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

    private StatusInfo statusInfo = new StatusInfo();

    public StatusInfoExtension setStatusInfo(StatusInfo statusInfo){
        this.statusInfo = statusInfo;
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
        Element spec = getSpecification(event);

        if (spec == null)
            return;

        Element[] examples = getAllExamplesInSpec(spec);

        for (Element example : examples) {
            String status = example.getAttributeValue("status", NAMESPACE_URI);
            String statusText = example.getAttributeValue("example", NAMESPACE_URI);

            if (isNotNull(status) && isNotNull(statusText)) {
                setExampleStatus(example, status, statusText);
                removeOriginalStatusElement(example);
            }
        }
    }

    private void setSomeValueToGetLater() {
    }

    private void setExampleStatus(Element example, String status, String statusText) {
        switch (status.toLowerCase().trim()) {
            case "expectedtofail":
                setNewStatusTextWithReason(example, statusText, statusInfo.getExpectedToFailTitleText());
                break;
            case "ignored":
                setNewStatusTextWithReason(example, statusText, statusInfo.getIgnoredTitleText());
                break;
            case "unimplemented":
                setNewStatusTextWithReason(example, statusText, statusInfo.getUnimplementedTitleText());
                break;
            default:
        }
    }

    private void removeOriginalStatusElement(Element example) { example.removeChild(originalExampleStatus(example)); }

    private void setNewStatusTextWithReason(Element example, String statusText, String status) {
        Element originalExampleStatus = originalExampleStatus(example);

        originalExampleStatus.appendSister(
                newMessage(statusInfo.getReasonPrefix(), statusInfo.getReasonPrefix() + statusText));

        originalExampleStatus.appendSister(
                newMessage(statusInfo.getTitleTextPrefix(), statusInfo.getTitleTextPrefix() + status));
    }

    private Element[] getAllExamplesInSpec(Element body) {
        return body.getChildElements("div");
    }

    private Element getSpecification(SpecificationProcessingEvent event) {
        return event.getRootElement().getFirstChildElement("body");
    }

    private Element originalExampleStatus(Element example) {
        return example.getFirstChildElement("p");
    }

    private Element newMessage(String styleClass, String message) {
        Element statusNote = new Element(statusInfo.getMessageSize());
        statusNote.appendText(message);
        statusNote.addStyleClass(styleClass);
        statusNote.addAttribute("style", statusInfo.getStyle());

        return statusNote;
    }

    private boolean isNotNull(String status) { return status != null; }

}

package org.concordion.ext.statusinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusInfo {

    private String style = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private String messageSize = "h5";
    private String titleTextPrefix = "";
    private String reasonPrefix = "Reason:";
    private String expectedToFailTitleText = "This example has been marked as EXPECTED TO FAIL";
    private String ignoredTitleText = "This example has been marked as IGNORED";
    private String unimplementedTitleText = "This example has been marked as UNIMPLEMENTED";

    private static final List<String> ALLOWED_MESSAGE_SIZES = new ArrayList<>(
            Arrays.asList("h1","h2","h3","h4","h5","h6"));

    public StatusInfo withStyle(String style) {
        this.style = style;
        return this;
    }

    public StatusInfo setMessageSize(String newMessageSize) {
        this.messageSize = ALLOWED_MESSAGE_SIZES.contains(newMessageSize.toLowerCase())?newMessageSize:messageSize;
        return this;
    }

    public StatusInfo setTitleTextPrefix(String value) {
        this.titleTextPrefix = value;
        return this;
    }

    public StatusInfo setReasonPrefixMessage(String value) {
        this.reasonPrefix = value;
        return this;
    }

    public StatusInfo setExpectedToFailTitleText(String value) {
        this.expectedToFailTitleText = value;
        return this;
    }

    public StatusInfo setIgnoredTitleText(String value) {
        this.ignoredTitleText = value;
        return this;
    }

    public StatusInfo setUnimplementedTitleText(String value) {
        this.unimplementedTitleText = value;
        return this;
    }

    String getMessageSize() {
        return messageSize;
    }

    String getStyle() {
        return style;
    }

    String getTitleTextPrefix() {
        return titleTextPrefix;
    }

    String getReasonPrefix() {
        return reasonPrefix;
    }

    String getExpectedToFailTitleText() {
        return expectedToFailTitleText;
    }

    String getIgnoredTitleText() {
        return ignoredTitleText;
    }

    String getUnimplementedTitleText() {
        return unimplementedTitleText;
    }
}

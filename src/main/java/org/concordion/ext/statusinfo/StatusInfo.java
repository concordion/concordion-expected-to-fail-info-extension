package org.concordion.ext.statusinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusInfo {

    private String style = "font-weight: normal; text-decoration: none; color: #bb5050;";
    private String messageSize = "h5";
    private String titleTextPrefix = "";
    private String reasonPrefix = "Reason: ";
    private String expectedToFailTitleText = "This example has been marked as EXPECTED TO FAIL";
    private String ignoredTitleText = "This example has been marked as IGNORED";
    private String unimplementedTitleText = "This example has been marked as UNIMPLEMENTED";

    StatusInfo() {}

    String getMessageSize() {
        return messageSize;
    }

    String getStyle() {
        return style;
    }

    String getTitleTextPrefix() { return titleTextPrefix; }

    String getUnimplementedTitleText() { return unimplementedTitleText; }

    String getIgnoredTitleText() { return ignoredTitleText; }

    String getExpectedToFailTitleText() { return expectedToFailTitleText; }

    String getReasonPrefix() { return reasonPrefix; }

    public static class StatusInfoBuilder {

        private static final List<String> ALLOWED_MESSAGE_SIZES = new ArrayList<>(
                Arrays.asList("h1","h2","h3","h4","h5","h6"));

        private String style;
        private String messageSize;
        private String titleTextPrefix;
        private String reasonPrefix;
        private String expectedToFailTitleText;
        private String ignoredTitleText;
        private String unimplementedTitleText;


        public StatusInfoBuilder(){}

        public StatusInfoBuilder withStyle(String style) {
            this.style = style;
            return this;
        }

        public StatusInfoBuilder setMessageSize(String newMessageSize) {
            this.messageSize = ALLOWED_MESSAGE_SIZES.contains(newMessageSize.toLowerCase())?newMessageSize:messageSize;
            return this;
        }

        public StatusInfoBuilder setTitleTestPrefix(String value) {
            this.titleTextPrefix = value;
            return this;
        }

        public StatusInfoBuilder setReasonPrefixMessage(String value) {
            this.reasonPrefix = value;
            return this;
        }

        public StatusInfoBuilder setExpectedToFailTitleText(String value) {
            this.expectedToFailTitleText = value;
            return this;
        }

        public StatusInfoBuilder setIgnoredTitleText(String value) {
            this.ignoredTitleText = value;
            return this;
        }

        public StatusInfoBuilder setUnimplementedTitleText(String value) {
            this.unimplementedTitleText = value;
            return this;
        }

        public StatusInfo build() {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.style = useDefaultValueIfNull(this.style, statusInfo.style);
            statusInfo.messageSize = useDefaultValueIfNull(this.messageSize, statusInfo.messageSize);
            statusInfo.titleTextPrefix = useDefaultValueIfNull(this.titleTextPrefix, statusInfo.titleTextPrefix);
            statusInfo.reasonPrefix = useDefaultValueIfNull(this.reasonPrefix, statusInfo.reasonPrefix);
            statusInfo.expectedToFailTitleText = useDefaultValueIfNull(this.expectedToFailTitleText, statusInfo.expectedToFailTitleText);
            statusInfo.ignoredTitleText = useDefaultValueIfNull(this.ignoredTitleText, statusInfo.ignoredTitleText);
            statusInfo.unimplementedTitleText = useDefaultValueIfNull(this.unimplementedTitleText,statusInfo.unimplementedTitleText);

            return statusInfo;
        }

        private String useDefaultValueIfNull(String newValue, String defaultValue) {
            if (newValue == null) {
              return defaultValue;
            }
            return newValue;
        }
    }

}

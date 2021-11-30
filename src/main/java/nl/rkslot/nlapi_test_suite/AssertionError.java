package nl.rkslot.nlapi_test_suite;

import org.jetbrains.annotations.NotNull;

public class AssertionError extends java.lang.AssertionError {

    public AssertionError(String message) {
        super(message);

        this.sendWebhookNotification(message);
    }

    private void sendWebhookNotification(final @NotNull String message) {
        // Send webhook notification
    }

}

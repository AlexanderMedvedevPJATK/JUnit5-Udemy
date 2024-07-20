package com.s28572.junit.service.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;

public class ThrowableExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        // Any exception except for IOException will be ignored and only IOExceptions are propagated
        if (throwable instanceof IOException) {
            throw throwable;
        }
    }
}

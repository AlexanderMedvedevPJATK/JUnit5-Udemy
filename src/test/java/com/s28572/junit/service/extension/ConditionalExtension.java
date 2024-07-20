package com.s28572.junit.service.extension;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Properties;

public class ConditionalExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return System.getProperty("skip") != null ?
                ConditionEvaluationResult.disabled("Test is skipped") :
                ConditionEvaluationResult.enabled("Enabled by default");
    }
}

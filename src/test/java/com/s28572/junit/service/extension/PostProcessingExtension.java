package com.s28572.junit.service.extension;

import com.s28572.junit.service.config.GetUserService;
import com.s28572.junit.service.UserService;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class PostProcessingExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        System.out.println("Post processing extension");
        Field[] declaredFields = testInstance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(GetUserService.class)) {
                declaredField.setAccessible(true);
                declaredField.set(testInstance, new UserService(null));
            }
        }
    }
}

package com.nik.junit.extension;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.launcher.TestExecutionListener;

public class GlobalExtension implements BeforeAllCallback, AfterTestExecutionCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("before all callback");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        System.out.println("after test execution");
    }
}

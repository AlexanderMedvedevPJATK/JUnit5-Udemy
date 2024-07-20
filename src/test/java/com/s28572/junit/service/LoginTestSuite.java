package com.s28572.junit.service;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses(UserServiceTest.LoginTest.class)
//@IncludeTags("login")
public class LoginTestSuite {
}

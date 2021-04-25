package com.epam.data.handler.core;

import com.epam.data.handler.config.SpringConfig;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {SpringConfig.class})
public abstract class AbstractUnitTest extends AbstractTest {

}

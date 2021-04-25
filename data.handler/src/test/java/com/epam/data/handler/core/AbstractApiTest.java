package com.epam.data.handler.core;

import com.epam.data.handler.config.SpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = {SpringConfig.class})
public abstract class AbstractApiTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;
    @Value("${admin.login}")
    protected String login;
    @Value("${admin.password}")
    protected String password;
    protected MediaType mediaTypeJson = new MediaType(MediaType.APPLICATION_JSON);
}

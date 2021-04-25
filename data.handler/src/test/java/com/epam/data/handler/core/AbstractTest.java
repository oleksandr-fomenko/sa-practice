package com.epam.data.handler.core;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gcp.autoconfigure.datastore.DatastoreProvider;
import org.springframework.cloud.gcp.autoconfigure.datastore.GcpDatastoreAutoConfiguration;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTemplate;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTransactionManager;
import org.springframework.cloud.gcp.data.datastore.core.convert.ObjectToKeyFactory;
import org.springframework.cloud.gcp.data.datastore.core.mapping.DatastoreMappingContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ExtendWith(SpringExtension.class)
public abstract class AbstractTest {
    @MockBean
    private DatastoreTransactionManager datastoreTransactionManager;
    @MockBean
    private DatastoreTemplate datastoreTemplate;
    @MockBean
    private DatastoreMappingContext datastoreMappingContext;
    @MockBean
    private GcpDatastoreAutoConfiguration gcpDatastoreAutoConfiguration;
    @MockBean
    private DatastoreProvider daatastoreProvider;
    @MockBean
    private ObjectToKeyFactory objectToKeyFactory;
}

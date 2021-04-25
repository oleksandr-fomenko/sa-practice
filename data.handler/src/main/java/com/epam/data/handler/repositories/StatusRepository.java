package com.epam.data.handler.repositories;

import com.epam.data.handler.entities.Status;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;


public interface StatusRepository extends DatastoreRepository<Status, Integer> {

}
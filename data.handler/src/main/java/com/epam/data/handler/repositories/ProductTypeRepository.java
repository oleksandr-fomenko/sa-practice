package com.epam.data.handler.repositories;

import com.epam.data.handler.entities.ProductType;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;


public interface ProductTypeRepository extends DatastoreRepository<ProductType, Integer> {

}
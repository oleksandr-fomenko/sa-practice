package com.epam.data.handler.repositories;

import com.epam.data.handler.entities.Product;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface ProductRepository extends DatastoreRepository<Product, Integer> {

}
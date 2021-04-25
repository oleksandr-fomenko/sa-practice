package com.epam.data.handler.core;

import com.epam.data.handler.entities.Product;
import com.epam.data.handler.entities.ProductType;
import com.epam.data.handler.entities.Status;
import com.epam.data.handler.utils.GenericBuilder;

import java.util.HashSet;
import java.util.Set;

public class DataFactory {

    public static Product getProductOne(String description){
        return GenericBuilder.of(Product::new)
                .with(p -> p.setId(1L))
                .with(p -> p.setCost(10.0))
                .with(p -> p.setProductType(getProductType(description)))
                .with(p -> p.setStatuses(new HashSet<>()))
                .with(p -> p.setDescription(description))
                .build();
    }

    public static Product getProductTwo(String description){
        Set<Status> statuses = new HashSet<>();
        statuses.add(getStatus(description));
        return GenericBuilder.of(Product::new)
                .with(p -> p.setId(2L))
                .with(p -> p.setCost(20.0))
                .with(p -> p.setProductType(getProductType(description)))
                .with(p -> p.setStatuses(statuses))
                .with(p -> p.setDescription(description))
                .build();
    }

    public static ProductType getProductType(String description){
        return GenericBuilder.of(ProductType::new)
                .with(p -> p.setId(1L))
                .with(p -> p.setDescription(description))
                .build();
    }

    public static Status getStatus(String description){
        return GenericBuilder.of(Status::new)
                .with(p -> p.setId(1L))
                .with(p -> p.setDescription(description))
                .build();
    }
}

package com.epam.data.handler.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Descendants;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "product")
public class Product implements Serializable {

    @Id
    @Field(name = "product_id")
    @JsonProperty(value = "productId", index = 0)
    private Long id;

    @Transient
    @JsonProperty(index = 1)
    @NotNull(message = "TestId is mandatory")
    private String testId;

    @Reference
    @JsonProperty(index = 4)
    private ProductType productType;

    @Descendants
    @JsonProperty(index = 7)
    private Set<Status> statuses = new HashSet<>();

    @Transient
    @JsonProperty(index = 8)
    private Integer count;

    @Field(name = "cost")
    @NotNull(message = "Cost is mandatory")
    @JsonProperty(index = 3)
    private Double cost;

    @Field(name = "description")
    @JsonProperty(index = 2)
    private String description;

    @LastModifiedDate
    @Field(name = "modify_date")
    @JsonProperty(index = 6)
    private Date modifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

}

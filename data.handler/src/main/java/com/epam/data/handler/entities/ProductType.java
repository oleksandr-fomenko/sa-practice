package com.epam.data.handler.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity(name = "product_type")
public class ProductType implements Serializable {

	@Id
	@Field(name = "product_type_id")
	@JsonProperty(value = "productTypeId", index = 0)
	private Long id;

	@Field(name = "description")
	@JsonProperty(index = 1)
	@NotBlank
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

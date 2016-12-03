package com.vdms.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Immutable representation of a product (because immutability is good!)
 */
public class Product {

    private String code;

    @NotNull
    private String name;

    @DecimalMin("0")
    private double price;

    @Size(min = 1)
    @Valid
    private Set<String> tags;
    private Map<String, Object> additionalProperties;

    /**
     * Default constructor, which exists purely for serialization purposes. This should not be called directly.
     */
    public Product() {}

    /**
     * Constructs a Product instance
     *
     * @param tags A set of tags associated with the product
     * @param price The product's price
     * @param name The product's name
     */
    public Product(String name, double price, Set<String> tags, Map<String, Object> additionalProperties) {
        this.code = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.tags = tags;
        this.additionalProperties = additionalProperties;
    }

    /**
     * The unique identifier for a product. Auto-generated
     */
    public String getCode() {
        return code;
    }

    /**
     * Name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * The price
     */
    public double getPrice() {
        return price;
    }

    /**
     * The tags associated with a product
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * Any (optional) properties associated with the product
     */
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .append(name)
                .append(price)
                .append(tags)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Product) == false) {
            return false;
        }
        Product rhs = ((Product) other);
        return new EqualsBuilder()
                .append(code, rhs.code)
                .append(name, rhs.name)
                .append(price, rhs.price)
                .append(tags, rhs.tags)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }

}
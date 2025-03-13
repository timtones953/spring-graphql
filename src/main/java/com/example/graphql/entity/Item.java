package com.example.graphql.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

@Container(containerName = "products", autoCreateContainer = false)
public class Item {
    private String id;
    private String name;
    private Integer quantity;
    private Boolean sale;

    @PartitionKey
    private String category;

    // Extra members omitted for brevity
}
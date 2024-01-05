package com.ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {
    private Long productId;
    private String productName;

    private String description;

    private Double importPrice;
    private Double exportPrice;

    private Integer stockQuantity;
    private String categoryName;

    private Date createdAt = new Date();
}

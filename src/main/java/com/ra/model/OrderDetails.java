package com.ra.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetails {
    @Id
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    private Double unitPrice;

    private Integer orderQuantity;
}

package com.danny.shoppingplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Integer id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                ", rating=" + (rating != null
                ? "{rate=" + rating.getRate() + ", count=" + rating.getCount() + "}"
                : "null") +
                '}';
    }

    @Getter
    @Setter
    public static class Rating {
        private Double rate;
        private Integer count;
    }
}
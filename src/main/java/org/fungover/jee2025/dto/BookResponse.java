package org.fungover.jee2025.dto;


import jakarta.json.bind.annotation.JsonbTypeInfo;

import java.math.BigDecimal;

public class BookResponse {

    private String title;

    private int pages;

    private BigDecimal price = BigDecimal.TEN;

    private String priceWithCurrency = "10 SEK";

    public BookResponse() {
    }

    public BookResponse(String title, int pages) {
        this.title = title;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceWithCurrency() {
        return priceWithCurrency;
    }

    public void setPriceWithCurrency(String priceWithCurrency) {
        this.priceWithCurrency = priceWithCurrency;
    }
}

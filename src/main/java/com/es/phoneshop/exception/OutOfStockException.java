package com.es.phoneshop.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends RuntimeException {
    private Product product;
    private int quantityRequested;
    private int stockAvailable;

    public OutOfStockException(Product product, int quantityRequested, int stockAvailable) {
        this.product = product;
        this.quantityRequested = quantityRequested;
        this.stockAvailable = stockAvailable;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }
}

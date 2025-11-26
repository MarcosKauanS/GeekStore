package com.geekstore.geekstore.modules.cart.model;
import com.geekstore.geekstore.modules.product.model.Product;


public class CartProduct {
    private final Product product;
    private final int quantity;

    public CartProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

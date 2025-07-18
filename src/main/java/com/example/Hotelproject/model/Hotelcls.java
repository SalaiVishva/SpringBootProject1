package com.example.Hotelproject.model;

import jakarta.persistence.*;

@Entity
@Table(name="Orders")
public class Hotelcls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long total;
    private String productname;
    private Long quantity;
    private Long price;
    private String fileName;
    public Long getId() {
        return id;
    }

    @Lob
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public Long getTotal() {
        return total;
    }

    private void updateTotal() {
        if (this.price != null && this.quantity != null) {
            this.total = this.price * this.quantity;
        }
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProduct_name() {
        return productname;
    }

    public void setProduct_name(String product_name) {
        this.productname = product_name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
        updateTotal();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
        updateTotal();
    }
}

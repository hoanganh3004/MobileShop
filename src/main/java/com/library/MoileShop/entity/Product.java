package com.library.MoileShop.entity;

public class Product {
    private int id;
    private String name;
    private String masp;
    private double price;
    private String description;
    private String image;
    private int quantity;
    private int categoryId;


    public Product() {
    }

    // ✅ Constructor đầy đủ
    public Product(int id, String name, String masp, double price, String description,
                   String image, int quantity, int categoryId) {
        this.id = id;
        this.name = name;
        this.masp = masp;
        this.price = price;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.categoryId = categoryId;

    }

    // ✅ Constructor cũ (vẫn giữ nguyên để không lỗi nơi khác)
    public Product(int id, String name, String masp, double price, String description, String image, int quantity) {
        this.id = id;
        this.name = name;
        this.masp = masp;
        this.price = price;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
    }

    // ✅ Constructor mới để dùng trong AdminProductDAO
    public Product(int id, String name, double price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    // Getter / Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}

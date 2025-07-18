package com.library.MoileShop.entity;

import java.sql.Timestamp;

public class Order {
    private int id;
    private String userCode; // Thêm trường userCode để khớp với user_code trong bảng orders
    private Account customer;
    private Timestamp orderDate;
    private String status;
    private String cancelReason;
    private double total;
    private String recipientName;
    private String recipientEmail; // Thêm trường recipientEmail
    private String recipientPhone;
    private String recipientAddress;

    public Order() {
    }

    public Order(int id, String userCode, Account customer, Timestamp orderDate, String status, String cancelReason, double total,
                 String recipientName, String recipientEmail, String recipientPhone, String recipientAddress) {
        this.id = id;
        this.userCode = userCode;
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
        this.cancelReason = cancelReason;
        this.total = total;
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.recipientPhone = recipientPhone;
        this.recipientAddress = recipientAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Account getCustomer() {
        return customer;
    }

    public void setCustomer(Account customer) {
        this.customer = customer;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", customer=" + customer +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", cancelReason='" + cancelReason + '\'' +
                ", total=" + total +
                ", recipientName='" + recipientName + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", recipientAddress='" + recipientAddress + '\'' +
                '}';
    }
}
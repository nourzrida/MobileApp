package com.example.teskertievents;

public class TicketType {
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int maxQuantity;

    public TicketType(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = 0;
        this.maxQuantity = 10; // Default max quantity
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0 && quantity <= maxQuantity) {
            this.quantity = quantity;
        }
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public void incrementQuantity() {
        if (quantity < maxQuantity) {
            quantity++;
        }
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }
}
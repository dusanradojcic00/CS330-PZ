package com.metropolitan.it355.entity;

public class Car {
    private Long id;
    private String model;
    private String maker;
    private String image;
    private int hp;
    private int gears;
    private int seats;
    private String fuel;
    private String transmission;
    private Double price;

    public Car(Long id, String model, String maker, String image, int hp, int gears, int seats, String fuel, String transmission, Double price) {
        this.id = id;
        this.model = model;
        this.maker = maker;
        this.image = image;
        this.hp = hp;
        this.gears = gears;
        this.seats = seats;
        this.fuel = fuel;
        this.transmission = transmission;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        this.gears = gears;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

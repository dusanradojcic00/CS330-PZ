package com.metropolitan.it355.entity;

import java.time.LocalDate;
import java.util.Date;

public class Reservation {
    private Long id;
    private LocalDate startingDate;
    private LocalDate endingDate;
    private Car car;
    private User user;

    public Reservation(Long id, LocalDate startingDate, LocalDate endingDate, Car car, User user) {
        this.id = id;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.car = car;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

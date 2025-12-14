package org.example;

import java.util.Date;

public class Booking {
    private int userId;
    private int roomNumber;
    private Date checkIn;
    private Date checkOut;
    private int totalCost;

    // Snapshot des données au moment de la réservation
    private RoomType roomTypeAtBooking;
    private int pricePerNightAtBooking;
    private int userBalanceAtBooking;

    public Booking(int userId, int roomNumber, Date checkIn, Date checkOut,
                   int totalCost, RoomType roomType, int pricePerNight, int userBalance) {
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalCost = totalCost;
        this.roomTypeAtBooking = roomType;
        this.pricePerNightAtBooking = pricePerNight;
        this.userBalanceAtBooking = userBalance;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public RoomType getRoomTypeAtBooking() {
        return roomTypeAtBooking;
    }

    public int getPricePerNightAtBooking() {
        return pricePerNightAtBooking;
    }

    public int getUserBalanceAtBooking() {
        return userBalanceAtBooking;
    }

    @Override
    public String toString() {
        return "Booking [User ID: " + userId + ", Room Number: " + roomNumber +
                ", Check-In: " + checkIn + ", Check-Out: " + checkOut +
                ", Total Cost: " + totalCost + ", Room Type: " + roomTypeAtBooking +
                ", Price/Night: " + pricePerNightAtBooking +
                ", User Balance at Booking: " + userBalanceAtBooking + "]";
    }
}
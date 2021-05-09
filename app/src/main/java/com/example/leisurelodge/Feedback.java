package com.example.leisurelodge;

public class Feedback {
    String id;
    String hotelName;
    String hotelId;
    String userId;
    String userName;
    int rate;
    String comment;

    public Feedback(String id, String hotelName, String hotelId, String userId, String userName, int rate, String comment) {
        this.id = id;
        this.hotelName = hotelName;
        this.hotelId = hotelId;
        this.userId = userId;
        this.userName = userName;
        this.rate = rate;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

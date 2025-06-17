package com.sample.category.response;

public class ErrorResponse {

    private int status;
    private String message;
    private String timeStamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = java.time.LocalDateTime.now().toString();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

package edu.cuhk.csci3310.wordle;

public class Achievement {
    private String name;
    private Boolean status;
    private String toastMessage;

    public Achievement(String name, Boolean status, String tm) {
        this.name = name;
        this.status = status;
        this.toastMessage = tm;
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

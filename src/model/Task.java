package src.model;

import java.util.Calendar;

public class Task {
    private String name;
    private String info;
    private Calendar dateTime;
    private String contacts;
    private boolean active;

    public Task(String name, String info, Calendar dateTime, String contacts){
        this.name = name;
        this.info = info;
        this.dateTime = dateTime;
        this.contacts = contacts;
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Task\n" +
                "Name: " + name + "\n" +
                "Info: " + info + "\n" +
                "DateNotify: " + dateTime.getTime() + "\n" +
                "Contacts: " + contacts + "\n" +
                "Is Active: " + active;
    }
}

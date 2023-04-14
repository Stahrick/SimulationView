package com.example.simulationview.Models;

public class Message {
    private int time;
    private String sender;
    private String receiver;
    private String type;

    public Message(int time, String sender, String receiver, String type) {
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getType() {
        return type;
    }
}

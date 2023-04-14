package com.example.simulationview.Models;

public class Position {
    private String name;
    private int time;
    private double x;
    private double y;

    public Position(String name, int time, double x, double y) {
        this.name = name;
        this.time = time;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

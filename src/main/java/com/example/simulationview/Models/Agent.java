package com.example.simulationview.Models;

import com.example.simulationview.HelloController;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Agent {
    private Circle circ;
    private Label tooltip;
    private String name;
    private String classname;
    private int id;

    public Agent(int id, String name, String classname, double x, double y, Color fillColor) {
        this.circ = new Circle();
        this.id = id;
        this.name = name;
        this.classname = classname;
        this.moveAgent(x,y);
        if(fillColor == null) {fillColor = pickRandomColor();}
        this.circ.setFill(fillColor);
        this.circ.setRadius(3);
    }

    public void moveAgent(double newX, double newY) {
        this.circ.setCenterX(HelloController.displayMiddleX + newX);
        this.circ.setCenterY(HelloController.displayMiddleY + newY);
    }

    public void displayMsg(String msg) {}

    public Circle getCirc() {return this.circ;}

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public String getClassname() {
        return this.classname;
    }

    private Color pickRandomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    public void setupToolTip() {
        tooltip = new Label();
        tooltip.setText(this.classname + ":" + this.name);
        tooltip.setTextFill(this.getCirc().getFill());
        tooltip.setLabelFor(this.circ);
        tooltip.setVisible(false);
        this.circ.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                tooltip.setVisible(true);
            } else {
                tooltip.setVisible(false);
            }
        });
        ((Pane) this.circ.getParent()).getChildren().add(tooltip);
    }
}

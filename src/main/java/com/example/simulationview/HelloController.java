package com.example.simulationview;

import com.example.simulationview.Models.Agent;
import com.example.simulationview.Models.Message;
import com.example.simulationview.Models.Position;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Optional;

public class HelloController {
    private int curTime = 0;
    private int maxTime;

    private long lastAnimationUpdate = 0;
    private AnimationTimer animation;
    private boolean isAnimationRunning = false;

    public static double displayMiddleX;
    public static double displayMiddleY;

    @FXML
    private Label curTimeLabel;

    @FXML
    private Pane displayArea;

    @FXML
    private Button pauseButton;

    @FXML
    private TextArea communicationArea;

    @FXML
    private Pane legendPane;

    @FXML
    protected void onSkipBeginButtonClick() {
        curTime = 0;
        communicationArea.setText("");
        updateAgentPositions();
        updateAgentCommunication();
        updateTimeLabel();
    }

    @FXML
    protected void onSkipEndButtonClick() {
        curTime = maxTime;
        communicationArea.setText("");
        updateAgentPositions();
        updateAgentCommunication();
        updateTimeLabel();
    }

    @FXML
    protected void onPreviousFrameButtonClick() {
        if(curTime>0) {
            curTime--;
            updateAgentPositions();
            updateAgentCommunication();
            updateTimeLabel();
        }
    }

    @FXML
    protected void onNextFrameButtonClick() {
        if((maxTime) > curTime) {
            curTime++;
            updateAgentPositions();
            updateAgentCommunication();
            updateTimeLabel();
        }
    }

    @FXML
    protected void onPauseButtonClick() {
        if(isAnimationRunning) {
            animation.stop();
            isAnimationRunning = false;
        }else{
            animation.start();
            isAnimationRunning = true;
        }
    }

    public void setup() {
        displayMiddleX = displayArea.getWidth()/2;
        displayMiddleY = displayArea.getHeight()/2;

        for(int i = 0; i < HelloApplication.agents.size(); i++) {
            Agent agent = HelloApplication.agents.get(i);
            displayArea.getChildren().add(agent.getCirc());
            Label agentLabel = new Label();
            agentLabel.setText(agent.getClassname() + ":" + agent.getName());
            agentLabel.setTextFill(agent.getCirc().getFill());
            legendPane.getChildren().add(agentLabel);
            agent.setupToolTip();
        }
        maxTime = HelloApplication.positions.keySet().size()-1;
        animation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long elapsedSec = (l - lastAnimationUpdate)/1000000;
                if(elapsedSec < 500) return;
                if(curTime >= maxTime) this.stop();
                else {
                    curTime++;
                    updateAgentPositions();
                    updateAgentCommunication();
                    updateTimeLabel();
                    lastAnimationUpdate = l;
                }
            }
        };
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            displayMiddleX = displayArea.getWidth()/2;
            displayMiddleY = displayArea.getHeight()/2;
        };

        displayArea.widthProperty().addListener(stageSizeListener);
        displayArea.heightProperty().addListener(stageSizeListener);
        communicationArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                communicationArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
    }

    private void updateAgentPositions() {
        ArrayList<Position> newPos = HelloApplication.positions.get(curTime);
        ArrayList<Agent> agents = HelloApplication.agents;
        for(Position pos : newPos) {
            String posName = pos.getName();
            Optional<Agent> possibleMatch = agents.stream().filter(agent -> {
                return agent.getName().equals(posName);
            }).findFirst();
            if(possibleMatch.isPresent()) {
                Agent matchedAgent = possibleMatch.get();
                matchedAgent.moveAgent(pos.getX(), pos.getY());
            }
        }
    }

    private void updateAgentCommunication() {
        ArrayList<Message> newMsg = HelloApplication.communication.get(curTime);
        if(newMsg == null) return;
        for(Message msg : newMsg) {
            communicationArea.appendText(msg.getTime() + " - [" + msg.getSender() + " -> "
                    + msg.getReceiver() + "]"
                    + " " + msg.getType() + System.lineSeparator());
        }

    }

    private void updateTimeLabel() {
        curTimeLabel.setText("" + curTime);
    }
}
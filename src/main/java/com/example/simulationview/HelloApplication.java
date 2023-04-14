package com.example.simulationview;

import com.example.simulationview.Models.Agent;
import com.example.simulationview.Models.Message;
import com.example.simulationview.Models.Position;
import com.example.simulationview.Util.DataReader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HelloApplication extends Application {
    public static HashMap<Integer, ArrayList<Position>> positions;
    public static HashMap<Integer, ArrayList<Message>> communication;
    public static ArrayList<Agent> agents;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Simulator Visualizer");
        stage.setScene(scene);
        final HelloController controller = (HelloController)fxmlLoader.getController();
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent window)
            {
                controller.setup();
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        loadData("./");
        launch();
    }

    private static void loadData(String path) {
        Object[] dataFile = DataReader.readData(path);
        positions = (HashMap<Integer, ArrayList<Position>>) dataFile[0];
        communication = (HashMap<Integer, ArrayList<Message>>) dataFile[1];
        agents = (ArrayList<Agent>) dataFile[2];
    }

}
package com.example.simulationview.Util;


import com.example.simulationview.Models.Agent;
import com.example.simulationview.Models.Message;
import com.example.simulationview.Models.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataReader {
    private static AgentIDToName agentIDToName = new AgentIDToName();

    public static Object[] readData(String path) {
        try {
            ArrayList<Agent> agents = readAgentConfig(path);
            HashMap<Integer, ArrayList<Position>> positions = readMovements(path);
            HashMap<Integer, ArrayList<Message>> messages = readCommunication(path);
            Object[] dataFiles = {positions, messages, agents};
            return dataFiles;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HashMap<Integer, ArrayList<Position>> readMovements(String path) throws IOException {
        String fileName = "position.csv";
        HashMap<Integer, ArrayList<Position>> positions = new HashMap<>();

        File f = new File(path, fileName);
        if(!f.exists()) {
            System.err.println("Coudln't read positions.csv");
            System.err.println("Used files have to be in the same directory");
            System.exit(1);
        }

        BufferedReader csvReader = new BufferedReader(new FileReader(f));
        String row;
        csvReader.readLine();
        ArrayList<Position> posTime = new ArrayList<>();
        int lastTime = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Position pos = new Position(data[0], Integer.parseInt(data[1]),
                    Double.parseDouble(data[2].replace(",",".")), Double.parseDouble(data[3].replace(",",".")));
            if(pos.getTime() != lastTime) {
                positions.put(lastTime, posTime);
                lastTime = pos.getTime();
                posTime = new ArrayList<>();
            }
            posTime.add(pos);
        }
        if(posTime.size() > 0) {
            positions.put(lastTime, posTime);
        }
        csvReader.close();
        return positions;
    }

    private static HashMap<Integer, ArrayList<Message>> readCommunication(String path) throws IOException {
        String fileName = "communication.csv";
        HashMap<Integer, ArrayList<Message>> messages = new HashMap<>();

        File f = new File(path, fileName);
        if(!f.exists()) {
            System.err.println("Couldn't read communication.csv");
            System.err.println("Used files have to be in the same directory");
            System.exit(1);
        }

        BufferedReader csvReader = new BufferedReader(new FileReader(f));
        String row;
        csvReader.readLine();
        ArrayList<Message> msgTime = new ArrayList<>();
        int lastTime = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Message msg = new Message(Integer.parseInt(data[0]), agentIDToName.convertIDToName(data[1]),
                    agentIDToName.convertIDToName(data[2]), data[3]);
            if(lastTime != msg.getTime()) {
                messages.put(lastTime, msgTime);
                lastTime = msg.getTime();
                msgTime = new ArrayList<>();
            }
            msgTime.add(msg);
        }
        if(msgTime.size() > 0) {
            messages.put(lastTime, msgTime);
        }
        csvReader.close();
        return messages;
    }

    private static ArrayList<Agent> readAgentConfig(String path) throws IOException, ParserConfigurationException, SAXException {
        String fileName = "input_example.xml";
        ArrayList<Agent> agents = new ArrayList();
        HashMap<Integer, String> convertId = new HashMap<>();

        File file = new File(path, fileName);
        if(!file.exists()) {
            System.err.println("Couldn't read input_example.xml");
            System.err.println("Used files have to be in the same directory");
            System.exit(1);
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList agentList = doc.getElementsByTagName("agentpattern");

        for(int i = 0; i < agentList.getLength(); i++) {
            if(agentList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element node = (Element) agentList.item(i);
                String className = node.getElementsByTagName("class").item(0).getTextContent();
                String name = node.getElementsByTagName("name").item(0).getTextContent();
                int x = Integer.parseInt(node.getElementsByTagName("x").item(0).getTextContent());
                int y = Integer.parseInt(node.getElementsByTagName("y").item(0).getTextContent());
                Agent agent = new Agent(i, name, className, x, y, null);
                agents.add(agent);
                convertId.put(agent.getId(), agent.getName());
            }
        }
        agentIDToName.setConvertTable(convertId);
        return agents;
    }
}

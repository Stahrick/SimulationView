package com.example.simulationview.Util;

import java.util.HashMap;
import java.util.NavigableMap;

public class AgentIDToName {
    private HashMap<Integer, String> convertTable;

    public void setConvertTable(HashMap<Integer, String> convertTable) {
        this.convertTable = convertTable;
    }

    public String convertIDToName(String idStr) {
        int id = Integer.parseInt(idStr);
        return convertTable.get(id);
    }
}

package com.abhinandan.familylocater.Models;

import java.util.List;

public class connections {
    List<String> Connections;

    public connections() {
    }

    public connections(List<String> connections) {
        Connections = connections;
    }

    public List<String> getConnections() {
        return Connections;
    }

    public void setConnections(List<String> connections) {
        Connections = connections;
    }
}

package com.abhinandan.familylocater.Models;

import java.util.List;

public class request {
    List<String> Requests;

    public request() {
    }

    public request(List<String> requests) {
        Requests = requests;
    }

    public List<String> getRequests() {
        return Requests;
    }

    public void setRequests(List<String> requests) {
        Requests = requests;
    }
}

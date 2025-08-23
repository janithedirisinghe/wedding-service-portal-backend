package com.example.WeddingVenderMngSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTestResponseDTO {
    private String status;
    private String message;
    private List<String> availableEndpoints;
    private Object sampleData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAvailableEndpoints() {
        return availableEndpoints;
    }

    public void setAvailableEndpoints(List<String> availableEndpoints) {
        this.availableEndpoints = availableEndpoints;
    }

    public Object getSampleData() {
        return sampleData;
    }

    public void setSampleData(Object sampleData) {
        this.sampleData = sampleData;
    }
}

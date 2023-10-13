package com.example.mapping.model;

import java.util.List;

public class MappingResult {
    private List<String> mappingLines;
    private List<String> newMappings;
    private  List<String> updatedMappings;

    public MappingResult(List<String> mappingLines, List<String> newMappings, List<String> updatedMappings) {
        this.mappingLines = mappingLines;
        this.newMappings = newMappings;
        this.updatedMappings = updatedMappings;
    }

    public List<String> getMappingLines() {
        return mappingLines;
    }

    public List<String> getNewMappings() {
        return newMappings;
    }
    public List<String> getUpdatedMappings(){
        return updatedMappings;
    }

    public String toString(List<String> CurrentList) {
        StringBuilder sb = new StringBuilder();
        for (String str : CurrentList) {
            sb.append(str).append("\n"); // Append each string with a newline character
        }
        return sb.toString();
    }

}

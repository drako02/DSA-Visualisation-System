package com.dsa_visualisation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class NotesLoader {
    private JsonNode noteNode;

    public NotesLoader(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            noteNode = mapper.readTree(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDataStructureNote(String dataStructure, String part) {
        return noteNode.path("DataStructures").path(dataStructure).path(part).asText();
    }

    public String getAlgorithmNote(String algorithm, String part) {
        return noteNode.path("DataStructures").path(algorithm).path(part).asText();
    }
}

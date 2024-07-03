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

    interface Contents {
        String getIntro();
        String getIntroHeading();
        String getOperations();
        String getOperationsHeading();
        String getApplications();
        String getApplicationHeading();

    }
    public Contents getDataStructureNote(String dataStructure, String part) {
//        return noteNode.path("DataStructures").path(dataStructure).path(part).asText();

        Contents content = new Contents(){
            @Override
            public String getIntro(){
                return noteNode.path("DataStructures").path(dataStructure).path(part).get(1).asText();
            }

            @Override
            public String getIntroHeading() {
                return noteNode.path("DataStructures").path(dataStructure).path(part).get(0).asText();
            }

            @Override
            public String getOperations() {
                return noteNode.path("DataStructures").path(dataStructure).path(part).get(1).asText();
            }

            @Override
            public String getOperationsHeading() {
                return noteNode.path("DataStructures").path(dataStructure).path(part).get(0).asText();
            }

            @Override
            public String getApplications() {
                return noteNode.path("DataStructures").path(dataStructure).path(part).get(1).asText();
            }

            @Override
            public String getApplicationHeading() {
                return noteNode.path("DataStructures").path(dataStructure).path(part).get(0).asText();
            }
        };

        return content;
    }

    public String getAlgorithmNote(String algorithm, String part) {
        return noteNode.path("DataStructures").path(algorithm).path(part).asText();
    }

}

package com.dsa_visualisation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CodeLoader {
    private JsonNode codeNode;

    public CodeLoader(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            codeNode = mapper.readTree(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCode(String language) {
        return codeNode.path(language).asText();
    }
}

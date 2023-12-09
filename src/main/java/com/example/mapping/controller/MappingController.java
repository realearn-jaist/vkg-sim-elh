package com.example.mapping.controller;

import com.example.mapping.model.MappingResult;
import com.example.mapping.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class MappingController {

    @Autowired
    private MappingService mappingService;
    @GetMapping("/")
    public String showMappingForm() {
        return "mapping-form"; // Create a Thymeleaf HTML template for the form
    }

    @PostMapping("/perform-mapping")
    public String performMapping(
            @RequestParam("mappingFile") MultipartFile mappingFile,
            @RequestParam("similarityFile") MultipartFile similarityFile,
            @RequestParam("threshold") double threshold,
            Model model
    ) {
        try {
            if (!isValidMappingFile(mappingFile)) {
                model.addAttribute("error", "Invalid mapping file format.");
                return "mapping-form";
            }

            if (!isValidSimilarityFile(similarityFile)) {
                model.addAttribute("error", "Invalid similarity file format.");
                return "mapping-form";
            }

            MappingResult result = mappingService.performMapping(mappingFile, similarityFile, threshold);
            model.addAttribute("result", result);
        } catch (IOException e) {
            model.addAttribute("error", "An error occurred while performing mapping.");
        }
        return "mapping-result";
    }

    @GetMapping("/perform-mapping")
    public String getMapping() {
        return "redirect:/";
    }

    private boolean isValidMappingFile(MultipartFile mappingFile) {
        try {
            String content = new String(mappingFile.getBytes(), StandardCharsets.UTF_8);

            if (content.contains("[[") && content.contains("]]") && content.contains("@collection")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidSimilarityFile(MultipartFile similarityFile) {
        try {
            String content = new String(similarityFile.getBytes(), StandardCharsets.UTF_8);
            String[] lines = content.split("\\r?\\n");

            for (String line : lines) {
                String[] parts = line.split("\t");

                try {
                    double threshold = Double.parseDouble(parts[2]);
                    if (threshold < 0.0 || threshold > 1.0) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}

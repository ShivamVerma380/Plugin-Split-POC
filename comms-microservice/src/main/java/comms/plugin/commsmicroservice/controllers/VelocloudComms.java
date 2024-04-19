package comms.plugin.commsmicroservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.apache.commons.io.FileUtils;

@RestController
public class VelocloudComms {
    // @GetMapping("/config")
    // public ResponseEntity<byte[]> createAndReturnFile() {
    //     try {
    //         Resource resource = new ClassPathResource("config.txt");
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @GetMapping("/config")
    public ResponseEntity<String> getTextFileContent() {
        try {
            // Load the resource file
            Resource resource = new FileSystemResource("/Users/shivamverma/plugin-split-poc/config/config.txt");
            // File file = resource.getFile();

            // Write content to the text file
            String content = "This is a sample config.\nTesting purpose.";
            BufferedWriter writer = new BufferedWriter(new FileWriter(resource.getFile().getAbsolutePath()));
            writer.write(content);
            writer.close();

            return ResponseEntity.status(HttpStatus.OK).body(content);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

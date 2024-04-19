package comms.plugin.commsmicroservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

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
    public ResponseEntity<?> getTextFileContent() {
        try {
            // Load the resource file
            Resource resource = new FileSystemResource("/Users/shivamverma/plugin-split-poc/config/config.json");
            // File file = resource.getFile();

            //Do api calls
            String jsonResponse = doApiCalls();
            
            //Save response
            saveResponse(jsonResponse, resource.getFile().getAbsolutePath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        

            return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveResponse(String jsonResponse, String path) throws IOException {
        // Create ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        // Write JSON to file
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), mapper.readTree(jsonResponse));
        // BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        // writer.write(jsonResponse);
        // writer.close();
    }

    private String doApiCalls() {
        // Defining the API URL
        final String apiUrl = "https://vco18-usvi1.velocloud.net/portal/rest/login/enterpriseLogin";
        String requestBody ="{\"username\":\"admin@redseal.net\", \"password\":\"1redSeal1\"}";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        // Create HttpEntity with headers and body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);


        // Creating a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Making a Post request and retrieving the response
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
        
        System.out.println("API Status Code: " + responseEntity.getStatusCode());
        System.out.println("Cookie"+ responseEntity.getHeaders().get("Set-Cookie"));
        String cookie = responseEntity.getHeaders().get("Set-Cookie").get(0);

        String edgesAPICall = "https://vco18-usvi1.velocloud.net/portal/rest/enterprise/getEnterpriseEdges";
        headers.add("Cookie", cookie);
        requestEntity = new HttpEntity<>(requestBody, headers);
        responseEntity = restTemplate.postForEntity(edgesAPICall, requestEntity, String.class);

        System.out.println("API Status Code: " + responseEntity.getStatusCode());
        System.out.println("Cookies:"+requestEntity.getHeaders().get("Cookie").get(0));
        System.out.println("API Edges : " + responseEntity.getBody());


        return responseEntity.getBody();
    }
}

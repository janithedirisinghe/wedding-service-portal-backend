package com.example.WeddingVenderMngSystem.service;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupabaseStorageService {

    private final String SUPABASE_URL = "https://untgtsclbjlgemwljimq.supabase.co";
    private final String BUCKET_NAME = "wedding-posts";
    private final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVudGd0c2NsYmpsZ2Vtd2xqaW1xIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczODkwNzcyNywiZXhwIjoyMDU0NDgzNzI3fQ.R8mveo0moDnrAXw8mh4OWnVPF4UBR2oeQeFsLhS6iFQ";

    public String uploadFile(MultipartFile file, String fileName) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;

            HttpPut httpPut = new HttpPut(uploadUrl);
            httpPut.setHeader("Authorization", "Bearer " + API_KEY);
            httpPut.setHeader("Content-Type", file.getContentType());
            httpPut.setEntity(new ByteArrayEntity(file.getBytes()));

            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200 || statusCode == 201) {
                // Return the public URL to be saved in the database
                return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET_NAME + "/" + fileName;
            } else {
                throw new RuntimeException("Upload failed. Status: " + statusCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Supabase upload failed", e);
        }
    }
}

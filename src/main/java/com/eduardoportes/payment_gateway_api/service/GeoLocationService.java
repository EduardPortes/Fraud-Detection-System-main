package com.eduardoportes.payment_gateway_api.service;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeoLocationService {

    public String[] getLocationByIp(String ip) throws Exception {
        String apiUrl = "http://ip-api.com/json/" + ip + "?fields=country,city";

        try (CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpGet request = new HttpGet(apiUrl);
            String response = httpClient.execute(request, httpResponse ->
                EntityUtils.toString(httpResponse.getEntity()));

            JsonNode root = new ObjectMapper().readTree(response);
            String city = root.path("city").asText();
            String country = root.path("country").asText();

            if (city.isEmpty() || country.isEmpty()) {
                return new String[]{"Unknown", "Location"};
            }
            return new  String[]{city, country};
        }


    }
}

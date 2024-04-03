package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    private static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=IIcCAxbiSj6sf7BEAvfXDx46urbFxkyQbsHjRdce");

        CloseableHttpResponse response = httpClient.execute(request);
        NasaResponse nasaResponse = mapper.readValue(response.getEntity().getContent(), NasaResponse.class);

        HttpGet secondRequest = new HttpGet(nasaResponse.getUrl());
        CloseableHttpResponse secondResponse = httpClient.execute(secondRequest);
        byte[] bytes = secondResponse.getEntity().getContent().readAllBytes();
        File file = new File("Pa30V_NASA_960.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(bytes);
        bos.flush();
        bos.close();
    }
}
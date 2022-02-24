package edu.escuelaing.arem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Connection {


    public String getClima(String city) throws IOException {

        String answer = "{}";
        URL obj = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=878140986221c8a29bd17d0233b17bce");


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(obj.openStream()))) {
            String inputLine = null;


            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                answer += inputLine;
            }


        } catch (IOException x) {
            System.err.println(x);
        }



        return answer;
    }

}
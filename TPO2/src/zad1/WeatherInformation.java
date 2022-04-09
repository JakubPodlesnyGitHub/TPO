package zad1;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherInformation  implements JsonInterFace{
    private String urlStrig;

    public WeatherInformation(String cityName,String country) {
        urlStrig = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "," + country + "&appid=63e87684b65e3d77c5817015f5e9db29&units=metric";
        System.out.println(country);
        System.out.println(urlStrig);
    }
    public String getWeatherString() throws IOException, ParseException {
        URL url = new URL(urlStrig);
        String cont = " ";
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine())!= null){
            cont += line;
        }
        return this.getInformationWeather(cont);
    }
}

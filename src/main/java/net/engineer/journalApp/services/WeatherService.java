package net.engineer.journalApp.services;


import net.engineer.journalApp.Cache.AppCache;
import net.engineer.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String key;
//    public static final String API =
//    "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private AppCache appCache;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService re;
    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = re.get("Weather_Of_" + city, WeatherResponse.class);
        if(weatherResponse!= null){
            return weatherResponse;
        }
        else{
            String finalAPI = appCache.APP_CACHE.get("weather_api").replace("<CITY>",city).replace("<API_KEY>",key);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET,null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body!=null){
                re.set("Weather_Of_" + city,body,300l);
            }
            return body;
        }

    }
}

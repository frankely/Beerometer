package me.frankelydiaz.beerometer.webservice;

import com.bluelinelabs.logansquare.LoganSquare;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import me.frankelydiaz.beerometer.model.Beer;

/**
 * Created by frankelydiaz on 3/19/15.
 */
public class BeerWebService {

    public static List<Beer> getBeers() {
        final String url = "http://ontariobeerapi.ca/beers/";
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        try {

            final String result = restTemplate.getForObject(url, String.class);
            final List<Beer> beers = LoganSquare.parseList(result, Beer.class);

            return beers;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<Beer>();
    }
}

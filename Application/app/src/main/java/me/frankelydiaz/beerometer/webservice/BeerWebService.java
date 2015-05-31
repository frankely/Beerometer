package me.frankelydiaz.beerometer.webservice;

import com.bluelinelabs.logansquare.LoganSquare;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.frankelydiaz.beerometer.model.Beer;

/**
 * Created by frankelydiaz on 3/19/15.
 */
public class BeerWebService {

    public static List<Beer> getBeers(final double minimumAbv) {
        final String url = "http://ontariobeerapi.ca/beers/";
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        try {

            final String result = restTemplate.getForObject(url, String.class);
            final List<Beer> beers = LoganSquare.parseList(result, Beer.class);

            return getValidBeers(beers,minimumAbv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<Beer>();
    }

    private static List<Beer> getValidBeers(final List<Beer> beers, final double minimumAbv) {
        List<Beer> validBeers = new ArrayList<Beer>();


        for (Beer beer : beers) {

            if (beer.imageUrl == null || beer.abv < minimumAbv)
                continue;


            validBeers.add(beer);
        }


        Collections.sort(validBeers, new Comparator<Beer>(){
            @Override
            public int compare(Beer thisBeer, Beer thatBeer) {
                return  Double.compare(thatBeer.abv,thisBeer.abv);
            }
        });


        return validBeers;
    }
}

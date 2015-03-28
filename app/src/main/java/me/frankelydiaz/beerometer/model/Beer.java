package me.frankelydiaz.beerometer.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by frankelydiaz on 3/19/15.
 */
@JsonObject
public class Beer {

    @JsonField
    public String name;

    @JsonField(name = "beer_id")
    public int beerId;

    @JsonField(name = "image_url")
    public String imageUrl;

    @JsonField
    public String category;

    @JsonField
    public double abv;

    @JsonField
    public String type;

    @JsonField
    public String brewer;

    @JsonField
    public String country;

    @JsonField(name = "on_sale")
    public String onSale;
}


/*
* "name": "Keystone Ice",
"beer_id": 131,
"image_url": "http://www.thebeerstore.ca/sites/default/files/styles/brand_hero/public/brand/hero/Key_ICE_355mL.jpg?itok=UDkvF6Rl",
"category": "Discount",
"abv": "5.5",
"type": "Lager",
"brewer": "Molson",
"country": "Canada",
"on_sale": true
*
* */
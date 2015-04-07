package me.frankelydiaz.beerometer.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class Beer$$JsonObjectMapper extends JsonMapper<Beer> {
  @Override
  public Beer parse(JsonParser jsonParser) throws IOException {
    return _parse(jsonParser);
  }

  public static Beer _parse(JsonParser jsonParser) throws IOException {
    Beer instance = new Beer();
    if (jsonParser.getCurrentToken() == null) {
      jsonParser.nextToken();
    }
    if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
      jsonParser.skipChildren();
      return null;
    }
    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
      String fieldName = jsonParser.getCurrentName();
      jsonParser.nextToken();
      parseField(instance, fieldName, jsonParser);
      jsonParser.skipChildren();
    }
    return instance;
  }

  public static void parseField(Beer instance, String fieldName, JsonParser jsonParser) throws IOException {
    if ("abv".equals(fieldName)) {
      instance.abv = jsonParser.getValueAsDouble();
    } else if ("beer_id".equals(fieldName)){
      instance.beerId = jsonParser.getValueAsInt();
    } else if ("brewer".equals(fieldName)){
      instance.brewer = jsonParser.getValueAsString(null);
    } else if ("category".equals(fieldName)){
      instance.category = jsonParser.getValueAsString(null);
    } else if ("country".equals(fieldName)){
      instance.country = jsonParser.getValueAsString(null);
    } else if ("image_url".equals(fieldName)){
      instance.imageUrl = jsonParser.getValueAsString(null);
    } else if ("name".equals(fieldName)){
      instance.name = jsonParser.getValueAsString(null);
    } else if ("on_sale".equals(fieldName)){
      instance.onSale = jsonParser.getValueAsString(null);
    } else if ("type".equals(fieldName)){
      instance.type = jsonParser.getValueAsString(null);
    }
  }

  @Override
  public void serialize(Beer object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
    _serialize(object, jsonGenerator, writeStartAndEnd);
  }

  public static void _serialize(Beer object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
    if (writeStartAndEnd) {
      jsonGenerator.writeStartObject();
    }
    jsonGenerator.writeNumberField("abv", object.abv);
    jsonGenerator.writeNumberField("beer_id", object.beerId);
    jsonGenerator.writeStringField("brewer", object.brewer);
    jsonGenerator.writeStringField("category", object.category);
    jsonGenerator.writeStringField("country", object.country);
    jsonGenerator.writeStringField("image_url", object.imageUrl);
    jsonGenerator.writeStringField("name", object.name);
    jsonGenerator.writeStringField("on_sale", object.onSale);
    jsonGenerator.writeStringField("type", object.type);
    if (writeStartAndEnd) {
      jsonGenerator.writeEndObject();
    }
  }
}

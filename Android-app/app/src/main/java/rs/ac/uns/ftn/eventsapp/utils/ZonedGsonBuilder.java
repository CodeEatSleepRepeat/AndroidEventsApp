package rs.ac.uns.ftn.eventsapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.threeten.bp.ZonedDateTime;

import java.io.IOException;

import retrofit2.converter.gson.GsonConverterFactory;

public class ZonedGsonBuilder {

    public static final GsonConverterFactory getZonedGsonFactory(){
        return GsonConverterFactory.create(ZonedGsonBuilder.getZonedDataTimeGson());
    }

    private static Gson getZonedDataTimeGson() {
        return new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
            @Override
            public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public ZonedDateTime read(JsonReader in) throws IOException {
                return ZonedDateTime.parse(in.nextString());
            }
        }).enableComplexMapKeySerialization().create();
    }
}

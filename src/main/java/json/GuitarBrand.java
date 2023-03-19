package json;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Main {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        GuitarBrand espGuitar = new GuitarBrand(sdf.parse("03-08-1975"),
                "ESP Guitars",
                "Japan",
                Arrays.asList("Metallica",
                        "Children of Bodom",
                        "The Rolling Stones", "Rammstein"));

        String espGuitarJson = new Gson().toJson(espGuitar);
        System.out.println(espGuitarJson);
    }
}
public class GuitarBrand {
    Date dateFounded;
    String name;
    String country;
    List<String> artistsUsedBy;

    public GuitarBrand(Date dateFounded, String name, String country, List<String> artistsUsedBy) {
        this.artistsUsedBy = artistsUsedBy;
        this.country = country;
        this.name = name;
        this.dateFounded = dateFounded;
    }

    // getters, setters, contructor
}

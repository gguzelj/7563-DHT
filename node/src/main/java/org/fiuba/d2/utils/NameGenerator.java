package org.fiuba.d2.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {

    private static List<String> names = new ArrayList<String>() {{
            add("Kathaleen");
            add("Khadijah");
            add("Violeta");
            add("Carmella");
            add("Johnson");
            add("Kandy");
            add("Mari");
            add("Mohammed");
            add("Elmer");
            add("Danille");
            add("Sherley");
            add("Debi");
            add("Ted");
            add("Saran");
            add("Doreatha");
            add("Venita");
            add("Jacquelynn");
            add("Nanette");
            add("Jacquiline");
            add("Fermin");
            add("Lashandra");
            add("Larraine");
            add("Demetra");
            add("Bunny");
            add("Alesha");
            add("Edra");
            add("Eartha");
            add("Delcie");
            add("Cleora");
            add("Hien");
            add("Corliss");
            add("Belia");
            add("Crissy");
            add("Esther");
            add("Catarina");
            add("Earlene");
            add("Cindy");
            add("Maria");
            add("Yer");
            add("Bryan");
            add("Sebrina");
            add("Christiane");
            add("Curt");
            add("Roseann");
            add("Majorie");
            add("Cecily");
            add("Sherri");
            add("Camellia");
            add("Jarrett");
            add("Stephani");
        }};

    public static String generateRandomName() {
        return names.get(new Random().nextInt(names.size()));
    }
}

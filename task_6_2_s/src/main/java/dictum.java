
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class dictum {
    //чтение массива афоризмов из json файла dictum.json
    private static JSONArray readFromFile(String fileName){
        JSONParser parser = new JSONParser();
        JSONArray dictumJA = new JSONArray();
        try {
            FileReader reader = new FileReader(fileName);
            try {
                dictumJA = (JSONArray) parser.parse(reader);
            }
            catch ( ParseException ex){
                System.err.println(ex.toString());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
        catch (IOException e) {
            System.err.println(e.toString());
        }
        return dictumJA;
    }
    //возвращаем один случайный афоризм из массива
    private static JSONObject getRandom (JSONArray dictumJA){
        Random random = new Random(System.currentTimeMillis());
        int rundomNumber = random.nextInt( dictumJA.size() );
        return (JSONObject) dictumJA.get(rundomNumber);
    }
    public static JSONObject getRandomFromFile (String FILENAME){
        JSONArray dictumJA=readFromFile(FILENAME);
        return getRandom(dictumJA);
    }
}

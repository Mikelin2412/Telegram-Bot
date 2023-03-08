package Weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class WeatherProcessing {

    public static String getWeather(String message, WeatherModel weatherModel) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=57477ca70285ed1374a859523dc40e69");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";

        while(in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        weatherModel.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        weatherModel.setTemp(main.getDouble("temp"));
        weatherModel.setHumidity(main.getDouble("humidity"));

        JSONObject wind = object.getJSONObject("wind");
        weatherModel.setWindSpeed(wind.getDouble("speed"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            //weatherModel.setIcon((String) obj.get("icon"));
            weatherModel.setMain((String) obj.get("main"));
        }

        result = "Город: " + weatherModel.getName() + "\n" +
                "Температура: " + weatherModel.getTemp() + " ˚C\n" +
                "Влажность воздуха: " + weatherModel.getHumidity() + "%\n" +
                "Скорость ветра: " + weatherModel.getWindSpeed() + " m/s\n" +
                "Погода сейчас: " + weatherModel.getMain() + "\n";
                //"http://openweathermap.org/img/wn/" + weatherModel.getIcon() + ".png";
        return result;
    }
}

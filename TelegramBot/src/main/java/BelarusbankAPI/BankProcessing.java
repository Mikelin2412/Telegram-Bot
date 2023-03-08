package BelarusbankAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class BankProcessing {
    public static String getBankProcessing(BankModel bankModel) throws IOException {
        URL url = new URL("https://belarusbank.by/api/kursExchange?city=Минск");

        Scanner sc = new Scanner((InputStream) url.getContent());
        String result = "";

        while (sc.hasNext()) {
            result += sc.nextLine();
        }

        JSONArray getArray = new JSONArray(result);
        int index = getArray.length() - 2;
        JSONObject obj = getArray.getJSONObject(index);
        bankModel.setUsd_out(obj.getDouble("USD_out"));
        bankModel.setUsd_in(obj.getDouble("USD_in"));
        bankModel.setEur_out(obj.getDouble("EUR_out"));
        bankModel.setEur_in(obj.getDouble("EUR_in"));
        bankModel.setRub_out(obj.getDouble("RUB_out"));
        bankModel.setRub_in(obj.getDouble("RUB_in"));
        bankModel.setCity_name((String) obj.get("name"));

        result = "Курсы валют в городе " + bankModel.getCity_name() + "\n\n" +
                "Беларусбанк:\n\n" +
                "Покупка:\n" +
                "1 Доллар: " + bankModel.getUsd_in() + " рублей\n" +
                "1 Евро: " + bankModel.getEur_in() + " рублей\n" +
                "100 Российских рублей: " + bankModel.getRub_in() + " рублей\n\n" +
                "Продажа:\n" +
                "1 Доллар: " + bankModel.getUsd_out() + " рублей\n" +
                "1 Евро: " + bankModel.getEur_out() + " рублей\n" +
                "100 Российских рублей: " + bankModel.getRub_out() + " рублей";

        return result;
    }
}

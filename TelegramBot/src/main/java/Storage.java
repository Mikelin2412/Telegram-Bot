import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Storage {
    private ArrayList<String> quoteList;
    Storage() {
        quoteList = new ArrayList<>();
        parser("https://citatnica.ru/citaty/mudrye-tsitaty-velikih-lyudej");
    }

    void parser(String strURL)
    {
        String className = "su-note-inner su-u-clearfix su-u-trim";
        Document doc = null;
        try {
            //Получаем документ нужной нам страницы
            doc = Jsoup.connect(strURL).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Получаем группу объектов, обращаясь методом из Jsoup к определенному блоку
        Elements elQuote = doc.getElementsByClass(className);

        //Достаем текст из каждого объекта поочереди и добавляем в наше хранилище
        elQuote.forEach(el -> {
            quoteList.add(el.text());
        });
    }

    String getRandQuote()
    {
        int randValue = (int)(Math.random() * quoteList.size());
        return quoteList.get(randValue);
    }
}
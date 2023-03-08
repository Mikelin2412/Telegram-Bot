import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class Quiz{
    public static String wantQuiz(int random){
        ArrayList<String> question = new ArrayList<>();
        question.add("Question 1");
        question.add("Question 2");
        question.add("Question 3");
        question.add("Question 4");
        question.add("Question 5");

        String ques = question.get(random);

        return ques;
    }

    public static void keyboard(SendMessage outMess, int i){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        outMess.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();

        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        keyboardRows.add(keyboardRow3);

        switch (i) {
            case 0:
                keyboardRow1.add(new KeyboardButton("Variant 1"));
                keyboardRow2.add(new KeyboardButton("Variant 2"));
                keyboardRow3.add(new KeyboardButton("Variant 3"));
                break;
            case 1:
                keyboardRow1.add(new KeyboardButton("Variant 4"));
                keyboardRow2.add(new KeyboardButton("Variant 5"));
                keyboardRow3.add(new KeyboardButton("Variant 6"));
                break;
            case 2:
                keyboardRow1.add(new KeyboardButton("Variant 7"));
                keyboardRow2.add(new KeyboardButton("Variant 8"));
                keyboardRow3.add(new KeyboardButton("Variant 9"));
                break;
            case 3:
                keyboardRow1.add(new KeyboardButton("Variant 10"));
                keyboardRow2.add(new KeyboardButton("Variant 11"));
                keyboardRow3.add(new KeyboardButton("Variant 12"));
                break;
            case 4:
                keyboardRow1.add(new KeyboardButton("Variant 13"));
                keyboardRow2.add(new KeyboardButton("Variant 14"));
                keyboardRow3.add(new KeyboardButton("Variant 15"));
                break;
        }

        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
}

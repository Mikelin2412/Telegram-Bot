import BelarusbankAPI.BankModel;
import BelarusbankAPI.BankProcessing;
import Weather.WeatherModel;
import Weather.WeatherProcessing;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.springframework.util.ResourceUtils;

import java.io.File;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "your bot token";
    final private String BOT_NAME = "your bot name";
    Storage storage;

    Bot() {
        storage = new Storage();
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage outMess = new SendMessage();
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String response;
                Message inMess = update.getMessage();
                String chatId = inMess.getChatId().toString();
                response = parseMessage(inMess.getText(), update);

                if (inMess.getText().equals("Want quiz")) {
                    outMess.setChatId(chatId);
                    outMess.setText(response);
                    inlineKeyboard(outMess);
                }
                else {
                    outMess.setChatId(chatId);
                    outMess.setText(response);
                    initKeyboard(outMess);
                }
                execute(outMess);
            }
            else if (update.hasMessage() && update.getMessage().hasDocument()) {
                saveDocument(update, outMess);
            }
            else if (update.hasMessage() && update.getMessage().hasPhoto()) {
                savePhoto(update, outMess);
            }
            else if (update.hasCallbackQuery()) {
                 String callbackData = update.getCallbackQuery().getData();
                 long messageId = update.getCallbackQuery().getMessage().getMessageId();
                 long chatId = update.getCallbackQuery().getMessage().getChatId();

                 switch (callbackData) {
                     case "YES_BUTTON":
                         String yesText = "Начинаем викторину!";
                         EditMessageText yesMessage = new EditMessageText();
                         yesMessage.setChatId(String.valueOf(chatId));
                         yesMessage.setText(yesText);
                         yesMessage.setMessageId((int) messageId);

                         try {
                             execute(yesMessage);
                             SendMessage question = new SendMessage();
                             int i = 0;
                             while (i != 5) {
                                 quizGame(chatId, question, i);
                                 i++;
                             }
                         } catch (TelegramApiException e) {
                             e.printStackTrace();
                         }

                         break;
                     case "NO_BUTTON":
                         String noText = "Что ж, вернемся назад!\n\n" + Help.help();
                         EditMessageText noMessage = new EditMessageText();
                         noMessage.setChatId(String.valueOf(chatId));
                         noMessage.setText(noText);
                         noMessage.setMessageId((int) messageId);

                         try {
                             execute(noMessage);
                         } catch (TelegramApiException e) {
                             e.printStackTrace();
                         }

                         break;
                 }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String parseMessage(String textMess, Update update){
        String response = null;

        switch (textMess) {
            case "/start":
                response = "Приветствую тебя, " + update.getMessage().getChat().getFirstName() + "!\n" + Help.help();
                break;
            case "Want a wise quote":
                response = storage.getRandQuote();
                break;
            case "Want a cute picture":
                try {
                    sendPhoto(update, "Path to file");
                } catch (TelegramApiException | FileNotFoundException e) {
                    e.printStackTrace();
                    response = "Картинка не найдена!";
                }
                break;
            case "Want a funny gif":
                try {
                    sendFile(update, "Path to file");
                } catch(TelegramApiException | FileNotFoundException e) {
                    e.printStackTrace();
                    response = "Документ не найден!";
                }
                break;
            case "Want quiz":
                String question = "В нашей викторине есть 5 вопросов. Вы действительно хотите начать?";
                response = question;

                break;
            case "Want weather":
                response = "Введите название города, погоду которого вы хотите узнать";
                break;
            case "Help...":
                response = Help.help();
                break;
            case "Want money":
                try {
                    SendMessage sendMessage = new SendMessage();
                    String wait = "Делаем запрос в банк, пожалуйста подождите (примерно 1 минута)...";
                    sendMessage.setText(wait);
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    execute(sendMessage);
                    BankModel bankModel = new BankModel();
                    response = BankProcessing.getBankProcessing(bankModel);
                } catch (IOException | TelegramApiException e) {
                    response = "Извините, данных по курсам валют не найдено!";
                }
                break;
            default:
                try {
                    WeatherModel weatherModel = new WeatherModel();
                    response = WeatherProcessing.getWeather(textMess, weatherModel);
                } catch (IOException e) {
                    response = "Извините, данных не найдено!";
                }
        }

        return response;
    }

    public void inlineKeyboard(SendMessage outMess) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Yes");
        yesButton.setCallbackData("YES_BUTTON");

        var noButton = new InlineKeyboardButton();

        noButton.setText("No");
        noButton.setCallbackData("NO_BUTTON");

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        outMess.setReplyMarkup(inlineKeyboardMarkup);
    }

    public void initKeyboard(SendMessage outMess) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        outMess.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow2);

        keyboardRow.add(new KeyboardButton("Want a wise quote"));
        keyboardRow.add(new KeyboardButton("Want a cute picture"));
        keyboardRow.add(new KeyboardButton("Want a funny gif"));

        keyboardRow2.add(new KeyboardButton("Want weather"));
        keyboardRow2.add(new KeyboardButton("Want money"));
        keyboardRow2.add(new KeyboardButton("Help..."));

        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    public void saveDocument(Update update, SendMessage sendMessage) {
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Your file is successfully saved!");
        String getID = String.valueOf(update.getMessage().getFrom().getId());
        Document document = new Document();
        document.setFileName(update.getMessage().getDocument().getFileName());
        document.setFileSize(update.getMessage().getDocument().getFileSize());
        document.setFileId(update.getMessage().getDocument().getFileId());
        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            downloadFile(file, new File("Path to file" + getID + "_" + update.getMessage().getDocument().getFileName()));
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void savePhoto (Update update, SendMessage sendMessage) {
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Your photo is successfully saved!");
        List<PhotoSize> photos = update.getMessage().getPhoto();
        String file_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null)).getFileId();
        GetFile getFile = new GetFile();
        getFile.setFileId(file_id);
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            downloadFile(file, new File("Path to file" + file_id));
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void sendPhoto(Update update, String imagePath) throws TelegramApiException, FileNotFoundException {
        SendPhoto sendPhoto = new SendPhoto();
        File file;
        file = ResourceUtils.getFile(String.valueOf(new File(imagePath)));
        sendPhoto.setChatId(update.getMessage().getChatId().toString());
        sendPhoto.setPhoto(new InputFile(file));
        execute(sendPhoto);
    }

    public void sendFile(Update update, String filePath) throws FileNotFoundException, TelegramApiException{
        SendDocument sendDocument = new SendDocument();
        File file;
        file = ResourceUtils.getFile(String.valueOf(new File(filePath)));
        sendDocument.setChatId(update.getMessage().getChatId().toString());
        sendDocument.setDocument(new InputFile(file));
        execute(sendDocument);
    }

    public void quizGame(long chatId, SendMessage question, int i){
        String quest = Quiz.wantQuiz(i);
        Quiz.keyboard(question, i);
        question.setChatId(String.valueOf(chatId));
        question.setText(quest);

        try {
            execute(question);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
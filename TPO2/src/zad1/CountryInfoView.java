package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CountryInfoView extends JFrame{
    private JPanel jPanelNorth;
    private JTextField nbPField, rateInformationField;
    private JTextArea weatherInfoField;
    private String countryName, baseCurrency, exchangeCurrency, weatherJson;
    private Double rate1, rate2;

    public CountryInfoView(String countryName, String baseCurrency, String exchangeCurrency, String weatherJson, Double rate1, Double rate2) throws HeadlessException {
        this.countryName = countryName;
        this.weatherJson = weatherJson;
        this.baseCurrency = baseCurrency;
        this.exchangeCurrency = exchangeCurrency;
        this.rate1 = rate1;
        this.rate2 = rate2;
        components();
        webPage();
        addComponentsToFrame();
        settingsWindow();
    }

    private void components() {
        weatherInfoField = new JTextArea();
        nbPField = new JTextField();
        rateInformationField = new JTextField();
        setTextFiled(rateInformationField, "KURS 1 " + exchangeCurrency + " DO " + " 1 " + baseCurrency + " TO " + rate1 );
        setTextFiled(nbPField, " NBP: KURS 1 " + " PLN " + " DO " + " 1 " + baseCurrency + " TO " + rate2 );
        setTextArea(weatherInfoField,createWeatherJson());
        jPanelNorth = new JPanel(new GridLayout(1, 2));
        jPanelNorth.add(nbPField);
        jPanelNorth.add(rateInformationField);
        setColor();
    }

    private void setTextFiled(JTextField jTextField, String text) {
        jTextField.setEditable(false);
        jTextField.setText(text);
        jTextField.setHorizontalAlignment(JTextField.CENTER);
        jTextField.setSize(new Dimension(100, 80));
    }

    private void setTextArea(JTextArea jTextArea, String text) {
        jTextArea.setEditable(false);
        jTextArea.setText(text);
        jTextArea.setLineWrap(true);
    }

    private void setColor() {
        Color color = new Color(0x5555C6);
        weatherInfoField.setForeground(Color.YELLOW);
        nbPField.setForeground(Color.YELLOW);
        rateInformationField.setForeground(Color.YELLOW);
        weatherInfoField.setBackground(color);
        nbPField.setBackground(color);
        rateInformationField.setBackground(color);
    }

    private void addComponentsToFrame() {
        this.add(new JScrollPane(weatherInfoField), BorderLayout.SOUTH);
        this.add(jPanelNorth, BorderLayout.NORTH);
        this.add(webPage(), BorderLayout.CENTER);
    }

    private JFXPanel webPage() {
        JFXPanel jfxPanel = new JFXPanel();
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.getEngine().load("https://en.wikipedia.org/wiki/" + countryName);
            jfxPanel.setScene(new Scene(webView));
        });
        return jfxPanel;
    }

    private void settingsWindow() {
        //--------------------------------------DANE OKNA----------------------------------------------------
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Country Widget " + countryName);
        this.setVisible(true);
    }
    private String createWeatherJson(){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(weatherJson);
            JSONArray jsonArray = (JSONArray) jsonObject.get("weather");
            Map<String, String> watherMain = (Map<String, String>) (jsonArray.get(0));
            Map<String, Double> mapMain = (Map<String, Double>) jsonObject.get("main");
            Map<String, Double> mapWind = (Map<String, Double>) jsonObject.get("wind");
            return "Pogoda Ogolne Informacje\n\tGlowne Informacje: " + watherMain.get("main") + "\n\tOpis: " + watherMain.get("description")
                    + "\nTemperatura\n\tAktualna: " + mapMain.get("temp") + "\n\tTemperatura MAX: " + mapMain.get("temp_max") + "\n\tTemperatura MIN: " + mapMain.get("temp_min")
                    + "\nCiesnienie: " + mapMain.get("pressure")
                    + "\nWidocznosc: " + jsonObject.get("visibility")
                    + "\nWiatr\n\tPredkosc: " + mapWind.get("speed") + "\n\tKierunek: " + mapWind.get("deg");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Brak Pogody";
    }
}

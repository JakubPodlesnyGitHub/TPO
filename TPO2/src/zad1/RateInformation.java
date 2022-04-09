package zad1;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class RateInformation  implements JsonInterFace{
    private String baseCurrency;
    private String exchangeCurrency;
    private String urlCurrencyString;

    public RateInformation(String exchangeCurrency,String baseCurrency) {
        this.exchangeCurrency = exchangeCurrency;
        this.baseCurrency  = baseCurrency;
        urlCurrencyString = "https://api.exchangerate.host/latest?base=" + baseCurrency +"&symbols=" + exchangeCurrency;
    }

    public double getExchangeInfo() throws IOException, ParseException {
        URL url = new URL(urlCurrencyString);
        String cont = " ";
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine())!= null){
            cont += line;
        }
        return this.getInformationExchange(cont,exchangeCurrency);
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }
}

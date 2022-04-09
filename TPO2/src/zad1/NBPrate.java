package zad1;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NBPrate implements JsonInterFace {
    private String stringNBPUrl;
    private String currencyCode;

    public NBPrate(String baseCurrency) {
        currencyCode = baseCurrency;
        stringNBPUrl = "http://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/?format=json";
    }

    public double getCurrencyNBP() throws IOException, ParseException {
        if (!currencyCode.equals("PLN")) {
            URL url = new URL(stringNBPUrl);
            String cont = " ";
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                cont += line;
            }
            return this.getInformationNBPExchange(cont);
        }else{
            return 1;
        }
    }

}

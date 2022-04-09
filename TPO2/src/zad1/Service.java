/**
 *
 *  @author Podleśny Jakub S20540
 *
 */

package zad1;


import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {
    private String countryName;
    private WeatherInformation weatherInformation;
    private RateInformation rateInformation;
    private NBPrate nbPrate;

    public Service(String countryName) {
        this.countryName = countryName;
        getCurrencyCode();
    }

    public String getWeather(String cityName) {
        weatherInformation = new WeatherInformation(cityName,getCountry());
        try {
            return weatherInformation.getWeatherString();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public double getRateFor(String currency) {
        rateInformation = new RateInformation(currency, getCurrencyCode());
        try {
            return rateInformation.getExchangeInfo();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getNBPRate() {
        nbPrate = new NBPrate(getCurrencyCode());
        try {
            return nbPrate.getCurrencyNBP();
        } catch (IOException | ParseException ioException) {
            ioException.printStackTrace();
        }
        return 0;
    }

    private String getCurrencyCode() {
        Currency currency = Currency.getInstance(new Locale("",getCountry()));
        return currency.getCurrencyCode();
    }

    private String getCountry(){
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            countries.put((new Locale("en", iso).getDisplayCountry(new Locale("en", iso))), iso);
        }
        return countries.get(countryName);
    }

    public String getRateInformationBaseCurrency() {
        return rateInformation.getBaseCurrency();
    }

    public String getRateInformationExchangeCurrency() {
        return rateInformation.getExchangeCurrency();
    }

    public String getCountryName() {
        return countryName;
    }
}  

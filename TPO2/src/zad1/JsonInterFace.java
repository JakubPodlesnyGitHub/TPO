package zad1;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

public interface JsonInterFace {

    default String getInformationWeather(String content) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
        return jsonObject.toJSONString();
    }

    default double getInformationExchange(String content, String exchangeCurrency) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
        Map<String, Double> exchangeRates = (Map<String, Double>) jsonObject.get("rates");
        return exchangeRates.get(exchangeCurrency);
    }

    default double getInformationNBPExchange(String content) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
        JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
        Map<String, Double> exchangeRates = (Map<String, Double>) jsonArray.get(0);
        return exchangeRates.get("mid");
    }

}

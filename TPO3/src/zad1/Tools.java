/**
 * @author Podleśny Jakub S20540
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(new FileInputStream(fileName));
        String host = (String) yamlData.get("host");
        int port = (int) yamlData.get("port");
        boolean concurMode = (boolean) yamlData.get("concurMode");
        boolean showSendRes = (boolean) yamlData.get("showSendRes");
        Map<String, List<String>> clientsMap = (Map<String, List<String>>) yamlData.get("clientsMap");
        Options newOptions = new Options(host, port, concurMode, showSendRes, clientsMap);
        return newOptions;
    }
}

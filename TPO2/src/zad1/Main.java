/**
 *
 *  @author Podleśny Jakub S20540
 *
 */

package zad1;


import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    Service s = new Service((String) JOptionPane.showInputDialog(null,"Podaj panstwo: ","PANSTWO",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src\\zad1\\Image\\earth-icon-png.jpg"),null,""));
    String weatherJson = s.getWeather((String) JOptionPane.showInputDialog(null,"Podaj miasto: ","MIASTO",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src\\zad1\\Image\\town-icon.jpg"),null,""));
    Double rate1 = s.getRateFor((String) JOptionPane.showInputDialog(null,"Podaj walute: ","WALUTA",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src\\zad1\\Image\\currency_Icon.png"),null,""));
    Double rate2 = s.getNBPRate();
    SwingUtilities.invokeLater(() -> {
      CountryInfoView countryInfoView = new CountryInfoView(s.getCountryName(),s.getRateInformationBaseCurrency(),s.getRateInformationExchangeCurrency(),weatherJson,rate1,rate2);
    });
  }
}

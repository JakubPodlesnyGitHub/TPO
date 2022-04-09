package zad1;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    private JTextArea jTextArea;
    private JTextField jTextField;
    private JButton jButtonSend, jButtonLogout;
    private JPanel jPanel;

    public Gui(String userName) {
        createComponents();
        componentsSettings();
        settingsWindow(userName);
        addComponentsToFrame();
    }

    private void createComponents() {
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextField = new JTextField();
        jTextField.setSize(100, 80);
        jButtonSend = new JButton("SEND");
        jButtonLogout = new JButton("LOG OUT");
        jPanel = new JPanel(new GridLayout(1, 2));
        jPanel.add(jTextField);
        jPanel.add(jButtonSend);
    }

    private void componentsSettings() {
        jButtonSend.setBackground(new Color(211, 0, 255));
        jButtonLogout.setBackground(new Color(255, 12, 62));
        jButtonSend.setForeground(new Color(230, 255, 0));
        jTextField.setBackground(new Color(11, 255, 232));
    }

    private void addComponentsToFrame() {
        this.add(new JScrollPane(jTextArea), BorderLayout.CENTER);
        this.add(jPanel, BorderLayout.SOUTH);
        this.add(jButtonLogout, BorderLayout.NORTH);
    }

    private void settingsWindow(String userName) {
        //--------------------------------------DANE OKNA----------------------------------------------------
        this.setSize(600, 400);
        this.setIconImage(new ImageIcon("src\\zad1\\Images\\chat_icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(userName + " WINDOW CHAT");
        this.setVisible(true);
    }

    public JTextArea getjTextArea() {
        return jTextArea;
    }

    public JTextField getjTextField() {
        return jTextField;
    }

    public JButton getjButtonSend() {
        return jButtonSend;
    }

    public JButton getjButtonLogout() {
        return jButtonLogout;
    }
}

package zad1;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;

public class User implements MessageListener {
    private String topicName;
    private String name;
    private Context context;
    private Connection connection;
    private MessageProducer messageProducer;
    private MessageConsumer messageConsumer;
    private TextMessage textMessage;
    private Gui gui;

    public User() {
        this.name = JOptionPane.showInputDialog("Proszę wprowadzić imię");
        this.topicName = JOptionPane.showInputDialog("Proszę wprowadzić temat rozmowy");
        this.gui = new Gui(name);
        addActionListner();
        start();
    }

    private Connection makeConnection() {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        try {
            context = new InitialContext();
            connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void start() {
        try {
            connection = makeConnection();
            Destination destination = (Destination) context.lookup(topicName);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            createProducerConsumer(session, destination);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    private void createProducerConsumer(Session session, Destination destination) throws JMSException {
        messageProducer = session.createProducer(destination);
        messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(this);
        textMessage = session.createTextMessage();
    }

    private void addActionListner() {
        gui.getjButtonSend().addActionListener(e -> {
            try {
                textMessage.setText("Użytkownik " + name + " przesłał/a: " +gui.getjTextField().getText());
                messageProducer.send(textMessage);
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        });
        gui.getjButtonLogout().addActionListener(e -> {
            try {
                textMessage.setText("Użytkownik " + name + " wylogował się");
                messageProducer.send(textMessage);
                connection.close();
                JOptionPane.showMessageDialog(null, "Uzytkownik się wylogował", "Wylogowanie", JOptionPane.WARNING_MESSAGE, new ImageIcon("src\\zad1\\Images\\conLost.png"));
                System.exit(0);
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        });
    }

    @Override
    public void onMessage(Message message) {
        try {
            gui.getjTextArea().append(((TextMessage) message).getText() + "\n");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

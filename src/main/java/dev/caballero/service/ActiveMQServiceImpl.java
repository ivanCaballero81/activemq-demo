package dev.caballero.service;

import dev.caballero.dto.MetaDataDto;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActiveMQServiceImpl implements ActiveMQService {

  private static final Logger LOGGER = Logger.getLogger(
    ActiveMQServiceImpl.class.getName()
  );
  protected String propertiesFile = "config.properties";
  protected Properties activeMqProps = new Properties();

  int messageSent = 0;

  public ActiveMQServiceImpl() {
    try {
      InputStream input =
        this.getClass().getClassLoader().getResourceAsStream(propertiesFile);
      activeMqProps.load(input);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error load Properties File {}", propertiesFile);
    }
  }

  @Override
  public void sendMessage(MetaDataDto metaData, byte[] message) {
    String queueName = activeMqProps.getProperty("queueName");
    Connection connection = null;
    Session session = null;
    messageSent = 0;

    try {
      connection = createConnection();
      // Create Session
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue(queueName);
      // Create a MessageProducer from Session to the Topic or Queue
      MessageProducer producer = session.createProducer(destination);

      // create a Message
      BytesMessage bytesMessage = session.createBytesMessage();
      bytesMessage.writeBytes(message);

      producer.send(bytesMessage);

      //Clean Up
      session.close();
      connection.close();

      messageSent = 1;
    } catch (JMSException e) {
      messageSent = 2;
      LOGGER.severe("Error JMSException");
    } finally {
      try {
        if (session != null) session.close();
        if (connection != null) connection.close();
      } catch (JMSException e) {
        LOGGER.severe("Error Finally");
      }
    }
  }

  @Override
  public Boolean isEmpty(String queueName) {
    return false;
  }

  @Override
  public void consumeMessage(String queueName) {
    Connection connection = null;
    Session session = null;
    try {
      // Create a Connection
      connection = createConnection();
      connection.start();

      // Create a Session
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue(queueName);

      // Create a MessageConsumer from the Session to the Topic or Queue
      MessageConsumer consumer = session.createConsumer(destination);

      // Wait for a message
      Message message = consumer.receive(1000);
      if (message instanceof TextMessage) {
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        LOGGER.log(Level.INFO, "Received: {}", text);
      } else {
        LOGGER.log(Level.INFO, "Message: {}", message);
      }

      consumer.close();
      session.close();
      connection.close();
    } catch (JMSException e) {
      LOGGER.severe("Error JMSException");
    }
  }

  private Connection createConnection() throws JMSException {
    String brokerUrl = activeMqProps.getProperty("brokerUrl");
    String userName = activeMqProps.getProperty("jmsUser");
    String userPassword = activeMqProps.getProperty("jmsPassword");
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
      userName,
      userPassword,
      brokerUrl
    );
    connectionFactory.setTrustedPackages(
      Arrays.asList("dev.caballero.service")
    );
    return connectionFactory.createConnection();
  }
}

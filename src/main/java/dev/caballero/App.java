package dev.caballero;

import dev.caballero.dto.MetaDataDto;
import dev.caballero.service.ActiveMQService;
import dev.caballero.service.ActiveMQServiceImpl;

import java.nio.charset.StandardCharsets;

public class App {

  public static void main(String[] args) {
    ActiveMQService service = new ActiveMQServiceImpl();
    MetaDataDto metaData = new MetaDataDto("id-32", "type", "dsdsdsds");

    byte[] message = "TEST MEsage".getBytes(StandardCharsets.UTF_8);

    service.sendMessage(metaData, message);
  }
}

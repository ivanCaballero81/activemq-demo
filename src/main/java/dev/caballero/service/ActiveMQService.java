package dev.caballero.service;

import dev.caballero.dto.MetaDataDto;

public interface ActiveMQService {
  void sendMessage(final MetaDataDto metaData, final byte[] message);

  Boolean isEmpty(final String queueName);

  void consumeMessage(final String queueName);
}

package dev.caballero.dto;

public class MetaDataDto {

  private String messsageId;
  private String jmsType;
  private String queueName;
  private int version;

  public MetaDataDto(String messsageId, String jmsType, String queueName) {
    this.messsageId = messsageId;
    this.jmsType = jmsType;
    this.queueName = queueName;
    this.version = 1;
  }

  public String getMesssageId() {
    return messsageId;
  }

  public void setMesssageId(String messsageId) {
    this.messsageId = messsageId;
  }

  public String getJmsType() {
    return jmsType;
  }

  public void setJmsType(String jmsType) {
    this.jmsType = jmsType;
  }

  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}

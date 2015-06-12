package org.g6.laas.core.field;

public class TextField extends AbstractField<String> {

  public String getValue() {
    return this.getContent();
  }

  public TextField(String content){
    super(content);
  }
}

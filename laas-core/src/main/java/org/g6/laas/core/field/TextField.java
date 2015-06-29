package org.g6.laas.core.field;

public class TextField extends AbstractField<String> {

  public String getValue() {
    return this.getContent();
  }

  public TextField(String content){
    super(content);
  }

  public int compareTo(Field o){
      return this.getValue().compareTo((String)o.getValue());
  }
}

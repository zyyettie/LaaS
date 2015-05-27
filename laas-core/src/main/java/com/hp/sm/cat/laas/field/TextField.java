package com.hp.sm.cat.laas.field;

public class TextField extends AbstractField<String> {
  public String getValue() {
    return this.getContent();
  }
}

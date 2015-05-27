package com.hp.sm.cat.laas.field;

public class IntegerField extends AbstractField<Integer>  {

  public Integer getValue() {
    return Integer.valueOf(this.getContent());
  }
}

package org.g6.laas.core.field;

public class IntegerField extends AbstractField<Integer>  {

  public Integer getValue() {
    return Integer.valueOf(this.getContent());
  }

  public IntegerField(String content){
    super(content);
  }
}

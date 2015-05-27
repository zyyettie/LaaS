package com.hp.sm.cat.laas.field;

import lombok.Data;

@Data
public abstract class AbstractField<T> implements Field<T> {
  private String content;
  private String name;
}

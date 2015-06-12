package org.g6.laas.core.field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractField<T> implements Field<T> {
  private String content;
  private String name;

  public AbstractField(String content){
    this.content = content;
  }
}

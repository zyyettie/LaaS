package com.hp.sm.cat.laas.log;

import com.hp.sm.cat.laas.field.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Collection;

/*
this class existing for consideration sometimes we need read multiple lines that is as slice
including [start, end) content
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Slice {

  private File file;
  private String content;
  private int start;
  private int end;

  public abstract Collection<Field> split();

}

package org.g6.laas.core.log;

import org.g6.laas.core.field.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.file.ILogFile;

import java.util.Collection;

/*
this class existing for consideration sometimes we need read multiple lines that is as slice
including [start, end) content
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Slice {

  private ILogFile file;
  private String content;
  private int start;
  private int end;

  public abstract Collection<Field> split();

}

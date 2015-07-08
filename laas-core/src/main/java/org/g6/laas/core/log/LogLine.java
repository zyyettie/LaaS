package org.g6.laas.core.log;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;

@NoArgsConstructor
@Data
public class LogLine extends Line {

  private Field sortedField = null;

  public LogLine(ILogFile file, String content, int lineNumber) {
    super(file, content, lineNumber, null);
  }

  public LogLine(ILogFile file, String content, int lineNumber, InputFormat lineFormat) {
    super(file, content, lineNumber, lineFormat);
  }

  /**
   * The specified InputFormat can not cover all the ones. so the split method can be overwritten
   * and no matter whether the InputFormat is needed or not, because the code will run on the line
   * content directly.
   *
   * @return
   */
  @Override
  public SplitResult split() {
    InputFormat inputFormat = getInputFormat();
    return (inputFormat == null ? null : inputFormat.splitLine(this));
  }

  @Override
  public int compareTo(Object o) {
    return sortedField.compareTo(((LogLine) o).sortedField);
  }

}

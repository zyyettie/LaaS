package org.g6.laas.core.format.provider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;

import java.io.File;

@Slf4j
@Data
public final class DefaultFormatProvider extends FileFormatProvider {

  public DefaultFormatProvider(File jsonFile) {
    super(jsonFile);
  }

  public DefaultFormatProvider(String jsonFile) {
    super(jsonFile);
  }

  @Override
  protected InputFormat parse() {
    DefaultInputFormat result = new DefaultInputFormat(getFile());
    result.build();
    return result;
  }
}

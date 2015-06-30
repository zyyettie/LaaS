package org.g6.laas.core.rule;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.log.Line;

@Data
@NoArgsConstructor
@Slf4j
public class KeywordRule extends AbstractRule {
  private String keyword;

  public KeywordRule(String keyword) {
    this.keyword = keyword;
  }

  public String getKeyword() {
    return this.keyword;
  }

  public boolean isSatisfied(Object content) {
    if (content == null)
      return false;
    else if ("".equals(getKeyword()) || getKeyword() == null)
      return false;
    else {
      return content.toString().contains(getKeyword());
    }
  }
}

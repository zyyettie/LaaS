package org.g6.laas.core.rule;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class KeywordRule extends AbstractRule {
  private String keyword;
  private Object matched;

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
      matched = content;
      return content.toString().contains(getKeyword());
    }
  }
}

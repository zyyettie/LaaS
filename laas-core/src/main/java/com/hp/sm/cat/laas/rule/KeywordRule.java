package com.hp.sm.cat.laas.rule;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: Pitt Zhou
 * Date: Jun/11/2015
 * Time: 1:50 PM
 * Keyword Rule must be satisfied only if the content contains the given keyword
 */
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
        else if (getKeyword()==null || getKeyword()=="")
            return false;
        else
            return content.toString().indexOf(getKeyword()) != -1;
    }
}

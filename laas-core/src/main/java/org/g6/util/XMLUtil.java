package org.g6.util;


import com.google.common.io.Resources;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;

public class XMLUtil {
    public static <T> T parse(String rule, String xmlFile) {
        return parse(new String[]{rule}, xmlFile);
    }

    public static <T> T parse(String[] rules, String xmlFile) {
        CaaSXMLRuleModule module = new CaaSXMLRuleModule(rules);

        DigesterLoader loader = newLoader(module);
        try {
            return loader.newDigester().parse(Resources.getResource(xmlFile));
        } catch (Exception e) {
            String ruleFiles = "";
            for (String rule : rules) {
                ruleFiles += rule + ",";
            }
            throw new LaaSCoreRuntimeException("Errors happen while parsing XML file: "
                    + xmlFile + " with rule files:" + ruleFiles, e);
        }
    }

    @Data
    @NoArgsConstructor
    private static class CaaSXMLRuleModule extends FromXmlRulesModule {
        String[] xmlFiles;

        CaaSXMLRuleModule(String[] xmlFiles) {
            this.xmlFiles = xmlFiles;
        }

        @Override
        protected void loadRules() {
            for (String xml : xmlFiles) {
                loadXMLRules(Resources.getResource(xml));
            }
        }
    }

}

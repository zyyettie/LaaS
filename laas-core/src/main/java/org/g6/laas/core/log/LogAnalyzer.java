package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.KeywordRule;

public class LogAnalyzer {
    public void analyse(){
        String fileName = "C:\\aa.txt";
        ILogFile file = new LogFile(fileName);
        //For Rule part, need to add to here
        //call LogHandler to handle log file
        Rule rule = new KeywordRule("RTE D");
        LogHandler handle = new ConcreteLogHandler(file, rule);
        try {
            handle.handle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.analyse();
    }
}

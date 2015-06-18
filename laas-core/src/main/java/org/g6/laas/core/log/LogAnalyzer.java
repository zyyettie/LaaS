package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;

public class LogAnalyzer {
    public void analyse(){
        String fileName = "aa.txt";
        ILogFile file = new LogFile(fileName);
        //For Rule part, need to add to here
        //call LogHandler to handle log file
    }
}

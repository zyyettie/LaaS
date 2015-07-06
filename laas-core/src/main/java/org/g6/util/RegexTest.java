package org.g6.util;

import java.util.regex.*;

public class RegexTest {
     public static String[] samples = {
             " 7076(  6028) 05/11/2015 16:49:52  RTE D (0x09E8A060)      DBACCESS - Group        against file Todo in 9.843000 seconds [rc=0 ]",
             " 4536( 12784) 06/15/2015 04:43:30  RTE D SCRIPTTRACE: ProcessDesignerEnablement.isPDEnabled entered, line 107",
             " 4536( 12784) 06/15/2015 04:43:30  RTE D SCRIPTTRACE: ProcessDesignerEnablement.isPDEnabled exited, elapsed: 0 ms",
             " 7076(  6028) 05/11/2015 16:49:45  RTE D DBQUERY^F^Todo(sqlserver I)^0^0.000000^P^262^2.921000^\"(itemType=\"incidents\" and status~=\"Resolved\")\"^{\"itemType\"}^0.000000^0.000000 ( [ 0] display fdisp.1 )",
             " 7076(  6028) 05/11/2015 16:49:32  RTE D DBFIND^F^operator(sqlserver I)^1^0.000000^ ^1^0.078000^\"name=\"WRZJRO\"\"^ ^0.000000^0.000000 ( [ 0] login call.user.login )"
     };

    public static String[] regexes={
            "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D.+DBACCESS.+(\\d+\\.\\d+)\\s+seconds.*",
            "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D SCRIPTTRACE:.+elapsed: (\\d+) ms",
            "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D DBQUERY(?:\\^[^\\^]+){6}\\^(\\d+\\.\\d+).*",
            "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D DBFIND(?:\\^[^\\^]+){6}\\^(\\d+\\.\\d+).*"
    };

    public static void main(String[] args) {
        for(String regex:regexes) {
            System.out.println("regex: "+regex);
            Pattern p = Pattern.compile(regex);
            for(String sample:samples) {
                Matcher m = p.matcher(sample);
                boolean b = m.matches();
                System.out.println(b);
                if (b) {
                    int count = m.groupCount();
                    for (int i=0; i<=count; i++) {
                        System.out.println(""+i+":"+m.group(i));
                    }
                }
            }
        }
    }
}

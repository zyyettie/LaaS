package org.g6.util;

import java.util.List;

public class JSONUtil {

    public static void main(String[] args) {

        List<String> list = FileUtil.readFile(FileUtil.getRelativeInputStream("/sm_rte_log.json"));
        for(String str: list){
            System.out.print(str);
        }
    }
}

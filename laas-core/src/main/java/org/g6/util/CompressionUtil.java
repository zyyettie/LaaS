package org.g6.util;

import org.zeroturnaround.zip.ZipUtil;

import java.io.File;

public class CompressionUtil {

    public static void compress(String inputFilePath, String zipFile) {
        ZipUtil.pack(new File(inputFilePath), new File(zipFile));
    }


}

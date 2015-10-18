package org.g6.util;

import com.google.common.io.Resources;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.exception.LaaSExceptionHandler;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class FileUtil {

    public static Map<String, String> getPropertyValues(String file) {
        Properties p = new Properties();
        Map<String, String> propMap = new HashMap();
        try {
            p.load(new FileInputStream(getFile(file)));
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                propMap.put(key, value);
            }
            return propMap;
        } catch (IOException e) {
            throw new LaaSCoreRuntimeException(file + " is not found.");
        }
    }

    public static String getvalue(String key, String file) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(getFile(file)));
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                if (key.equals(entry.getKey())) {
                    return (String) entry.getValue();
                }
            }
        } catch (IOException e) {
            throw new LaaSCoreRuntimeException(file + " is not found.");
        }
        return null;
    }

    /**
     * @param file the format of this parameter should be package/file e.g. org/g6/laas/sm/sm_rte_log.json
     * @return
     * @throws URISyntaxException
     */
    public static File getFile(String file) {
        URL url = Resources.getResource(file);
        File result = null;
        try {
            result = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new LaaSCoreRuntimeException(file + " is not found.");
        }
        return result;
    }

    /**
     * Assume the file is not big, and also each line is not needed to analyze while reading
     *
     * @param is
     * @return
     */
    public static List<String> readFile(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            List<String> list = new ArrayList<>();
            String line;
            while (null != (line = br.readLine()))
                list.add(line);

            return list;
        } catch (IOException e) {
            throw new LaaSCoreRuntimeException("Exception is thrown when reading file", e);
        }

    }

    public static boolean writeFile(String str, String outFile) {
        List<String> list = new ArrayList();
        list.add(str);
        return writeFile(list, outFile);
    }

    public static boolean writeFile(List<String> lineList, String outFile) {
        try (FileWriter writer = new FileWriter(outFile);
             BufferedWriter bw = new BufferedWriter(writer);) {
            for (String str : lineList) {
                bw.write(str);
                bw.newLine();
            }
        } catch (IOException e) {
            LaaSExceptionHandler.handleException("Error happens when create the file : " + outFile, e);
        }

        return true;
    }

    /**
     * Create a file from the input stream
     *
     * @param stream
     * @param outFile
     */
    public static boolean createFile(InputStream stream, String outFile) {
        byte[] buffer = new byte[4 * 1024];

        try (FileOutputStream os = new FileOutputStream(outFile); InputStream is = stream;) {
            int read;
            while ((read = is.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        } catch (Exception e) {
            LaaSExceptionHandler.handleException("Error happens when create the file : " + outFile, e);
        }
        return true;
    }

    /**
     * Copy a file
     *
     * @param file
     * @param outFile
     */
    public static boolean createFile(String file, String outFile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(file));
        } catch (FileNotFoundException e) {
            LaaSExceptionHandler.handleException("Error happens when getting the input stream of " + file, e);
        }
        return createFile(fis, outFile);
    }

    /**
     * Create a new folder
     *
     * @param dir
     */
    public static boolean createDir(String dir) {
        File f = new File(dir);
        return f.mkdirs();
    }

    /**
     * delete a folder including all child folders
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(String dir) {
        return deleteDir(new File(dir));
    }

    public static boolean deleteDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    /**
     * Delete a file
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(String file) {
        File f = new File(file);
        if (f.exists() && f.isFile()) {
            return f.delete();
        }
        return true;
    }

    /**
     * Get a file list under a directory
     *
     * @param dir
     * @return
     */
    public static List<String> getFileList(String dir) {
        List<String> fileList = new ArrayList<String>();
        File _dir = new File(dir);
        File[] files = _dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    String fileName = files[i].getAbsolutePath();
                    fileList.add(fileName);
                }
            }
        }
        return fileList;
    }

    /**
     * Get file name list under a directory
     *
     * @param dir
     * @return
     */
    public static List<String> getFileNameList(String dir) {
        List<String> fileNameList = new ArrayList<String>();
        File _dir = new File(dir);

        File[] files = _dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    String fileName = files[i].getName();
                    fileNameList.add(fileName);
                }
            }
        }
        return fileNameList;
    }

    /**
     * Get directory name list of a directory
     *
     * @param dir
     * @return
     */
    public static List<String> getDirNameList(String dir) {
        List<String> dirNameList = new ArrayList<String>();
        File _dir = new File(dir);

        File[] files = _dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    String dirName = files[i].getName();
                    dirNameList.add(dirName);
                }
            }
        }
        return dirNameList;
    }

    /**
     * Check if it is a valid file
     *
     * @param file
     * @return
     */
    public static boolean isFile(String file) {
        File f = new File(file);
        return f.exists() && f.isFile();
    }

    /**
     * Check if it is valid directory
     *
     * @param file
     * @return
     */
    public static boolean isDir(String file) {
        File f = new File(file);
        return f.exists() && f.isDirectory();
    }

    /**
     * Get file name
     *
     * @param file
     * @return
     */
    public static String getFileName(String file) {
        File f = new File(file);
        return f.getName();
    }
}

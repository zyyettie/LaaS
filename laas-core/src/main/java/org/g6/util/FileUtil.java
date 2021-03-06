package org.g6.util;

import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.exception.LaaSExceptionHandler;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;

public class FileUtil {
    public final static String separator = File.separator;

    public static Map<String, String> getPropertyValues(String file) {
        Properties p = new Properties();
        Map<String, String> propMap = new HashMap();
        try {
            p.load(FileUtil.class.getResourceAsStream(file));
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

    public static String getValue(String key, String file) {
        Properties p = new Properties();
        try {
            p.load(FileUtil.class.getResourceAsStream(file));
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
     * Get the InputStream of a file
     * @param file  if the file starts with /, the JVM will look for it from the root of class path.
     *              otherwise, starts looking from the package where the class is.
     *              Normally always recommend starting with /
     * @return
     */
    public static InputStream getInputStreamOfFileInClassPath(String file){
         return FileUtil.class.getResourceAsStream(file);
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

    public static String readFullFile(File file) {
        StringBuffer sb = new StringBuffer();

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);) {


            String content;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    public static List<String> readFirstNLines(File file, long num) {
        List<String> lines = new ArrayList();

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);) {


            String content;
            int count = 0;
            while ((content = reader.readLine()) != null) {
                if (count >= num)
                    break;
                lines.add(content);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return lines;
    }

    public static List<String> readLastNLines(File file, long num) {
        List<String> result = new ArrayList<>();
        long count = 0;

        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return result;
        }

        try (RandomAccessFile fileRead = new RandomAccessFile(file, "r");) {
            long length = fileRead.length();
            if (length == 0L) {
                return result;
            } else {
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    fileRead.seek(pos);
                    if (fileRead.readByte() == '\n') {
                        String line = fileRead.readLine();
                        result.add(line);
                        count++;
                        if (count == num) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    fileRead.seek(0);
                    result.add(fileRead.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

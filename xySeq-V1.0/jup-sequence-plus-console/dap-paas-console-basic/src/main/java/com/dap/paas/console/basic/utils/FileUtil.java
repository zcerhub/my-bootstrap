package com.dap.paas.console.basic.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 判断url是否是http资源
     *
     * @param url url
     * @return 是否http
     */
    public static boolean isHttpUrl(URL url) {
        return url.getProtocol().toLowerCase().startsWith("file") || url.getProtocol().toLowerCase().startsWith("http");
    }

    /**
     * 上传文件
     *
     * @param file
     * @param path 输出路径
     * @return
     */
    public static boolean uploadFile(MultipartFile file, String path) {
        try(InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(path)) {
            StreamUtils.copy(in, out);
            log.info("文件上传完成");
            return true;
        }
        catch (IOException e) {
            log.error("文件上传失败", e.getMessage());
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFileByName(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("删除文件" + fileName + "成功！");
                return true;
            } else {
                log.info("删除文件" + fileName + "失败！");
                return false;
            }
        } else {
            log.info("删除文件不存在：" + fileName);
            return false;
        }
    }

    /**
     * 通过文件名获取文件后缀
     *
     * @param fileName 文件名称
     * @return 文件后缀
     */
    public static String suffixFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 根据文件路径删除文件
     *
     * @param filePath 绝对路径
     */
    public static boolean deleteFileByPath(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            return false;
        }
        return true;
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            log.info("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtil.deleteFileByName(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else if (files[i].isDirectory()) {
                // 删除子目录
                flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!dirFile.delete() || !flag) {
            log.info("删除目录失败！");
            return false;
        }
        return true;
    }

    /**
     * 文件下载
     *
     * @param filePath 绝对路径
     */
    public static void downloadFile(String filePath, HttpServletRequest request, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment;filename=" + EncodingUtil.convertToFileName(request, file.getName()));
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        }
        catch (IOException e) {
            log.error("文件下载出错", e);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件。<br/>
     *
     * @param response HttpServletResponse
     * @param fileName 文件名称
     * @param is       InputStream
     * @param charset  字符集编码
     */
    public static void downFile(HttpServletResponse response, String fileName, InputStream is, String charset) {
        OutputStream fos = null;
        InputStream fis = is;
        try {
            response.setContentType("text/plain");
            fileName = URLEncoder.encode(fileName, charset);
            response.setCharacterEncoding(charset);
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            fos = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.constant.FileConsts;
import com.dap.paas.console.basic.entity.FilePackage;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.Organization;
import com.dap.paas.console.basic.enums.FileType;
import com.dap.paas.console.basic.service.FilePackageService;
import com.dap.paas.console.basic.utils.FileUtil;
import com.dap.paas.console.common.constants.CharsetConsts;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static com.dap.paas.console.basic.enums.FileType.*;

/**
 * 文件上传下载
 *
 */
@Api(tags = "基础信息-文件管理")
@RestController
@RequestMapping("/v1/basic/file")
public class FileController {
    private static final Logger log= LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FilePackageService filePackageService;

    @ApiOperation("添加包信息")
    @PostMapping("/add")
    public Result add(@RequestBody FilePackage filePackage){
        String version = filePackage.getVersion();
        final List<FilePackage> filePackages = filePackageService.queryList(filePackage);
        for(FilePackage fp:filePackages){
            if(fp.getVersion().equals(version)){
                return ResultUtils.error(ResultEnum.FAILED.getCode(),"版本号重复");
            }
        }
        filePackageService.saveObject(filePackage);

        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 查询指定tableName下的所有包信息
     * @param tableName
     * @return
     */
    @ApiOperation("查询当前tableName下的所有包信息")
    @GetMapping("/queryList")
    public Result getFileListByTableName(@RequestParam("tableName")String tableName){
        try{
            Map map = new HashMap();
            map.put("tableName",tableName);
            List<FilePackage> filePackages = filePackageService.queryList(map);
            return ResultUtils.success(ResultEnum.SUCCESS, filePackages);
        } catch (ServiceException e) {
            log.error("分页版本号失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("分页查询当前tableName下的所有包信息")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<FilePackage> param) {
        try {
            Page filePackages = filePackageService.queryPage(param.getPageNo(),param.getPageSize(), param.getRequestObject());
            return ResultUtils.success(ResultEnum.SUCCESS, filePackages);
        } catch (ServiceException e) {
            log.error("分页查询失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    @ApiOperation("上传包文件")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
                         @RequestParam ("id") String id,
                         @RequestParam ("tableName")String tableName,
                         @RequestParam ("packageType") String packageType) {

        // 获取文件名
        String fileName = file.getOriginalFilename();
        fileName = HtmlUtils.htmlEscape(fileName, CharsetConsts.DEFAULT_ENCODING);
        int unixSep = fileName.lastIndexOf('/');
        int winSep = fileName.lastIndexOf('\\');
        int pos = (Math.max(winSep, unixSep));
        if (pos != -1)  {
            fileName = fileName.substring(pos + 1);
        }

        String fileDir = getFileDir(fileName, null,tableName,packageType);
        String packageName = "package";
        String sdk = "sdk";
        // 判断是否存在同名文件
        if (existsFile(fileDir, fileName)) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "存在同名文件，请先删除原有文件再次上传");
        }
        File outFile = new File(fileDir + File.separator);

        if (!outFile.exists() && !outFile.mkdirs()) {
            log.error("创建文件夹[{}]失败，请检查目录权限！",fileDir);
        }

        log.info("开始上传文件：{}", fileDir + File.separator + fileName);

        if(FileUtil.uploadFile(file, fileDir + File.separator + fileName)) {
            FilePackage filePackage = filePackageService.getObjectById(id);
            if(packageType.equals(packageName)){
                filePackage.setPackageUrl(fileDir);
                filePackage.setPackageName(fileName);
            }
            if(packageType.equals(sdk)){
                filePackage.setSdkUrl(fileDir);
                filePackage.setSdkName(fileName);
            }

            filePackageService.updateObject(filePackage);
            return ResultUtils.success(ResultEnum.SUCCESS);
        }else {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "文件上传失败");
        }
    }

    @ApiOperation("删除当前所选信息")
    @DeleteMapping("/deleteFile")
    public Result deleteFile(@RequestBody HashMap<String, String> map) {
        String id = map.get("id");
        FilePackage filePackage = filePackageService.getObjectById(id);
        String tableName = filePackage.getTableName();
        if(StringUtil.isNotEmpty(filePackage.getPackageName())){
           String filePackageName = filePackage.getPackageName();
           String fileDirPackage = getFileDir(filePackageName, null,tableName,"package");
           try{
               if (!FileUtil.deleteFileByPath(fileDirPackage + File.separator + filePackageName)) {
                   log.error("删除文件【{}】失败，请检查目录权限！", filePackageName);
               }
           }
           catch (Exception e) {
               log.error("文件删除失败", e.getMessage());
           return ResultUtils.error(ResultEnum.FAILED.getCode(), e.getMessage());
           }
        }
        if(StringUtil.isNotEmpty(filePackage.getSdkName())){
            String fileSDKName = filePackage.getSdkName();
            String fileDirSDK = getFileDir(fileSDKName,  null,tableName,"sdk");
            try{
                if (!FileUtil.deleteFileByPath(fileDirSDK + File.separator + fileSDKName)) {
                    log.error("删除文件【{}】失败，请检查目录权限！", fileSDKName);
                }
            }
            catch (Exception e) {
                log.error("文件删除失败", e.getMessage());
                return ResultUtils.error(ResultEnum.FAILED.getCode(), e.getMessage());
            }
        }
        filePackageService.delObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    @ApiOperation("下载包")
    @GetMapping("/download")
    public void download(@RequestParam("id") String id,
                         @RequestParam("fileName") String fileName,
                         @RequestParam("tableName") String tableName,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        String packageType = null;
        FilePackage filePackage = filePackageService.getObjectById(id);
        if(StringUtils.isNotEmpty(filePackage.getPackageName()) && filePackage.getPackageName().equals(fileName)){
            packageType = "package";
        }
        if(StringUtils.isNotEmpty(filePackage.getSdkName()) && filePackage.getSdkName().equals(fileName)){
            packageType = "sdk";
        }
        String fileDir = getFileDir(fileName, null,tableName,packageType);
        FileUtil.downloadFile(fileDir + File.separator + fileName, request, response);
    }

    @GetMapping("/listFiles")
    public Result getFiles(FileType fileType) {
        List<String> list = new ArrayList<>();
        String fileDir = getFileDir(null, fileType,null,null);
        File file = new File(fileDir + File.separator);
        if (file.exists()) {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(f -> list.add(f.getName()));
        }

        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }

    @ApiOperation("查询当前tableName下的所有包信息")
    @GetMapping("/queryVersionList")
    public Result getVersionListByTableName(@RequestParam("tableName")String tableName){
        try{
            Map map = new HashMap();
            map.put("tableName",tableName);
            List<FilePackage> filePackages = filePackageService.queryList(map);
            List<String> versions = new ArrayList<>();
            for(FilePackage fp:filePackages){
                versions.add(fp.getVersion());
            }
            return ResultUtils.success(ResultEnum.SUCCESS, versions);
        } catch (ServiceException e) {
            log.error("分页版本号失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    private boolean existsFile(String fileDir, String fileName) {
        File file = new File(fileDir + fileName);
        return file.exists();
    }


    private String getFileDir(String fileName, FileType fileType,String tableName,String packageType) {
        String redis = "redis";
        if(StringUtil.isEmpty(tableName)){
            return null;
        }

        if(tableName.equals(redis) && StringUtil.isNotEmpty(fileName) && fileName.contains("ARM")){
            tableName = "/" + packageType + "/" + tableName + "/ARM";
        }else if(StringUtil.isNotEmpty(fileName) && tableName.equals(redis)){
            tableName = "/" + packageType + "/" + tableName + "/X86";
        }else{
            tableName = "/" + packageType + "/" + tableName;
        }
        String fileDir = FileConsts.FILE_DIR + tableName;
        if(fileType == null) {
            fileType = typeFromFileName(fileName);
        }
        switch (fileType) {
            case OFFICE:
            case PDF:
                return fileDir + FileConsts.DOC_DIR;
            case JAR:
                return fileDir + FileConsts.SDK_DIR;
            default:
                return fileDir;
        }
    }
}

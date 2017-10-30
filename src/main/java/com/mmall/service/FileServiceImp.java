package com.mmall.service;


import com.google.common.collect.Lists;
import com.mmall.utils.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("fileServiceImp")
public class FileServiceImp implements IFileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImp.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString() + fileExtensionName;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.error("上传文件异常", e);
            return null;
        }
        FTPUtil.uploadFile(Lists.newArrayList(targetFile));
        targetFile.delete();
        return targetFile.getName();
    }

}

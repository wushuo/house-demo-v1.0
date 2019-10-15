package com.mooc.house.biz.service;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    /**
     * 保存图像
     * @param files
     * @return
     */
    public List<String> getImagePath(List<MultipartFile> files) {
        List<String> paths = Lists.newArrayList();
        files.forEach(file ->{
            File localFile = null;
            if(!file.isEmpty()) {
                try {
                    localFile = saveToLocalFile(file, filePath);
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
                    paths.add(path);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        return paths;
    }

    /**
     * 保存文件到本地目录
     * @param file
     * @param filePath
     * @return
     */
    private File saveToLocalFile(MultipartFile file, String filePath) throws IOException {
        File newFile = new File(filePath+"/"+ Instant.now().getEpochSecond()+"/"+file.getOriginalFilename());
        if(!newFile.exists()) newFile.getParentFile().mkdirs();
        newFile.createNewFile();
        Files.write(file.getBytes(), newFile);
        return newFile;
    }
}

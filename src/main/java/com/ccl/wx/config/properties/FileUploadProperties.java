package com.ccl.wx.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 褚超亮
 * @date 2020/4/3 22:06
 */
@Data
@Component
public class FileUploadProperties {

    @Value("${file.upload.suffix.path}")
    private String suffixPath;

    @Value("${file.upload.local.path}")
    private String localPath;

    @Value("${file.upload.useless.local.path}")
    private String uselessLocalPath;
}

package com.ccl.wx.service.impl;

import com.ccl.wx.service.CircleFileService;
import com.ccl.wx.util.FtpUtil;

import java.util.ArrayList;

/**
 * @author 褚超亮
 * @date 2020/2/3 13:12
 */
public class CircleFileServiceImpl implements CircleFileService {

    @Override
    public void delCircleFileImage(ArrayList<String> images) {
        for (String image : images) {
            FtpUtil.delFile(image);
        }
    }

}

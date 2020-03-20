package com.ccl.wx.service;

import java.util.ArrayList;

/**
 * @author 褚超亮
 * @date 2020/2/3 13:11
 */
public interface CircleFileService {

    /**
     * 便利图片路径删除图片
     * @param images 图片列表
     * @return
     */
    void delCircleFileImage(ArrayList<String> images);
}

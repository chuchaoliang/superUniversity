package com.ccl.wx.service;

import com.ccl.wx.dto.CircleIntroDTO;
import com.ccl.wx.entity.CircleIntro;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 褚超亮
 * @date 2020/4/16 10:19
 */

public interface CircleIntroService {


    int deleteByPrimaryKey(Integer id);

    int insert(CircleIntro record);

    int insertSelective(CircleIntro record);

    CircleIntro selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleIntro record);

    int updateByPrimaryKey(CircleIntro record);

    /**
     * 保存圈子简介音频
     *
     * @param circleId 圈子id
     * @param file     文件
     * @param userId   用户id
     * @return
     */
    String saveCircleIntroVoice(Long circleId, MultipartFile file, String userId);

    /**
     * 保存圈子简介信息
     *
     * @param circleIntro 圈子信息
     * @return
     */
    String saveCircleIntroContent(CircleIntro circleIntro);

    /**
     * 保存圈子简介图片信息
     *
     * @param circleId 圈子id
     * @param file     文件
     * @param userId
     * @return
     */
    String saveCircleIntroImage(Long circleId, MultipartFile file, String userId);

    /**
     * 保存圈子简介音频
     *
     * @param circleId 圈子id
     * @param file     文件
     * @param userId   用户id
     * @return
     */
    String saveCircleMasterIntroVoice(Long circleId, MultipartFile file, String userId);

    /**
     * 保存圈子简介图片
     *
     * @param circleId 圈子id
     * @param file     要保存的文件
     * @param userId   用户id
     * @return
     */
    String saveCircleMasterIntroImage(Long circleId, MultipartFile file, String userId);

    /**
     * 更新圈子简介信息
     *
     * @param circleIntroDTO
     * @return
     */
    String updateCircleIntro(CircleIntroDTO circleIntroDTO);

    /**
     * 更新圈主简介信息
     *
     * @param circleIntroDTO
     * @return
     */
    String updateCircleMasterIntro(CircleIntroDTO circleIntroDTO);

    /**
     * 获取圈子简介信息
     *
     * @param circleId 圈子id
     * @return
     */
    String getCircleIntro(Integer circleId);
}






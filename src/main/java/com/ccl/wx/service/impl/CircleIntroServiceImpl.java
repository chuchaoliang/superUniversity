package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.CircleIntroDTO;
import com.ccl.wx.entity.CircleIntro;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.mapper.CircleIntroMapper;
import com.ccl.wx.service.CircleIntroService;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/16 10:19
 */

@Service
public class CircleIntroServiceImpl implements CircleIntroService {

    @Resource
    private CircleIntroMapper circleIntroMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return circleIntroMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CircleIntro record) {
        return circleIntroMapper.insert(record);
    }

    @Override
    public int insertSelective(CircleIntro record) {
        return circleIntroMapper.insertSelective(record);
    }

    @Override
    public CircleIntro selectByPrimaryKey(Integer id) {
        return circleIntroMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(CircleIntro record) {
        return circleIntroMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CircleIntro record) {
        return circleIntroMapper.updateByPrimaryKey(record);
    }

    @Override
    public String saveCircleIntroVoice(Long circleId, MultipartFile file, String userId) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleId.intValue());
        String fail = EnumResultStatus.FAIL.getValue();
        if (circleIntro == null) {
            return fail;
        }
        String result = FtpUtil.uploadFile(userId, file);
        if (fail.equals(result)) {
            return fail;
        }
        circleIntro.setCircleVoice(result);
        int i = circleIntroMapper.updateByPrimaryKeySelective(circleIntro);
        if (i == 0) {
            return fail;
        }
        return JSON.toJSONString(result);
    }


    @Override
    public String saveCircleIntroContent(CircleIntro circleIntro) {
        CircleIntro circleIntroInfo = circleIntroMapper.selectByPrimaryKey(circleIntro.getCircleId());
        int i;
        if (circleIntroInfo == null) {
            // 插入数据
            i = circleIntroMapper.insertSelective(circleIntro);
        } else {
            // 更新数据
            i = circleIntroMapper.updateByPrimaryKeySelective(circleIntro);
        }
        if (i == 0) {
            return EnumResultStatus.FAIL.getValue();
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String saveCircleIntroImage(Long circleId, MultipartFile file, String userId) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleId.intValue());
        String fail = EnumResultStatus.FAIL.getValue();
        if (circleIntro == null) {
            return fail;
        }
        // 拼接图片字符串
        boolean flag = false;
        if ("".equals(circleIntro.getCircleImage())) {
            flag = true;
        }
        // 上传图片
        String result = FtpUtil.uploadFile(userId, file);
        if (fail.equals(result)) {
            return fail;
        }
        int i = circleIntroMapper.concatCircleImage(result, circleId, flag);
        if (i == 0) {
            return fail;
        }
        return JSON.toJSONString(result);
    }

    @Override
    public String saveCircleMasterIntroVoice(Long circleId, MultipartFile file, String userId) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleId.intValue());
        String fail = EnumResultStatus.FAIL.getValue();
        if (circleIntro == null) {
            return fail;
        }
        String result = FtpUtil.uploadFile(userId, file);
        if (fail.equals(result)) {
            return fail;
        }
        circleIntro.setUserVoice(result);
        int i = circleIntroMapper.updateByPrimaryKeySelective(circleIntro);
        if (i == 0) {
            return fail;
        }
        return JSON.toJSONString(result);
    }

    @Override
    public String saveCircleMasterIntroImage(Long circleId, MultipartFile file, String userId) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleId.intValue());
        String fail = EnumResultStatus.FAIL.getValue();
        if (circleIntro == null) {
            return fail;
        }
        // 拼接图片字符串
        boolean flag = false;
        if ("".equals(circleIntro.getUserImage())) {
            flag = true;
        }
        // 上传图片
        String result = FtpUtil.uploadFile(userId, file);
        if (fail.equals(result)) {
            return fail;
        }
        int i = circleIntroMapper.concatUserImage(result, circleId, flag);
        if (i == 0) {
            return fail;
        }
        return JSON.toJSONString(result);
    }

    @Override
    public String updateCircleIntro(CircleIntroDTO circleIntroDTO) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleIntroDTO.getCircleId());
        if (circleIntro == null) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            // 处理图片
            List<String> historyImages = new ArrayList<>();
            String circleImage = circleIntro.getCircleImage();
            if (!StringUtils.isEmpty(circleImage)) {
                historyImages = Arrays.asList(circleImage.split(","));
            }
            circleIntro.setCircleImage(CclUtil.fileListDispose(circleIntroDTO.getCircleImages(), historyImages));
            // 处理音频
            circleIntro.setCircleVoice(CclUtil.fileDispose(circleIntroDTO.getCircleVoice(), circleIntro.getCircleVoice()));
            // 处理文本内容
            circleIntro.setCircleIntro(circleIntroDTO.getCircleIntro());
            // 更新
            int i = circleIntroMapper.updateByPrimaryKeySelective(circleIntro);
            if (i == 0) {
                return EnumResultStatus.FAIL.getValue();
            }
            return JSON.toJSONStringWithDateFormat(circleIntro, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }

    @Override
    public String updateCircleMasterIntro(CircleIntroDTO circleIntroDTO) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleIntroDTO.getCircleId());
        if (circleIntro == null) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            // 处理图片
            List<String> historyImages = new ArrayList<>();
            String circleImage = circleIntro.getUserImage();
            if (!StringUtils.isEmpty(circleImage)) {
                historyImages = Arrays.asList(circleImage.split(","));
            }
            circleIntro.setUserImage(CclUtil.fileListDispose(circleIntroDTO.getCircleImages(), historyImages));
            // 处理音频
            circleIntro.setUserVoice(CclUtil.fileDispose(circleIntroDTO.getCircleVoice(), circleIntro.getUserVoice()));
            // 处理文本内容
            circleIntro.setUserIntro(circleIntroDTO.getUserIntro());
            // 更新
            int i = circleIntroMapper.updateByPrimaryKeySelective(circleIntro);
            if (i == 0) {
                return EnumResultStatus.FAIL.getValue();
            }
            return JSON.toJSONStringWithDateFormat(circleIntro, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }

    @Override
    public String getCircleIntro(Integer circleId) {
        CircleIntro circleIntro = circleIntroMapper.selectByPrimaryKey(circleId);
        if (circleIntro == null) {
            return EnumResultStatus.FAIL.getValue();
        }
        return JSON.toJSONStringWithDateFormat(circleIntro, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }
}






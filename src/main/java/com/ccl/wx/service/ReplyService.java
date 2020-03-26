package com.ccl.wx.service;

import com.ccl.wx.entity.Reply;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

public interface ReplyService {


    int deleteByPrimaryKey(Long id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    List<Long> selectIdByDiaryId(Long diaryId);
}

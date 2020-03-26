package com.ccl.wx.service.impl;

import com.ccl.wx.entity.Reply;
import com.ccl.wx.mapper.ReplyMapper;
import com.ccl.wx.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author  褚超亮
 * @date  2020/3/25 20:05
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class ReplyServiceImpl implements ReplyService{

    @Resource
    private ReplyMapper replyMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return replyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Reply record) {
        return replyMapper.insert(record);
    }

    @Override
    public int insertSelective(Reply record) {
        return replyMapper.insertSelective(record);
    }

    @Override
    public Reply selectByPrimaryKey(Long id) {
        return replyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Reply record) {
        return replyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Reply record) {
        return replyMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Long> selectIdByDiaryId(Long diaryId) {
        return replyMapper.selectIdByDiaryId(diaryId);
    }
}

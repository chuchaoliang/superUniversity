package com.ccl.wx.service;

import com.ccl.wx.entity.SystemNotify;
import com.ccl.wx.vo.notify.SystemNotifyVO;

/**
 * @author 褚超亮
 * @date 2020/6/3 22:28
 */

public interface SystemNotifyService {


    int deleteByPrimaryKey(Integer id);

    int insert(SystemNotify record);

    int insertSelective(SystemNotify record);

    SystemNotify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemNotify record);

    int updateByPrimaryKey(SystemNotify record);

    /**
     * 发送消息提醒
     *
     * @param systemNotifyVO 消息提醒具体信息
     * @param type
     * @return
     */
    String sendMessageNotify(SystemNotifyVO systemNotifyVO, Integer type);
}

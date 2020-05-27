package com.ccl.wx.service.impl;

import com.ccl.wx.entity.NotifyConfig;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.enums.notify.EnumNotifyOperationType;
import com.ccl.wx.mapper.NotifyConfigMapper;
import com.ccl.wx.service.NotifyConfigService;
import com.ccl.wx.util.CclUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/5/24 15:58
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class NotifyConfigServiceImpl implements NotifyConfigService {

    @Resource
    private NotifyConfigMapper notifyConfigMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return notifyConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(NotifyConfig record) {
        return notifyConfigMapper.insert(record);
    }

    @Override
    public int insertSelective(NotifyConfig record) {
        return notifyConfigMapper.insertSelective(record);
    }

    @Override
    public NotifyConfig selectByPrimaryKey(String id) {
        return notifyConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(NotifyConfig record) {
        return notifyConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(NotifyConfig record) {
        return notifyConfigMapper.updateByPrimaryKey(record);
    }

    @Override
    public boolean judgeMessageRemind(Integer type, String userId) {
        // 用户设置对象为空
        if (!judgeUserNotifyIsNull(userId)) {
            NotifyConfig userNotifyConfig = selectByPrimaryKey(userId);
            if (StringUtils.isEmpty(userNotifyConfig.getNotifyConfig())) {
                return true;
            } else {
                List<String> configs = Arrays.asList(userNotifyConfig.getNotifyConfig().split(","));
                if (configs.contains(String.valueOf(type)) || configs.contains(String.valueOf(-type))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean judgeMessagePersistence(Integer type, String userId) {
        // 用户设置对象为空
        if (!judgeUserNotifyIsNull(userId)) {
            NotifyConfig userNotifyConfig = selectByPrimaryKey(userId);
            if (StringUtils.isEmpty(userNotifyConfig.getNotifyConfig())) {
                return true;
            } else {
                List<String> configs = Arrays.asList(userNotifyConfig.getNotifyConfig().split(","));
                if (configs.contains(String.valueOf(-type))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getMessageOperationType(Integer type, String userId) {
        if (judgeUserNotifyIsNull(userId)) {
            return EnumNotifyOperationType.NORMAL.getValue();
        } else {
            NotifyConfig userNotifyConfig = selectByPrimaryKey(userId);
            List<String> configs = Arrays.asList(userNotifyConfig.getNotifyConfig().split(","));
            if (configs.contains(String.valueOf(type))) {
                return EnumNotifyOperationType.NO_REMIND.getValue();
            } else if (configs.contains(String.valueOf(-type))) {
                return EnumNotifyOperationType.NO_PERSISTENCE.getValue();
            } else {
                return EnumNotifyOperationType.NORMAL.getValue();
            }
        }
    }

    /**
     * 判断用户配置是否为空
     *
     * @param userId 用户id
     * @return 为空true 不为空false
     */
    public boolean judgeUserNotifyIsNull(String userId) {
        NotifyConfig userNotifyConfig = selectByPrimaryKey(userId);
        if (userNotifyConfig == null) {
            NotifyConfig notifyConfig = new NotifyConfig();
            notifyConfig.setId(userId);
            insertSelective(notifyConfig);
            return true;
        }
        return false;
    }

    @Override
    public String addUserNotifyConfig(List<Integer> configList, String userId) {
        // 这里的意义是由于没有限制消息列表的内容，不应该超过这么大吧？？
        if (configList.size() > 20) {
            return EnumResultStatus.FAIL.getValue();
        }
        boolean notifyIsNull = judgeUserNotifyIsNull(userId);
        NotifyConfig userNotifyConfig = selectByPrimaryKey(userId);
        if (!notifyIsNull) {
            // 获取用户设置
            if (!StringUtils.isEmpty(userNotifyConfig.getNotifyConfig())) {
                List<String> configs = new ArrayList<>(Arrays.asList(userNotifyConfig.getNotifyConfig().split(",")));
                // 删除用户配置
                configs.removeIf(config -> configList.contains(Integer.parseInt(config)) || configList.contains(-Integer.parseInt(config)));
                userNotifyConfig.setNotifyConfig(CclUtil.listToString(configs, ','));
                int i = updateByPrimaryKeySelective(userNotifyConfig);
                return CclUtil.judgeOperationIsSuccess(i);
            }
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    // TODO 待重构懒得重构 坑！！！这里做了很多实际中不可能出现的情况，虽然代码冗余但是处理了很多情况！
    @Override
    public String delUserNotifyConfig(List<Integer> configList, String userId, Integer type) {
        // 这里的意义是由于没有限制消息列表的内容，不应该超过这么大吧？？
        if (configList.size() > 20) {
            return EnumResultStatus.FAIL.getValue();
        }
        boolean notifyIsNull = judgeUserNotifyIsNull(userId);
        NotifyConfig userNotifyConfig = selectByPrimaryKey(userId);
        // 用户配置对象为空
        if (notifyIsNull) {
            // 判断用户是否消息提醒，或者不持久到数据库
            if (type.equals(EnumNotifyOperationType.NO_REMIND.getValue())) {
                // 直接插入数据
                userNotifyConfig.setNotifyConfig(CclUtil.listToString(configList, ','));
                int i = updateByPrimaryKeySelective(userNotifyConfig);
                return CclUtil.judgeOperationIsSuccess(i);
            } else if (type.equals(EnumNotifyOperationType.NO_PERSISTENCE.getValue())) {
                ArrayList<Integer> newConfigList = new ArrayList<>();
                configList.forEach(config -> newConfigList.add(-config));
                userNotifyConfig.setNotifyConfig(CclUtil.listToString(newConfigList, ','));
                int i = updateByPrimaryKeySelective(userNotifyConfig);
                return CclUtil.judgeOperationIsSuccess(i);
            } else {
                // 错误情况
                return EnumResultStatus.FAIL.getValue();
            }
        } else {
            // 用户配置对象不为空
            // 获取配置列表
            if (!StringUtils.isEmpty(userNotifyConfig.getNotifyConfig())) {
                List<String> configs = new ArrayList<>(Arrays.asList(userNotifyConfig.getNotifyConfig().split(",")));
                if (type.equals(EnumNotifyOperationType.NO_REMIND.getValue())) {
                    // 用户消息不提醒但是持久化
                    configList.forEach(config -> {
                        if (!configs.contains(String.valueOf(config)) && !configs.contains(String.valueOf(-config))) {
                            // 此配置不存在在用户的设置中
                            configs.add(String.valueOf(config));
                        } else {
                            // 此配置已经存在在设置中
                            if (configs.contains(String.valueOf(-config))) {
                                configs.remove(String.valueOf(-config));
                                configs.add(String.valueOf(config));
                            }
                        }
                    });
                    userNotifyConfig.setNotifyConfig(CclUtil.listToString(configs, ','));
                    int i = updateByPrimaryKeySelective(userNotifyConfig);
                    return CclUtil.judgeOperationIsSuccess(i);
                } else if (type.equals(EnumNotifyOperationType.NO_PERSISTENCE.getValue())) {
                    // 用户消息不持久化当然也就不提醒
                    configList.forEach(config -> {
                        if (!configs.contains(String.valueOf(config)) && !configs.contains(String.valueOf(-config))) {
                            // 此配置不存在在用户的设置中
                            configs.add(String.valueOf(-config));
                        } else {
                            // 此配置已经存在在设置中
                            if (configs.contains(String.valueOf(config))) {
                                configs.remove(String.valueOf(config));
                                configs.add(String.valueOf(-config));
                            }
                        }
                    });
                    userNotifyConfig.setNotifyConfig(CclUtil.listToString(configs, ','));
                    int i = updateByPrimaryKeySelective(userNotifyConfig);
                    return CclUtil.judgeOperationIsSuccess(i);
                } else {
                    return EnumResultStatus.FAIL.getValue();
                }
            } else {
                // 用户配置为空直接插入数据
                if (type.equals(EnumNotifyOperationType.NO_REMIND.getValue())) {
                    userNotifyConfig.setNotifyConfig(CclUtil.listToString(configList, ','));
                    int i = updateByPrimaryKeySelective(userNotifyConfig);
                    return CclUtil.judgeOperationIsSuccess(i);
                } else if (type.equals(EnumNotifyOperationType.NO_PERSISTENCE.getValue())) {
                    // 用户设置不持久化
                    configList.forEach(config -> {
                        configList.remove(config);
                        configList.add(-config);
                    });
                    userNotifyConfig.setNotifyConfig(CclUtil.listToString(configList, ','));
                    int i = updateByPrimaryKeySelective(userNotifyConfig);
                    return CclUtil.judgeOperationIsSuccess(i);
                } else {
                    return EnumResultStatus.FAIL.getValue();
                }
            }
        }
    }
}

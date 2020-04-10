package com.ccl.wx.service;

import com.ccl.wx.entity.CircleInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/6 14:22
 */

public interface CircleInfoService {


    int deleteByPrimaryKey(Long circleId);

    int insert(CircleInfo record);

    int insertSelective(CircleInfo record);

    CircleInfo selectByPrimaryKey(Long circleId);

    int updateByPrimaryKeySelective(CircleInfo record);

    int updateByPrimaryKey(CircleInfo record);

    /**
     * 根据圈子id 更新圈子主题总数
     *
     * @param circleId 圈子id
     * @param value    需要+ - 的值 + 1 - 1
     * @return
     */
    int updateThemeNumberByCircleId(Long circleId, Integer value);

    /**
     * 获取圈子内的主页面加载的全部内容
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return 圈子全部内容json字符串
     */
    String getCircleIndexAllContent(String userId, Integer circleId);

    /**
     * 根据关键词模糊查询数据
     *
     * @param likeCircleName 圈子查询关键词
     * @return
     */
    List<CircleInfo> selectAllLikeAndCircleName(String likeCircleName);

    /**
     * 挑选圈子的所有名称
     *
     * @return
     */
    List<String> selectAllCircleName();

    /**
     * 根据圈子类型查询圈子数据
     *
     * @param tid
     * @return
     */
    List<CircleInfo> findAllByCircleLocation(int tid);

    /**
     * 根据圈子类型和关键字查询圈子数据
     *
     * @param keyword 关键字
     * @param tid     圈子类型
     * @return
     */
    List<CircleInfo> selectAllByCircleNameLikeAndCircleLocation(String keyword, int tid);

    /**
     * 创建圈子
     *
     * @param circleInfo 圈子信息
     * @param image      圈子的头像
     * @return
     */
    String fondCircle(CircleInfo circleInfo, MultipartFile image);

    /**
     * 检测圈子名称是否重复
     * true 不重复 false 重复
     *
     * @param circleName 圈子名称
     * @return
     */
    boolean checkCircleName(String circleName);

    /**
     * 更新圈子人数
     *
     * @param circleId 圈子id
     * @param value    要增加的值人
     * @return
     */
    Integer updateCircleMemberByCircleId(Long circleId, int value);

    /**
     * 圈子装饰方法
     *
     * @param circles 需要装饰的圈子信息
     * @param userId  用户id
     * @param number  数据总数
     * @param page    当前是第几页
     * @return
     */
    String selectAdornCircle(List<CircleInfo> circles, String userId, Integer number, Integer page);

    /**
     * 根据圈子类型查找圈子
     *
     * @param type   圈子类型
     * @param userId 用户id
     * @param page   当前页
     * @return
     */
    String selectCircleByType(Integer type, String userId, Integer page);

    /**
     * 判断用户是否可以进入私密圈子
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return
     */
    String judgeUserIntoPrivacyCircle(String userId, Long circleId);

    /**
     * 根据条件查询圈子
     *
     * @param circleInfo 条件
     * @return
     */
    List<CircleInfo> selectByAll(CircleInfo circleInfo);
}




package com.ccl.wx.service;

import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.vo.CircleNoticeVO;
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
     * 获取圈子内的主页面加载的全部内容
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return 圈子全部内容json字符串
     */
    String getCircleIndexAllContent(String userId, Integer circleId);

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

    /**
     * 判断用户是否为圈主
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return true(是) false(不是)
     */
    Boolean judgeUserIsCircleMaster(String userId, Long circleId);

    /**
     * 更新圈子中的数据
     *
     * @param circleInfo 圈子信息
     * @param circleId   圈子id
     * @param value      要添加的值
     * @return
     */
    int updateCircleData(CircleInfo circleInfo, Long circleId, Integer value);

    /**
     * 根据圈子类型和关键词查询数据
     *
     * @param keyword 关键词
     * @param type    圈子类型
     * @param userId  用户id
     * @param page    页数
     * @return
     */
    String searchCircleByTypeKeyWord(String keyword, Integer type, String userId, Integer page);

    /**
     * 查询圈子
     *
     * @param keyword 关键词
     * @param type    类型（位置）
     * @return
     */
    List<CircleInfo> selectSearchCircleInfo(String keyword, Integer type);

    /**
     * 根据关键词搜索圈子数据
     *
     * @param keyword 圈子关键词
     * @param userId  用户id
     * @param page    页数
     * @return
     */
    String searchCircleByKeyWord(String keyword, String userId, Integer page);

    /**
     * 保存圈子公告
     *
     * @param circleNoticeVO 圈子通知对象
     * @param userId         用户id
     * @return
     */
    String saveCircleNotice(CircleNoticeVO circleNoticeVO, String userId);

    /**
     * 删除圈子公告信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String deleteCircleNotice(Long circleId, String userId);

    /**
     * 更新圈子公告信息
     *
     * @param circleNoticeVO 圈子id
     * @param userId   用户id
     * @return
     */
    String updateCircleNotice(CircleNoticeVO circleNoticeVO, String userId);

    /**
     * 检测圈子公告是否为空，不为空返回公告信息
     *
     * @param circleId 圈子id
     * @return
     */
    String checkCircleNotice(Long circleId);
}





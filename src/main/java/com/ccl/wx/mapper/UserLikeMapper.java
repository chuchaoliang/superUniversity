package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/1/28 10:18
 */

public interface UserLikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLike record);

    int insertSelective(UserLike record);

    UserLike selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLike record);

    int updateByPrimaryKey(UserLike record);

    /**
     * 根据点赞id 查询点赞数据
     *
     * @param typeId 点赞id
     * @return
     */
    UserLike selectByTypeId(@Param("typeId") Long typeId);

    /**
     * 拼接点赞用户id
     *
     * @param diaryId
     * @param userId
     * @return
     */
    int concatLikeUserId(@Param("diaryId") Long diaryId, @Param("userId") String userId);

    /**
     * 根据id信息查询点赞全部列表
     *
     * @param typeId（日志id或者评论、点评id）
     * @return
     */
    List<Long> selectIdByTypeId(@Param("typeId") Long typeId);
}
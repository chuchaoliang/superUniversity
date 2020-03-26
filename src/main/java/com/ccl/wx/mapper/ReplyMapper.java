package com.ccl.wx.mapper;

import com.ccl.wx.entity.Reply;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

@Mapper
public interface ReplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    /**
     * 根据评论id查找回复
     *
     * @param commentId 评论的id
     * @return
     */
    List<Reply> selectAllByCommentId(@Param("commentId") Long commentId, @Param("start") Integer start, @Param("num") Integer num);

    /**
     * 根据圈子id查询全部回复数据
     *
     * @param circleId
     * @return
     */
    List<Reply> selectAllByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据日志信息查询全部回复id列表
     *
     * @param diaryId
     * @return
     */
    List<Long> selectIdByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 根据日记id查询回复的条数
     *
     * @param diaryId
     * @return
     */
    Long countByDiaryId(@Param("diaryId") Long diaryId);
}
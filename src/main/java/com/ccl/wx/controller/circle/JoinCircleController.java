package com.ccl.wx.controller.circle;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.enums.common.EnumPage;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.ResponseMsgUtil;
import com.ccl.wx.vo.CircleNoticeVO;
import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/3/16 22:04
 */
@Api(tags = {"JoinCircleController【用户加入圈子相关】"})
@RestController
@RequestMapping("/circle")
public class JoinCircleController {

    @Resource
    private JoinCircleService joinCircleService;

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private CircleInfoService circleInfoService;

    /**
     * 获取用户积分排行榜
     *
     * @param circleId 圈子id
     * @param page     当前页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/all/vitality")
    public Result<String> getUserVitalityRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                                 @RequestParam(value = "page", required = false) Integer page) {
        String result = joinCircleService.getUserVitalityRankingData(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取用户连续打卡排行榜
     *
     * @param circleId 圈子id
     * @param page     当前页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/all/signin")
    public Result<String> getUserSignInRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestParam(value = "page", required = false) Integer page) {
        String result = joinCircleService.getUserSignInRankingData(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取某位用在圈子中的活跃度排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/person/vitality")
    public Result<String> getPersonUserVitalityRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                                       @RequestHeader(value = "token", required = false) String userId) {
        List<Map> userVitalityRankingInfo = joinCircleService.getUserVitalityRankingInfo(circleId, userId, 0, EnumPage.LAST_NUMBER.getValue());
        return ResponseMsgUtil.success(JSON.toJSONString(userVitalityRankingInfo));
    }

    /**
     * 获取某位用在圈子中的连续打卡排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/rank/person/signin")
    public Result<String> getUserSignInRanking(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestHeader(value = "token", required = false) String userId) {
        List<Map> userSignInRankingInfo = joinCircleService.getUserSignInRankingInfo(circleId, userId, 0, EnumPage.LAST_NUMBER.getValue());
        return ResponseMsgUtil.success(JSON.toJSONString(userSignInRankingInfo));
    }

    /**
     * 根据日期获取用户打卡统计信息(已打卡)
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param date     时间(格式年月日)
     * @param page     第几页
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/statistics/all/success")
    public Result<String> getUserSignStatisticsSuccessInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                           @RequestHeader(value = "token", required = false) String userId,
                                                           @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                           @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = joinCircleService.getUserSignStatisticsSuccessInfo(circleId, userId, date, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 根据日期获取用户打卡统计信息（未打卡）
     * 参数含义同上
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param date     时间
     * @param page     第几页
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/statistics/all/fail")
    public Result<String> getUserSignStatisticsFailInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                        @RequestHeader(value = "token", required = false) String userId,
                                                        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = joinCircleService.getUserSignStatisticsFailInfo(circleId, userId, date, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 根据日期获取打卡信息 已打卡人数，未打卡人数
     *
     * @param circleId 圈子id
     * @param date     日期 yyyyMMdd
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/statistics/number")
    public Result<String> getSignStatisticsNumberInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                                      @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        String result = joinCircleService.getCircleSignInInfo(circleId, date);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取圈子正常用户信息
     *
     * @param circleId 圈子id
     * @param page     页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/manager/normal")
    public Result<String> getCircleNormalUser(@RequestParam(value = "circleId", required = false) Long circleId,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = joinCircleService.getCircleNormalUser(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 淘汰圈子中用户
     *
     * @param circleId  圈子id
     * @param outUserId 被淘汰用户人id
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/manager/normal/out")
    public Result<String> outCircleUser(@RequestParam(value = "circleId", required = false) Long circleId,
                                        @RequestParam(value = "userId", required = false) String outUserId,
                                        @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.outCircleUser(circleId, outUserId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        } else if (EnumResultStatus.UNKNOWN.getValue().equals(result)) {
            // 无权限操作
            return ResponseMsgUtil.fail(EnumResultCode.UNAUTHORIZED);
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 获取圈子中被拒绝的成员信息
     *
     * @param circleId 圈子id
     * @param page     页数
     * @return
     */
    @GetMapping("/menu/manager/refuse")
    public Result<String> getCircleRefuseUser(@RequestParam(value = "circleId", required = false) Long circleId,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = joinCircleService.getCircleRefuseUser(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 挑选圈子待审核成员
     *
     * @param circleId 圈子id
     * @param page     页数
     * @return
     */
    @GetMapping("/menu/manager/audit")
    public Result<String> getCircleAuditUser(@RequestParam(value = "circleId", required = false) Long circleId,
                                             @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = joinCircleService.getCircleAuditUser(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 同意用户加入圈子
     *
     * @param circleId    圈子id
     * @param applyUserId 申请用户id
     * @return
     */
    @GetMapping("/menu/manager/audit/agree")
    public Result<String> agreeJoinCircle(@RequestParam(value = "circleId", required = false) Long circleId,
                                          @RequestParam(value = "userId", required = false) String applyUserId) {
        String result = joinCircleService.agreeJoinApply(applyUserId, circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 拒绝用户加入圈子
     *
     * @param circleId     圈子id
     * @param applyUserId  申请用户id
     * @param refuseReason 拒绝理由
     * @return
     */
    @GetMapping("/menu/manager/audit/refuse")
    public Result<String> refuseJoinCircle(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId,
                                           @ParamCheck @RequestParam(value = "userId", required = false) String applyUserId,
                                           @RequestParam(value = "reason", required = false) String refuseReason) {
        String result = joinCircleService.refuseJoinCircle(circleId, applyUserId, refuseReason);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 添加圈子昵称
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/nickname/add")
    public Result<String> saveCircleNickname(@RequestParam(value = "circleId", required = false) Long circleId,
                                             @RequestHeader(value = "token", required = false) String userId,
                                             @RequestParam(value = "nickname", required = false) String nickname) {
        String result = joinCircleService.saveCircleNickname(userId, circleId, nickname);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 设置用户加入圈子昵称为默认值
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/nickname/default")
    public Result<String> updateCircleNicknameDefault(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId,
                                                      @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.updateCircleNicknameDefault(userId, circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("用户未加入圈子，或者状态异常！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 检测圈子昵称是否存在
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/nickname/check")
    public Result<String> checkUserCircleNickname(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId,
                                                  @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.checkUserCircleNickname(circleId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("用户未加入圈子，或者状态异常！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 更新圈子昵称
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param nickname 圈子昵称
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/nickname/update")
    public Result<String> updateUserCircleNickname(@RequestParam(value = "circleId", required = false) Long circleId,
                                                   @RequestHeader(value = "token", required = false) String userId,
                                                   @RequestParam(value = "nickname", required = false) String nickname) {
        String result = joinCircleService.updateUserCircleNickname(circleId, userId, nickname);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("用户未加入圈子，或者状态异常！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 添加管理员
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/admin/add")
    public Result<String> addCircleAdmin(@RequestParam(value = "circleId", required = false) Long circleId,
                                         @RequestParam(value = "userId", required = false) String userId) {
        String result = joinCircleService.addCircleAdmin(circleId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 检测是否可以添加管理员
     *
     * @param circleId 圈子id
     * @return
     */
    @GetMapping("/menu/admin/add/check")
    public Result<String> checkAddCircleAdmin(@RequestParam(value = "circleId", required = false) Long circleId) {
        String result = joinCircleService.checkAddCircleAdmin(circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("管理员人数已满！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 获取圈子中管理员
     *
     * @param circleId 圈子id
     * @return
     */
    @GetMapping("/menu/admin/get/admin")
    public Result<String> getCircleAdminInfo(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId) {
        String result = joinCircleService.getCircleAdminInfo(circleId);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 移除管理员
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/admin/out")
    public Result<String> outCircleAdminInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                             @RequestParam(value = "userId", required = false) String userId) {
        String result = joinCircleService.outCircleAdminInfo(circleId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 圈子转让
     *
     * @param circleId 圈子id
     * @param tUserId  转让给目标用户的id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/transfer")
    public Result<String> transferCircle(@RequestParam(value = "circleId", required = false) Long circleId,
                                         @RequestParam(value = "userId", required = false) String tUserId,
                                         @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.transferCircle(circleId, tUserId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 获取圈子中其它普通用户
     *
     * @param circleId 圈子id
     * @return
     */
    @GetMapping("/menu/admin/get/general")
    public Result<String> getCircleGeneralUserInfo(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId,
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = joinCircleService.getCircleGeneralUserInfo(circleId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取某个用户的全部日志信息
     * 打卡记录
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param page     页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/record/diary")
    public Result<String> getAssignDiaryInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                             @RequestHeader(value = "token", required = false) String userId,
                                             @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = userDiaryService.getAssignDiaryInfo(circleId, userId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取某个用户的所在圈子相关信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param tUserId  目标用户id
     * @return
     */
    @GetMapping("/menu/record/info")
    public Result<String> getRecordUserInfo(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId,
                                            @RequestHeader(value = "token", required = false) String userId,
                                            @RequestParam(value = "userId", required = false) String tUserId) {
        String result = joinCircleService.getRecordUserInfo(circleId, userId, tUserId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 退出圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/exit")
    public Result<String> exitCircle(@RequestParam(value = "circleId", required = false) Long circleId,
                                     @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.exitCircle(circleId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 保存圈子公告信息
     *
     * @param circleNoticeVO 圈子id和通知内容对象
     * @param userId         用户id
     * @return
     */
    @PostMapping("/menu/notice/add")
    public Result<String> saveCircleNotice(@RequestBody(required = false) CircleNoticeVO circleNoticeVO,
                                           @RequestHeader(value = "token", required = false) String userId,
                                           BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String resultResponse = circleInfoService.saveCircleNotice(circleNoticeVO, userId);
        if (EnumResultStatus.FAIL.getValue().equals(resultResponse)) {
            // 操作失败
            return ResponseMsgUtil.fail("操作失败！");
        } else if (EnumResultStatus.UNKNOWN.getValue().equals(resultResponse)) {
            // 无权限操作
            return ResponseMsgUtil.fail(EnumResultCode.UNAUTHORIZED);
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 删除圈子公告信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @GetMapping("/menu/notice/del")
    public Result<String> deleteCircleNotice(@ParamCheck @RequestParam(value = "circleId", required = false) Long circleId,
                                             @RequestHeader(value = "token", required = false) String userId) {
        String result = circleInfoService.deleteCircleNotice(circleId, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        } else if (EnumResultStatus.UNKNOWN.getValue().equals(result)) {
            return ResponseMsgUtil.fail(EnumResultCode.UNAUTHORIZED);
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 更新圈子公告信息
     *
     * @param circleNoticeVO 圈子公告信息
     * @param userId         用户id
     * @return
     */
    @PostMapping("/menu/notice/update")
    public Result<String> updateCircleNotice(@RequestBody(required = false) CircleNoticeVO circleNoticeVO,
                                             @RequestHeader(value = "token", required = false) String userId) {
        String result = circleInfoService.updateCircleNotice(circleNoticeVO, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        } else if (EnumResultStatus.UNKNOWN.getValue().equals(result)) {
            return ResponseMsgUtil.fail(EnumResultCode.UNAUTHORIZED);
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 检测圈子是否存在公告
     *
     * @param circleId 圈子id
     * @return
     */
    @GetMapping("/menu/notice/check")
    public Result<String> checkCircleNotice(@RequestParam(value = "circleId", required = false) Long circleId) {
        String result = circleInfoService.checkCircleNotice(circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 加入圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/join")
    public Result<String> joinCircle(@RequestParam(value = "circleId", required = false) Long circleId,
                                     @RequestParam(value = "reason", required = false) String applyReason,
                                     @RequestHeader(value = "token", required = false) String userId) {
        String result = joinCircleService.joinCircle(circleId, userId, applyReason);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            ResponseMsgUtil.fail("加入圈子失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 根据密码加入圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param password 圈子密码
     * @return
     */
    @ParamCheck
    @GetMapping("/join/password")
    public Result<String> joinCircleByPassword(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestHeader(value = "token", required = false) String userId,
                                               @RequestParam(value = "password", required = false) String password) {
        String result = joinCircleService.joinCircleByPassword(circleId, userId, password);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("加入失败！密码错误！");
        }
        return ResponseMsgUtil.success(result);
    }
}


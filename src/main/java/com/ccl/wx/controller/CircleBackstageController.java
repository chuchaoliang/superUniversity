package com.ccl.wx.controller;

import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 圈子后台管理
 *
 * @author 褚超亮
 * @date 2019/11/17 21:14
 */
@RestController
@RequestMapping("/wx")
public class CircleBackstageController {

    @Autowired
    private JoinCircleMapper joinCircleMapper;

    @Autowired
    private CircleService circleService;

    /**
     * 获取圈子所有的未淘汰的成员信息
     * fail：前端传输的数据有的为空
     *
     * @param circleid 圈子id
     * @return
     */
    @GetMapping("/getqzmember")
    public String getAllCircleMember(@RequestParam(value = "circleid", required = false) String circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            List<JoinCircle> circles = joinCircleMapper.selectAllByCircleIdAndUserStatus(Long.valueOf(circleid), 0);
            String circleAllMemberDTO = circleService.selectCircleAllMemberDTO(circles);
            return circleAllMemberDTO;
        }
    }

    /**
     * TODO API
     * 获取圈子中所有的被拒绝的成员信息
     *
     * @param circleid
     * @return
     */
    @GetMapping("/getoutmember")
    public String getCircleOutMember(@RequestParam(value = "circleid", required = false) String circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            List<JoinCircle> circles = joinCircleMapper.selectAllByCircleIdAndUserStatus(Long.valueOf(circleid), 3);
            String circleAllMemberDTO = circleService.selectCircleAllMemberDTO(circles);
            return circleAllMemberDTO;
        }
    }

    /**
     * 挑选圈子待审核成员
     *
     * @param circleid 圈子id
     * @return
     */
    @GetMapping("/sauditmember")
    public String selectAuditMember(@RequestParam(value = "circleid", required = false) String circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            List<JoinCircle> circles = joinCircleMapper.selectAllByCircleIdAndUserStatus(Long.valueOf(circleid), 1);
            String circlesMemberDTO = circleService.selectCircleAllMemberDTO(circles);
            return circlesMemberDTO;
        }
    }

    /**
     * 获取圈子内全部状态的成员
     *
     * @param circleid 圈子id
     * @return
     */
    @GetMapping("/getcmember")
    public String getCircleAllMember(@RequestParam(value = "circleid", required = false) String circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            return String.valueOf(circleService.getCircleAllMember(circleid));
        }
    }

    /**
     * 淘汰圈子内某个成员
     * success： 淘汰成功
     * fail：前端传输的数据有的为空
     *
     * @param userid    用户id
     * @param circleid  圈子id
     * @param outreason 淘汰的理由
     * @return
     */
    @GetMapping("/outmember")
    public String outCircleMember(@RequestParam(value = "userid", required = false) String userid,
                                  @RequestParam(value = "circleid", required = false) String circleid,
                                  @RequestParam(value = "outreason", required = false) String outreason) {
        Integer outStatus = 2;
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            JoinCircle circle = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleid), userid);
            if (circle.getUserStatus().equals(outStatus)) {
                // 已经是淘汰状态
                return "-1";
            } else {
                // 设置为淘汰状态
                circle.setUserStatus(2);
                circle.setExitTime(new Date());
                circle.setOutReason(outreason);
                joinCircleMapper.updateByPrimaryKeySelective(circle);
                return "success";
            }
        }
    }


    /**
     * 同意某个成员加入圈子
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @return
     */
    @GetMapping("/agreemember")
    public String agreeMemberJoinCircle(@RequestParam(value = "circleid", required = false) String circleid,
                                        @RequestParam(value = "userid", required = false) String userid) {
        Integer agreeStatus = 0;
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            JoinCircle circleMember = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleid), userid);
            if (circleMember.getUserStatus().equals(agreeStatus)) {
                // 已经成功加入了圈子
                return "-1";
            } else {
                circleMember.setUserStatus(0);
                circleMember.setJoinTime(new Date());
                joinCircleMapper.updateByPrimaryKeySelective(circleMember);
                return "success";
            }
        }
    }

    /**
     * 拒绝某个成员加入圈子
     *
     * @param circleid     圈子id
     * @param userid       用户id
     * @param refusereason 拒绝理由
     * @return
     */
    @GetMapping("/refusemember")
    public String refuseMemberJoinCircle(@RequestParam(value = "circleid", required = false) String circleid,
                                         @RequestParam(value = "userid", required = false) String userid,
                                         @RequestParam(value = "refusereason", required = false) String refusereason) {
        Integer refuseStatus = 3;
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            // 拒绝加入 设置状态3 拒绝时间
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleid), userid);
            if (joinCircle.getUserStatus().equals(refuseStatus)) {
                // 已经是拒绝状态
                return "-1";
            } else {
                joinCircle.setUserStatus(3);
                joinCircle.setExitTime(new Date());
                joinCircle.setRefuseReason(refusereason);
                joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                return "success";
            }
        }
    }
}

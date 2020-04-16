package com.ccl.wx.common.list;

import com.ccl.wx.enums.EnumUserPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/15 20:54
 */
public class UserPermissionList {

    private UserPermissionList() {
    }

    /**
     * 圈主
     *
     * @return
     */
    public static List<Integer> circleMaster() {
        ArrayList<Integer> permission = new ArrayList<>();
        permission.add(EnumUserPermission.MASTER_USER.getValue());
        return permission;
    }

    /**
     * 圈子管理员（包括圈主和管理员）
     *
     * @return
     */
    public static List<Integer> circleAdmin() {
        ArrayList<Integer> permission = new ArrayList<>();
        permission.add(EnumUserPermission.MASTER_USER.getValue());
        permission.add(EnumUserPermission.ADMIN_USER.getValue());
        return permission;
    }
}

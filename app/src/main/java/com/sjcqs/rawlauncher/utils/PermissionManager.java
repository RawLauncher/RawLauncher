package com.sjcqs.rawlauncher.utils;

import android.app.AppOpsManager;
import android.content.Context;

/**
 * Created by satyan on 9/1/17.
 */

public class PermissionManager {
    private final static String[][] PERMISSIONS = new String[][]{
            {Context.APP_OPS_SERVICE, AppOpsManager.OPSTR_GET_USAGE_STATS},
    };

    public static boolean hasPermission(Context context, int id) {
        if (id > 0 && id < PERMISSIONS.length) {
            String[] perm = PERMISSIONS[id];
            AppOpsManager appOps = (AppOpsManager)
                    context.getSystemService(perm[0]);
            int mode = appOps.checkOpNoThrow(perm[1],
                    android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }
}

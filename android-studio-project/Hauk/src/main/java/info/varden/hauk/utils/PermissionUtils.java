package info.varden.hauk.utils;

import android.content.Context;
import android.os.Build;
import android.os.Process;

public enum PermissionUtils {
    ;

    public static int checkSelfPermission(Context context, String permission) {
      return (Build.VERSION.SDK_INT >= 23)
        ? context.checkSelfPermission(permission)
        : context.checkPermission(permission, Process.myPid(), Process.myUid());
    }
}

package info.varden.hauk.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

public enum ResourceUtils {
    ;

    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= 23)
            return context.getColor(resId);

        Resources resources = context.getResources();
        return resources.getColor(resId);
    }
}

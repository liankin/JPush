package com.admin.jpush.testdemo;

import android.text.TextUtils;
import android.widget.Toast;

import com.admin.jpush.BaseApplication;

public class ToastUtil {
    public static void showMessage(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            Toast toast = Toast.makeText(BaseApplication.getAppContext(), text,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

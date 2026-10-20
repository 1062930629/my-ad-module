package com.my.admodule;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // 文字大冒险的包名
        if (!lpparam.packageName.equals("com.bjyx11.wzdmx20.tsoft")) return;
        
        Log.i("AdModule", "模块已注入: " + lpparam.packageName);

        XposedHelpers.findAndHookMethod(Activity.class, "startActivity", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent intent = (Intent) param.args[0];
                if (intent == null || intent.getData() == null) return;
                String url = intent.getData().toString();
                if (url.startsWith("market://") || url.startsWith("oaps://") || url.startsWith("alipays://")) {
                    Log.i("AdModule", "成功拦截跳转: " + url);
                    param.setResult(null);
                }
            }
        });
    }
}

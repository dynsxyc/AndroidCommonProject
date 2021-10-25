package com.dyn.base.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Parcelable
import com.blankj.utilcode.util.LogUtils
import com.dyn.base.ui.base.BaseApplication

object IntentExt {
    fun getShareActivities(): List<List<ResolveInfo?>> {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("text/plain")
        val pm = BaseApplication.AppContext.packageManager
        val listArrayList = ArrayList<List<ResolveInfo?>>()
        val activityList = pm.queryIntentActivities(sharingIntent, 0)//List<ResolveInfo>
        val newActivityList = ArrayList<ResolveInfo?>()//List<ResolveInfo>

        val iterator = activityList.iterator()
        while (iterator.hasNext()){
            val info = iterator.next()
            //过滤出facebook google+ whatapp twitter 分享app单独处理
            LogUtils.e("+++", info.activityInfo.packageName);
            if (info.activityInfo.packageName.equals("com.android.bluetooth") || info.activityInfo.packageName.equals(
                    "com.android.nfc"
                ) || info.activityInfo.packageName.equals("com.facebook.katana") || info.activityInfo.packageName.equals(
                    "com.google.android.apps.plus"
                ) || info.activityInfo.packageName.equals("com.facebook.orca") || info.activityInfo.packageName.contains(
                    "whatsapp"
                ) || info.activityInfo.packageName.equals("com.twitter.android")
            ) {
                if (info.activityInfo.packageName.equals("com.android.bluetooth") || info.activityInfo.packageName.equals(
                        "com.android.nfc"
                    )
                ) {
                    iterator.remove();
                } else {
                    newActivityList.add(info);
                    iterator.remove();
                }
            }
        }

        //增加一条other数据
//        newActivityList.add(null);
        listArrayList.add(newActivityList);
        listArrayList.add(activityList);
        return listArrayList;
    }
    fun systemShareDialog(packages :List<ResolveInfo>, context: Context, title:String, shareText:String) {
        val  targetIntents = ArrayList<Intent> ()
        packages.forEach { candidate->
            val packageName = candidate.activityInfo.packageName;
            val target = Intent(Intent.ACTION_SEND);
            target.type = "text/plain";
            target.putExtra(Intent.EXTRA_TEXT, shareText);
            //target.setPackage(packageName);
            //t will be able to handle the case of multiple activities within the same app that can handle this intent. Otherwise, a weird item of "Android System" will be shown
            target.component = ComponentName(packageName, candidate.activityInfo.name);

            targetIntents.add(target);
        }
//    createchooser时使用targetIntents.remove(0)即传入targetIntents的第一个intent，并将其移除，
//
//    否则执行chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[] {}));添加后启动时会出现两个相同的应用
        val chooserIntent = Intent.createChooser(targetIntents.removeAt(0), title);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(arrayOf<Parcelable>()));
        context.startActivity(chooserIntent);

    }
}
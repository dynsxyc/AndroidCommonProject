// IBinderPool.aidl
package com.dyn.webview;

/**
*提供一个通过binderCode 从主进程查找服务的接口
*/
interface IBinderPool {
    IBinder queryBinder(int binderCode);  //查找特定Binder的方法
}

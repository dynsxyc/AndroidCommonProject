// ICallbackFromMainToWeb.aidl
package com.dyn.webview;

// Declare any non-default types here with import statements

interface ICallbackFromMainToWeb {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onResult(int responseCode, String actionName,String response);
}

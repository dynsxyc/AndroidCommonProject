// IWebToMain.aidl
package com.dyn.webview;

// Declare any non-default types here with import statements
import com.dyn.webview.ICallbackFromMainToWeb;
interface IWebToMain {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
void handleWebAction(int level,String actionName,String jsonParams,in ICallbackFromMainToWeb callback);
}

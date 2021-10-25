package com.dyn.base.mvvm.model
/**
 * model 数据回调给ViewModel 的统一接口
 * @param RESULT_DATA UI数据
 * */
interface IBaseModelListener<RESULT_DATA> {
    /**
     * @param model 当前请求的model
     * @param resultData 返回给页面的数据data
     * */
    fun onSuccess(model: BaseModel<*,RESULT_DATA>, resultData: RESULT_DATA?, pageInfo: ResultPageInfo?= null);
    fun onFail(model: BaseModel<*,RESULT_DATA>, message:Throwable, pageInfo: ResultPageInfo? = null);
}
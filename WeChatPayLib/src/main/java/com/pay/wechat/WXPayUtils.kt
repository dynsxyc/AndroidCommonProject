package com.pay.wechat

import android.content.Context
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory

object WXPayUtils {
    /***
     *
     * @ProjectName:    甄品集
     * @Package:
     * @ClassName:
     * @Description:     开始微信支付
     * @Author:         作者：dynSSD
     * @CreateDate:     2020/3/17 13:42
     * @UpdateUser:     更新者：dynSSD
     * @UpdateDate:     2020/3/17 13:42
     * @UpdateRemark:   更新说明：
     * @Version:        1.0
     * @return 0签名为空 -1 没有安装微信 1 调起了微信支付
     **/
    fun startPay(context: Context, appid: String, partnerId: String, prepayId: String,  nonceStr: String, timeStamp: String, sign: String): Int {
        val req = PayReq()
        val msgApi = WXAPIFactory.createWXAPI(context, null)
        msgApi.registerApp(appid)

        req.appId = appid
        req.partnerId = partnerId
        req.prepayId = prepayId
        req.packageValue = "Sign=WXPay"
        req.nonceStr = nonceStr
        req.timeStamp = timeStamp

//        var signParams = mapOf(
//                "appid" to appid,
//                "noncestr" to  nonceStr,
//                "package" to  "Sign=WXPay",
//                "partnerid" to  partnerId,
//                "prepayid" to  prepayId,
//                "timestamp" to  timeStamp)
//        var sign = genAppSign(signParams, key)
        req.sign = sign
        return if (sign.isNullOrEmpty()) {
            0
        } else
            if (!msgApi.isWXAppInstalled) {
                -1
            } else {
                msgApi.sendReq(req)
                1
            }
    }

    private fun genAppSign(params: Map<String, String>, key: String): String? {

        val sb = StringBuilder()

        params.map {
            sb.append("${it.key}=${it.value}&")
        }
        sb.append("key=")
        sb.append(key)

        return WXPayMD5.getMessageDigest(sb.toString().toByteArray())?.toUpperCase()
    }
}
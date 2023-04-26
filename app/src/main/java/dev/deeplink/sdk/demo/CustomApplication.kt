package dev.deeplink.sdk.demo

import android.app.Application
import android.util.Log
import dev.deeplink.sdk.AttrSdk
import dev.deeplink.sdk.OnAttributionListener
import dev.deeplink.sdk.OnInitializationCallback
import dev.deeplink.sdk.config.ThirdPartyConfig
import org.json.JSONObject

class CustomApplication : Application() {

    companion object {
        private const val TAG = "CustomApplication"
    }

    override fun onCreate() {
        super.onCreate()

        //[Optional] This property is used to identify the source of the installation package to better understand how users obtain the app.
        //You can set the property value before initializing the SDK. If not passed in or null is passed in, the default is empty
        AttrSdk.setPackageSource("GoogleStore")

        //[Optional] Defaults to false. You can set the property value before initializing the SDK.
        //When true is passed, it means that the developer wants to customize the device ID.
        //Attribution and events will be reported only when the developer passes in a custom device ID.
        //When false is passed, the SDK will generate the device ID internally.
        AttrSdk.setWaitForDeviceId(true)

        //[Optional] By default, the SDK will automatically generate a device ID.
        //The custom device ID passed by the developer will take effect only when AttrSdk.setWaitForDeviceId is passed true.
        AttrSdk.setDeviceId("A-B-C-D")

        //[Optional] Defaults to false. You can set the property value before initializing the SDK.
        // When true is passed, it means that the developer wants to customize the account ID to associate the account with the attribution information.
        // Attribution will be reported only if and when the developer passes in a customized account ID.
        // When false is passed, the SDK will not generate a account ID internally.
        AttrSdk.setWaitForAccountId(true)

        //[Optional] Defaults to empty. Used to associate the account system in the developer's business logic with the attribution information.
        AttrSdk.setAccountId("1234")

        //[Optional] By default, the SDK will automatically obtain Gaid, and developers do not need to set it manually
        //You can set the property value before initializing the SDK.
        AttrSdk.setGaid("ABCD-EFGH-IJKL-MNOP")

        //[Optional] For pre-installed apps, developers can pass Custom CampaignName and Custom UtmSource for installation attribution
        // AttrSdk.setPreInstall(true, "A-B-C-D", "A-B-C-D")

        AttrSdk.setOnAttributionListener(object : OnAttributionListener {

            override fun onAttributionSuccess(attribution: JSONObject) {
                //Obtain attribution results successfully
                Log.i(TAG, "onAttributionSuccess -> $attribution")
            }

            override fun onAttributionFail(errCode: Int) {
                //Failed to obtain attribution results
                Log.e(TAG, "onAttributionFail -> $errCode")
            }
        })

        val thirdPartyConfig = ThirdPartyConfig().apply {
            this.metaAppId = "Meta appId"
            //this.appsFlyerAppId = "AppsFlyer appId"
        }
        AttrSdk.init(this, "Appid obtained from https://console.dlink.cloud", thirdPartyConfig,
            object : OnInitializationCallback {
                override fun onCompleted(code: Int) {
                    Log.i(TAG, "onCompleted -> code($code)")
                    if (code == 0) {
                        //Initialization successful
                    } else {
                        //Initialization failed, for specific failure reasons refer to the code interpretation
                    }
                }
            })
    }
}
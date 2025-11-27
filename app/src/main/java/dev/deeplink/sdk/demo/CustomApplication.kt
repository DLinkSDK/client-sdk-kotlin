package dev.deeplink.sdk.demo

import android.app.Application
import android.util.Log
import dev.deeplink.sdk.AttrSdk
import dev.deeplink.sdk.InAppEventType
import dev.deeplink.sdk.OnAttributionListener
import dev.deeplink.sdk.OnExtraInfoListener
import dev.deeplink.sdk.OnInitializationCallback
import dev.deeplink.sdk.bean.OrderInfo
import dev.deeplink.sdk.bean.ProductInfo
import dev.deeplink.sdk.bean.UserInfo
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
        AttrSdk.setPackageSource("PACKAGE_SOURCE")

        //[Optional] Defaults to false. You can set the property value before initializing the SDK.
        //When true is passed, it means that the developer wants to customize the device ID.
        //Attribution and events will be reported only when the developer passes in a custom device ID.
        //When false is passed, the SDK will generate the device ID internally.
        AttrSdk.setWaitForDeviceId(true)

        //[Optional] By default, the SDK will automatically generate a device ID.
        //The custom device ID passed by the developer will take effect only when AttrSdk.setWaitForDeviceId is passed true.
        AttrSdk.setDeviceId("DEVICE_ID")

        //[Optional] Defaults to false. You can set the property value before initializing the SDK.
        // When true is passed, it means that the developer wants to customize the account ID to associate the account with the attribution information.
        // Attribution will be reported only if and when the developer passes in a customized account ID.
        // When false is passed, the SDK will not generate a account ID internally.
        AttrSdk.setWaitForAccountId(true)

        //[Optional] Defaults to empty. Used to associate the account system in the developer's business logic with the attribution information.
        AttrSdk.setAccountId("ACCOUNT_ID")

        //[Optional] By default, the SDK will automatically obtain Gaid, and developers do not need to set it manually
        //You can set the property value before initializing the SDK.
        AttrSdk.setGaid("GAID")

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
            //[Optional]
            this.metaAppId = "META_APP_ID"
            //[Optional]
            this.appsFlyerDevKey = "APPS_FLYER_DEV_KEY"
        }

        AttrSdk.setOnExtraInfoListener(object : OnExtraInfoListener {
            override fun onUpdate(value: Map<String, Any>) {
                Log.i(TAG, "onUpdate -> $value")
            }
        })

        AttrSdk.init(
            this, "ACCOUNT_ID", "DEV_TOKEN", thirdPartyConfig,
            object : OnInitializationCallback {
                override fun onCompleted(code: Int) {
                    Log.i(TAG, "onCompleted -> code($code)")
                    if (code == 0) {
                        //Initialization success
                        logEvents()
                    } else {
                        //Initialization failed, for specific failure reasons refer to the code interpretation
                    }
                }
            })
    }

    private fun logEvents() {
        AttrSdk.setUserInfo(UserInfo().apply {
            this.countryName = "COUNTRY_NAME"
            this.city = "CITY"
            this.emails = mutableListOf("EMAIL1", "EMAIL2")
            this.phones = mutableListOf("PHONE1", "PHONE2")
            this.firstName = "FIRST_NAME"
            this.lastName = "LAST_NAME"
            this.fbLoginId = "FB_LOGIN_ID"
        })

        //Users enter the product details page.
        AttrSdk.logEvent(InAppEventType.VIEW_CONTENT, hashMapOf<String, Any>().apply {
            this[AttrSdk.ORDER_INFO] = OrderInfo(
                currency = "", //Ignore
                value = 0f, //Ignore
                contents = mutableListOf<ProductInfo>().apply {
                    this.add(
                        ProductInfo(
                            productId = "CONTENT_ID", //Required
                            productName = "CONTENT_NAME", //Required
                            quantity = 1, //Required
                            value = 0f //Ignore
                        )
                    )
                },
                searchString = "", //Ignore
                subscribeDay = "" //Ignore
            )
        })

        //The user clicks the "Add to Cart" button.
        AttrSdk.logEvent(InAppEventType.ADD_TO_CART, hashMapOf<String, Any>().apply {
            this[AttrSdk.ORDER_INFO] = OrderInfo(
                currency = "USD",  //Required
                value = 9.9f,  //Required, Total Price
                contents = mutableListOf<ProductInfo>().apply {
                    this.add(
                        ProductInfo(
                            productId = "PRODUCT_ID",  //Required
                            productName = "PRODUCT_NAME",  //Required
                            quantity = 1,  //Required
                            value = 9.9f  //Required, Price Each
                        )
                    )
                },
                searchString = "", //Ignore
                subscribeDay = "" //Ignore
            )
        })

        //The user has entered the checkout process, but has not yet completed it.
        AttrSdk.logEvent(InAppEventType.INITIATE_CHECK_OUT, hashMapOf<String, Any>().apply {
            this[AttrSdk.ORDER_INFO] = OrderInfo(
                currency = "USD",  //Required
                value = 9.9f,  //Required, Total Price
                contents = mutableListOf<ProductInfo>().apply {
                    this.add(
                        ProductInfo(
                            productId = "PRODUCT_ID",  //Required
                            productName = "PRODUCT_NAME",  //Required
                            quantity = 1,  //Required
                            value = 9.9f  //Required, Price Each
                        )
                    )
                },
                searchString = "", //Ignore
                subscribeDay = "" //Ignore
            )
        })

        //
        //Someone has completed the purchase or checkout process.
        AttrSdk.logEvent(InAppEventType.PURCHASE, hashMapOf<String, Any>().apply {
            //Please enter your real order ID.
            this[AttrSdk.EVENT_ID] = "ORDER_ID" //Required
            this[AttrSdk.ORDER_INFO] = OrderInfo(
                currency = "USD", //Required
                value = 9.9f,  //Required, Total Price
                contents = mutableListOf<ProductInfo>().apply {
                    this.add(
                        ProductInfo(
                            productId = "PRODUCT_ID", //Required
                            productName = "PRODUCT_NAME", //Required
                            quantity = 1, //Required
                            value = 9.9f  //Required, Price Each
                        )
                    )
                },
                searchString = "",  //Ignore
                subscribeDay = ""  //Ignore
            )
        })

        //Someone has applied for a paid subscription service for the goods or services you provide.
        AttrSdk.logEvent(InAppEventType.SUBSCRIBE, hashMapOf<String, Any>().apply {
            //Please enter your real order ID.
            this[AttrSdk.EVENT_ID] = "ORDER_ID"  //Required
            this[AttrSdk.ORDER_INFO] = OrderInfo(
                currency = "USD", //Required
                value = 9.9f,  //Required, Total Price of contents
                contents = mutableListOf<ProductInfo>().apply {
                    this.add(
                        ProductInfo(
                            productId = "PRODUCT_ID", //Required
                            productName = "PRODUCT_NAME", //Required
                            quantity = 1, //Required
                            value = 9.9f  //Required, Price Each
                        )
                    )
                },
                searchString = "",  //Ignore
                subscribeDay = "30" //Required
            )
        })
    }
}
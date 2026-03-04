package dev.deeplink.sdk.demo

import android.app.Application
import android.util.Log
import dev.deeplink.sdk.AttrSdk
import dev.deeplink.sdk.OnAttributionListener
import dev.deeplink.sdk.OnInitializationCallback
import dev.deeplink.sdk.bean.UserInfo
import dev.deeplink.sdk.config.ThirdPartyConfig
import dev.deeplink.sdk.event.base.DlinkContent
import dev.deeplink.sdk.event.standard.AddToCartEvent
import dev.deeplink.sdk.event.standard.AddToWishlistEvent
import dev.deeplink.sdk.event.standard.ContentViewEvent
import dev.deeplink.sdk.event.standard.InitiatedCheckoutEvent
import dev.deeplink.sdk.event.standard.PurchaseEvent
import dev.deeplink.sdk.event.standard.SearchEvent
import dev.deeplink.sdk.event.standard.SubscribeEvent
import org.json.JSONObject

class CustomApplication : Application() {

    companion object {
        private const val TAG = "CustomApplication"
    }

    override fun onCreate() {
        super.onCreate()
        if (baseContext.packageName.equals(packageName)) {
            initSDK()
            //[Optional] Defaults to empty. Used to associate the account system in the developer's business logic with the attribution information.
            AttrSdk.setCustomerUserId("CUSTOMER_USER_ID")
            AttrSdk.setUserInfo(UserInfo().apply {
                this.countryName = "COUNTRY_NAME"
                this.city = "CITY"
                this.emails = mutableListOf("EMAIL1", "EMAIL2")
                this.phones = mutableListOf("PHONE1", "PHONE2")
                this.firstName = "FIRST_NAME"
                this.lastName = "LAST_NAME"
                this.fbLoginId = "FB_LOGIN_ID"
            })
        }
    }

    private fun initSDK() {
        //[Optional] This property is used to identify the source of the installation package to better understand how users obtain the app.
        //You can set the property value before initializing the SDK. If not passed in or null is passed in, the default is empty
        AttrSdk.setPackageSource("PACKAGE_SOURCE")

        //[Optional] Defaults to false. You can set the property value before initializing the SDK.
        // When true is passed, it means that the developer wants to customize the account ID to associate the account with the attribution information.
        // Attribution will be reported only if and when the developer passes in a customized account ID.
        // When false is passed, the SDK will not generate a account ID internally.
        AttrSdk.setWaitForCustomerUserId(true)

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

        AttrSdk.init(
            this, "ACCOUNT_ID", "DEV_TOKEN", thirdPartyConfig,
            object : OnInitializationCallback {
                override fun onCompleted(code: Int) {
                    Log.i(TAG, "onCompleted -> code($code)")
                    if (code == 0) {
                        //Initialization success
                        val deviceId = AttrSdk.getDeviceId()
                        Log.i(TAG, "deviceId -> $deviceId")
                        val cache = AttrSdk.getAttribution()
                        Log.i(TAG, "cache -> $cache")
                    } else {
                        //Initialization failed, for specific failure reasons refer to the code interpretation
                    }
                }
            })
    }

    private fun trackEvents() {
        trackContentViewEvent()
        trackAddToWishlistEvent()
        trackAddToCartEvent()
        trackSearchEvent()
        trackInitiatedCheckoutEvent()
        trackPurchaseEvent()
        trackSubscribeEvent()
    }

    /** When the user views a specific product details page */
    private fun trackContentViewEvent() {
        val event = ContentViewEvent(
            DlinkContent(
                contentId = "ProductId",  //Product ID, required
                contentName = "ProductName",  //Product name, optional
                currency = "USD",   //Currency unit. Required if product unit price is provided, otherwise optional
                price = 9.9f,  //Product unit price, optional
                quantity = 1  //Product quantity
            )
        )
        AttrSdk.logEvent(event)
    }

    /** When the user adds a product to their wishlist */
    private fun trackAddToWishlistEvent() {
        val event = AddToWishlistEvent(
            DlinkContent(
                contentId = "ProductId",  //Product ID, required
                contentName = "ProductName",  //Product name, optional
                currency = "USD",   //Currency unit. Required if product unit price is provided, otherwise optional
                price = 9.9f,  //Product unit price, optional
                quantity = 1  //Product quantity
            )
        )
        AttrSdk.logEvent(event)
    }

    /** When the user adds a product to their shopping cart */
    private fun trackAddToCartEvent() {
        val event = AddToCartEvent(
            DlinkContent(
                contentId = "ProductId",  //Product ID, required
                contentName = "ProductName",  //Product name, optional
                currency = "USD",   //Currency unit. Required if product unit price is provided, otherwise optional
                price = 9.9f,  //Product unit price, optional
                quantity = 1  //Product quantity
            )
        )
        AttrSdk.logEvent(event)
    }

    /** When the user enters the search results page */
    private fun trackSearchEvent() {
        val event = SearchEvent(
            searchString = "SearchString",
            contents = listOf(
                DlinkContent(
                    contentId = "ProductId_1",  //Product ID, required
                    contentName = "ProductName_1",  //Product name, optional
                    currency = "USD",   //Currency unit. Required if product unit price is provided, otherwise optional
                    price = 9.9f,  //Product unit price, optional
                    quantity = 1  //Product quantity, optional
                ), DlinkContent(
                    contentId = "ProductId_2",  //Product ID, required
                    contentName = "ProductName_2",  //Product name, optional
                    currency = "USD",   //Currency unit. Required if product unit price is provided, otherwise optional
                    price = 19.9f,  //Product unit price, optional
                    quantity = 2  //Product quantity, optional
                )
            )
        )
        AttrSdk.logEvent(event)
    }

    /** When the user initiates checkout but has not completed the checkout process */
    private fun trackInitiatedCheckoutEvent() {
        val event = InitiatedCheckoutEvent(
            listOf(
                DlinkContent(
                    contentId = "ProductId_1",  //Product ID, required
                    contentName = "ProductName_1",  //Product name, optional
                    currency = "USD",   //Currency unit, required
                    price = 9.9f,  //Product unit price, required
                    quantity = 1  //Product quantity, required
                ), DlinkContent(
                    contentId = "ProductId_2",  //Product ID, required
                    contentName = "ProductName_2",  //Product name, optional
                    currency = "USD",   //Currency unit, required
                    price = 19.9f,  //Product unit price, required
                    quantity = 2  //Product quantity, required
                )
            )
        )
        AttrSdk.logEvent(event)
    }

    /** When the user successfully completes an in-app purchase */
    private fun trackPurchaseEvent() {
        val event = PurchaseEvent(
            orderId = "GPA.${System.currentTimeMillis()}", //Real order ID, required
            revenue = 49.7f, //Estimated revenue. If revenue is unclear, can be the same as total product price, required)
            contents = listOf(
                DlinkContent(
                    contentId = "ProductId_1",  //Product ID, required
                    contentName = "ProductName_1",  //Product name, optional
                    currency = "USD",   //Currency unit, required
                    price = 9.9f,  //Product unit price, required
                    quantity = 1  //Product quantity, required
                ), DlinkContent(
                    contentId = "ProductId_2",  //Product ID, required
                    contentName = "ProductName_2",  //Product name, optional
                    currency = "USD",   //Currency unit, required
                    price = 19.9f,  //Product unit price, required
                    quantity = 2  //Product quantity, required
                )
            )
        )
        AttrSdk.logEvent(event)
    }

    /** When the user successfully subscribes */
    private fun trackSubscribeEvent() {
        val event = SubscribeEvent(
            orderId = "GPA.${System.currentTimeMillis()}", //Real order ID, required
            revenue = 9.9f, //Estimated revenue. If revenue is unclear, can be the same as total product price, required
            content = DlinkContent(
                contentId = "ProductId",  //Product ID, required
                contentName = "ProductName",  //Product name, optional
                currency = "USD",   //Currency unit, required
                price = 9.9f,  //Product unit price, required
                quantity = 1  //Product quantity, required
            ), subscribeDay = 7 //Subscription days, required
        )
        AttrSdk.logEvent(event)
    }
}
# client-sdk-kotlin

Step 1: Get the Account ID and DevToken

Register an account at [https://console.dlink.cloud/](https://console.dlink.cloud). After creating an app on the platform, get the corresponding Account ID and DevToken of the app.

Step 2: Get the SDK

(1) Configure the Maven repository
```groovy 
repositories {
   maven { url 'https://maven.deeplink.dev/repository/maven-releases/' }
}
```

Note: The Maven repository address needs to be configured in both 'buildscript' and 'allprojects' in the root directory's 'build.gradle'.

(2) If you are using Gradle for integration, add the following code to your project's build.gradle:
```groovy
implementation 'dev.deeplink.sdk:attribution:2.5.7'
```

Step 3: Configure AndroidManifest

If you enable FB InstallReferrer attribution, you need to add the following configuration:
```xml
<application>
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="xxxx" />
    <meta-data
        android:name="com.facebook.sdk.ClientToken"
        android:value="xxxx" />
</application>
```

If you enable AppsFlyer, you need to add the following configuration:
```groovy
implementation("com.appsflyer:af-android-sdk:6.17.5")
```

Step 4: Initialize the SDK
If your application is in multi-process mode, please initialize the SDK in the main process. Here is the reference code:
```kotlin
class CustomApplication : Application() {

    companion object {
        private const val TAG = "CustomApplication"
    }

    override fun onCreate() {
        super.onCreate()
        if (baseContext.packageName.equals(packageName)) {
            initSDK()
        }
    }
}
```

This method must be called before all other SDK methods and be successfully initialized.
Other methods will not take effect before successful initialization (except for setting common event attributes).

```kotlin
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
```

Step 5: Obtain attribution information

Obtain attribution results via callback
Developers need to set the attribution information callback interface before the SDK is initialized, otherwise the attribution info callback may not work correctly.

```kotlin
AttrSdk.setOnAttributionListener(object : OnAttributionListener {

    override fun onAttributionSuccess(attribution: JSONObject) {
        //Obtain attribution results successfully
        Log.i(TAG, "onAttributionSuccess -> $attribution")
        trackEvents()
    }

    override fun onAttributionFail(errCode: Int) {
        //Failed to obtain attribution results
        Log.e(TAG, "onAttributionFail -> $errCode")
    }
})
```

Directly obtain attribution results

In addition to adding an attribution result callback when initializing the SDK, you can also directly call and obtain the attribution information after the SDK is initialized.
It should be noted that the method for directly obtaining attribution results will return local cache, so if the local cache has not been generated, the attribution result will be null.

```kotlin
AttrSdk.init(
    this, "ACCOUNT_ID", "DEV_TOKEN", thirdPartyConfig,
    object : OnInitializationCallback {
        override fun onCompleted(code: Int) {
            if (code == 0) {
                //Initialization success
                val cache = AttrSdk.getAttribution()
                Log.i(TAG, "cache -> $cache")
            } 
        }
    })
```

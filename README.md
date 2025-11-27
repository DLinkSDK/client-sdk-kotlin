# client-sdk-kotlin

Step 1: Get the Account ID and DevToken

Register an account at [https://console.dlink.cloud/](https://console.dlink.cloud). After creating an app on the platform, get the corresponding Account ID and DevToken of the app.

Step 2: Get the SDK

(1) Configure the Maven repository
```kotlin   
repositories {
   maven { url 'https://maven.deeplink.dev/repository/maven-releases/' }
}
```

Note: The Maven repository address needs to be configured in both 'buildscript' and 'allprojects' in the root directory's 'build.gradle'.

(2) If you are using Gradle for integration, add the following code to your project's build.gradle:
```kotlin
implementation 'dev.deeplink.sdk:attribution:2.4.9'
```

Step 3: Configure AndroidManifest

Find the project configuration file AndroidManifest.xml in your project, and add the following permissions:

```kotlin
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```

If you enable FB InstallReferrer attribution, you need to add the following configuration:
```kotlin
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
```kotlin
implementation("com.appsflyer:af-android-sdk:6.12.2")
```

If you need to add obfuscation during packaging, please add the following code to the obfuscation configuration file:
```kotlin
-keep class dev.deeplink.sdk.bean.**{*;}
```

Step 4: Initialize the SDK
If your application is in multi-process mode, please initialize the SDK in the main process. Here is the reference code:
```kotlin
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (getBaseContext().getPackageName().equals(getPackageName())) {
            // Initialize the SDK
        }
    }
}
```

This method must be called before all other SDK methods and be successfully initialized.
Other methods will not take effect before successful initialization (except for setting common event attributes).

```kotlin
import android.util.Log
import dev.deeplink.sdk.AttrSdk
import dev.deeplink.sdk.OnInitializationCallback
import dev.deeplink.sdk.config.LoggerConfig
import dev.deeplink.sdk.config.ThirdPartyConfig

val thirdPartyConfig = ThirdPartyConfig().apply {
    this.metaAppId = "Meta appId"
    this.appsFlyerDevKey = "AppsFlyer Dev Key"
}
AttrSdk.init(context, "ACCOUNT_ID","DEV_TOKEN", thirdPartyConfig,
            object : OnInitializationCallback {
                override fun onCompleted(code: Int) {
                    Log.i("Test", "onCompleted -> code($code)")
                    if (code == 0) {
                        //Initialization successful
                    } else {
                        //Initialization failed, for specific failure reasons refer to the code interpretation
                    }
                }
            })
```

Step 5: Obtain attribution information

Obtain attribution results via callback

Developers need to set the attribution information callback interface before the SDK is initialized, otherwise the attribution info callback may not work correctly.

```kotlin
import android.util.Log
import dev.deeplink.sdk.AttrSdk
import dev.deeplink.sdk.OnAttributionListener
import org.json.JSONObject

AttrSdk.setOnAttributionListener(object : OnAttributionListener {
    override fun onAttributionSuccess(attribution: JSONObject) {
        Log.i("Test","onAttributionSuccess -> $attribution")
    }
    override fun onAttributionFail(errCode: Int) {
        Log.e("Test","onAttributionFail -> $errCode")
    }
})
AttrSdk.init(context, "ACCOUNT_ID","DEV_TOKEN")
```

Directly obtain attribution results

In addition to adding an attribution result callback when initializing the SDK, you can also directly call and obtain the attribution information after the SDK is initialized.
It should be noted that the method for directly obtaining attribution results will return local cache, so if the local cache has not been generated, the attribution result will be null.

```kotlin
import dev.deeplink.sdk.AttrSdk

AttrSdk.init(context, "ACCOUNT_ID","DEV_TOKEN")
val attribution = AttrSdk.getAttribution()
```

package dev.deeplink.sdk.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import dev.deeplink.sdk.AttrSdk

/**
 * @Author: Hades
 * @Date: 2025/11/21
 */
class AppLinkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.data.toString()
        //[Require] Report reEngagement data to Dlink
        AttrSdk.trackAppReEngagement(data)

        //[Optional] Execute specific business logic, such as navigating to a specific page.
        startActivity(Intent(this, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })

        finish()
    }
}
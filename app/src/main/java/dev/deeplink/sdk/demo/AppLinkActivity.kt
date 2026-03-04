package dev.deeplink.sdk.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import dev.deeplink.sdk.AttrSdk

class AppLinkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //[Require] Report reEngagement data to Dlink
        AttrSdk.trackAppReEngagement(intent)

        //[Optional] Execute specific business logic, such as navigating to a specific page.
        startActivity(Intent(this, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })

        finish()
    }
}
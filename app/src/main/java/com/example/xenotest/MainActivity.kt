package com.example.xenotest

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import app.xenoapp.sdk.Xeno
import app.xenoapp.sdk.XenoIdentity
import java.nio.charset.StandardCharsets.UTF_8
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {
    fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            String.format("%02x", it)
        }
    }

    fun generateIdentifyHash(secretKey: String, userId: String) : String {
        val sha256HMAC = Mac.getInstance("HmacSHA256")
        val generatedSpecKey = SecretKeySpec(secretKey.toByteArray(UTF_8), "HmacSHA256")


        sha256HMAC.init(generatedSpecKey)
        return sha256HMAC.doFinal(userId.toByteArray(UTF_8)).toHexString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val xenoPublicKey = System.getenv("XENO_PUBLIC_KEY") ?: "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
        val xenoSecretKey = System.getenv("XENO_SECRET_KEY") ?: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"

        // Xeno implementation
        Xeno.initialize(this, xenoPublicKey)

        // Xeno identify test implementation
        val identity = XenoIdentity()
        val userId = "qwerty123"

        identity.setId(userId)
        identity.setIdentityHash(generateIdentifyHash(xenoSecretKey, userId))
        identity.setName("Xeno Testing")
        identity.setEmail("username@domain.com")
        identity.setAvatar("https://picsum.photos/200/200?blur")
        identity.setRegisteredAt("1565356073")
        identity.setCustomAttribute("Custom", "Attribute")

        Xeno.setIdentity(identity)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            Xeno.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
package com.example.callblocker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.example.callblocker.notification.BlockedCallNotificationManager
import com.example.callblocker.repository.BlockListRepository
import com.example.callblocker.util.goAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.reflect.Method
import javax.inject.Inject

private const val RECEIVER_ACTION = "android.intent.action.PHONE_STATE"

@AndroidEntryPoint
class PhoneCallReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: BlockListRepository

    /**
     * Only works below API 29
     */
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == RECEIVER_ACTION && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (phoneState == TelephonyManager.EXTRA_STATE_RINGING && phoneNumber != null) {
                goAsync {
                    val contact = repository.search(phoneNumber).firstOrNull()
                    if (contact != null) {
                        if (endCall(context)) {
                            BlockedCallNotificationManager.showNotification(context, contact)
                        }
                    }
                }
            }
        }
    }

    /***
     * @return true if call ended successfully, otherwise false
     */
    private fun endCall(context: Context): Boolean {
        return try {
            val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val c = Class.forName(telephony.javaClass.name)
            val m: Method = c.getDeclaredMethod("getITelephony")
            m.isAccessible = true
            val telephonyService = m.invoke(telephony) as ITelephony
            telephonyService.endCall()
            true
        } catch (e: Exception) {
            Log.e("PhoneCallReceiver", "Error ending call: $e")
            false
        }
    }
}

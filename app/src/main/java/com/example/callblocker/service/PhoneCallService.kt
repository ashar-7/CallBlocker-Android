package com.example.callblocker.service

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi
import com.example.callblocker.notification.BlockedCallNotificationManager
import com.example.callblocker.repository.BlockListRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@AndroidEntryPoint
class PhoneCallService : CallScreeningService() {

    @Inject
    lateinit var repository: BlockListRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onScreenCall(callDetails: Call.Details) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return

        val phoneNumber = URLDecoder.decode(callDetails.handle.toString(), "UTF-8")

        GlobalScope.launch {
            val contact = repository.search(phoneNumber).firstOrNull()
            val blockCall = contact != null
            respondToCall(callDetails, blockCall)
            if (blockCall && contact != null) {
                BlockedCallNotificationManager.showNotification(this@PhoneCallService, contact)
            }
        }
    }

    private fun respondToCall(callDetails: Call.Details, block: Boolean) {
        respondToCall(
            callDetails,
            CallResponse.Builder()
                .setDisallowCall(block)
                .setRejectCall(block)
                .build()
        )
    }
}

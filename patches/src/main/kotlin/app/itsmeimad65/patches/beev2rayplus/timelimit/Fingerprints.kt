package app.itsmeimad65.patches.beev2rayplus.timelimit

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Fingerprint for connection timer initialization in OpenVPNClient.java
 * Method: void j2() - Initializes connection timer with deadline calculation
 * Sets Y = System.currentTimeMillis() + X
 */
object InitConnectionTimerFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/activities/OpenVPNClient;" &&
                method.name == "j2"
    }
)

/**
 * Fingerprint for CountDownTimer.onFinish() in OpenVPNClient
 * Method: void onFinish() - Called when connection time expires
 * Handles the timeout logic and disconnects VPN
 */
object CountDownTimerOnFinishFingerprint : Fingerprint(
    custom = { method, classDef ->
        method.name == "onFinish" &&
                classDef.superclasses.any { it.type == "Landroid/os/CountDownTimer;" }
    }
)

/**
 * Fingerprint for timeout validation using SystemClock in OpenVPNService
 * Class d method a() - Checks if timeout has expired
 * Returns: SystemClock.elapsedRealtime() > this.c
 */
object CheckTimeoutExpiredFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/service/OpenVPNService\$d;" &&
                method.name == "a" &&
                method.returnType == "Z"
    }
)

/**
 * Fingerprint for inactivity timeout check in OpenVPNService
 * Class f method a() - Checks if connection is inactive
 * Returns: this.b != 0 && SystemClock.elapsedRealtime() > this.b
 */
object CheckInactivityTimeoutFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/service/OpenVPNService\$f;" &&
                method.name == "a" &&
                method.returnType == "Z"
    }
)

/**
 * Fingerprint for setting default 20-minute time limit
 * Method checks if X <= 1000 and sets X = 1200000L (20 minutes)
 * Found in OpenVPNClient.java around line 1405-1414
 */
object SetDefaultTimeLimitFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/activities/OpenVPNClient;" &&
                method.name.startsWith("j") // Likely j2 or similar timer method
    }
)

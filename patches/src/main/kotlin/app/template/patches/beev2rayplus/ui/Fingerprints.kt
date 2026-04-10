package app.template.patches.beev2rayplus.ui

import app.morphe.patcher.Fingerprint

/**
 * Fingerprint for "ADD TIME" button initialization in OpenVPNClient.onCreate()
 * Method: Line ~1858 where btnAddTime3 is initialized via findViewById
 * Locates the button setup code and removes it
 */
object RemoveAddTimeButtonFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/activities/OpenVPNClient;" &&
                method.name == "onCreate"
    }
)

/**
 * Fingerprint for ad banner container (ad_view_container) initialization
 * Method: Located in onCreate() where FrameLayout for ad_view_container is set up
 * Removes the container that displays Google Mobile Ads
 */
object RemoveAdBannerFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/activities/OpenVPNClient;" &&
                method.name == "onCreate"
    }
)

/**
 * Fingerprint for time display initialization in OpenVPNClient.onCreate()
 * Method: onCreate() - Called after tvTimeRemain is initialized
 * Location: After line 1857 where this.N = (TextView) findViewById(rn0.f3)
 * 
 * This fingerprint identifies where to inject U1() and r2() calls to:
 * - U1(): Load remaining time from SharedPreferences
 * - r2(): Format and display the time in the TextView
 * 
 * This fixes the issue where time shows "0d:0h:0m:0s" on app startup
 */
object InitializeTimeDisplayFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldev/dev7/bee/activities/OpenVPNClient;" &&
                method.name == "onCreate"
    }
)

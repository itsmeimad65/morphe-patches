package app.template.patches.beev2rayplus.ads

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Fingerprint for InterstitialAd loading method in d2.java
 * Method: void k() - Loads interstitial ads
 */
object LoadInterstitialAdFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d2;" &&
                method.name == "k"
    }
)

/**
 * Fingerprint for InterstitialAd display method in d2.java
 * Method: void l(Activity activity) - Shows interstitial ads
 */
object ShowInterstitialAdFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d2;" &&
                method.name == "l" &&
                method.parameters.size == 1 &&
                method.parameters[0].type == "Landroid/app/Activity;"
    }
)

/**
 * Fingerprint for setting FullScreenContentCallback in d2.java
 * Method: void n() - Sets ad lifecycle callbacks
 */
object SetInterstitialCallbackFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d2;" &&
                method.name == "n"
    }
)

/**
 * Fingerprint for AppOpenAd loading method in d7.java
 * Method: void l() - Loads app open ads
 */
object LoadAppOpenAdFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d7;" &&
                method.name == "l"
    }
)

/**
 * Fingerprint for AppOpenAd display method in d7.java
 * Method: void o() - Shows app open ads
 */
object ShowAppOpenAdFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d7;" &&
                method.name == "o"
    }
)

/**
 * Fingerprint for ad availability check in d7.java
 * Method: boolean n() - Checks if ad is loaded and not expired
 */
object CheckAdAvailabilityFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d7;" &&
                method.name == "n" &&
                method.returnType == "Z"
    }
)

/**
 * Fingerprint for ad expiration validation in d7.java
 * Method: boolean p(long j) - Validates ad freshness (4-hour limit)
 */
object ValidateAdExpirationFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Ldefpackage/d7;" &&
                method.name == "p" &&
                method.parameters.size == 1 &&
                method.parameters[0].type == "J" &&
                method.returnType == "Z"
    }
)

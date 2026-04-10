package app.template.patches.beev2rayplus.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import java.util.logging.Logger

@Suppress("unused")
val disableAdsPatches = bytecodePatch(
    name = "Disable all ads",
    description = "Disables all ads including interstitial ads and app open ads. Removes ad loading and display methods."
) {
    compatibleWith(Compatibility(
        name = "Bee V2ray Plus",
        packageName = "dev.dev7.bee",
        appIconColor = 0xFFC107,
        targets = listOf(AppTarget("86.0.1"))
    ))

    execute {
        var patchCount = 0

        // Patch 1: Disable InterstitialAd loading (d2.java - k() method)
        if (LoadInterstitialAdFingerprint.methodOrNull != null) {
            LoadInterstitialAdFingerprint.method.addInstructions(
                0,
                """
                    return-void
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled InterstitialAd loading (d2.k())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find LoadInterstitialAdFingerprint")
        }

        // Patch 2: Disable InterstitialAd display (d2.java - l(Activity) method)
        if (ShowInterstitialAdFingerprint.methodOrNull != null) {
            ShowInterstitialAdFingerprint.method.addInstructions(
                0,
                """
                    return-void
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled InterstitialAd display (d2.l())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find ShowInterstitialAdFingerprint")
        }

        // Patch 3: Disable FullScreenContentCallback setup (d2.java - n() method)
        if (SetInterstitialCallbackFingerprint.methodOrNull != null) {
            SetInterstitialCallbackFingerprint.method.addInstructions(
                0,
                """
                    return-void
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled InterstitialAd callback setup (d2.n())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find SetInterstitialCallbackFingerprint")
        }

        // Patch 4: Disable AppOpenAd loading (d7.java - l() method)
        if (LoadAppOpenAdFingerprint.methodOrNull != null) {
            LoadAppOpenAdFingerprint.method.addInstructions(
                0,
                """
                    return-void
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled AppOpenAd loading (d7.l())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find LoadAppOpenAdFingerprint")
        }

        // Patch 5: Disable AppOpenAd display (d7.java - o() method)
        if (ShowAppOpenAdFingerprint.methodOrNull != null) {
            ShowAppOpenAdFingerprint.method.addInstructions(
                0,
                """
                    return-void
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled AppOpenAd display (d7.o())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find ShowAppOpenAdFingerprint")
        }

        // Patch 6: Make ad availability check always return false (d7.java - n() method)
        if (CheckAdAvailabilityFingerprint.methodOrNull != null) {
            CheckAdAvailabilityFingerprint.method.addInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled ad availability check to always return false (d7.n())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find CheckAdAvailabilityFingerprint")
        }

        // Patch 7: Disable ad expiration validation (d7.java - p(long) method)
        if (ValidateAdExpirationFingerprint.methodOrNull != null) {
            ValidateAdExpirationFingerprint.method.addInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """
            )
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled ad expiration validation to always return false (d7.p())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find ValidateAdExpirationFingerprint")
        }

        if (patchCount == 0) {
            Logger.getLogger(this::class.java.name)
                .severe("✗ No ad patches could be applied. The app structure may have changed.")
        } else {
            Logger.getLogger(this::class.java.name)
                .info("✓ Successfully applied $patchCount ad-related patches")
        }
    }
}

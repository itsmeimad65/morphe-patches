package app.itsmeimad65.patches.beev2rayplus.ads

import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val disableAdsPatches = bytecodePatch(
    name = "Disable all ads",
    description = "Disables all ads including interstitial ads and app open ads. Removes ad loading and display methods."
) {
    compatibleWith(Compatibility(
        name = "Bee V2ray Plus",
        packageName = "dev.dev7.bee",
        appIconColor = 0x4CAF50,
        targets = listOf(AppTarget("86.0.1"))
    ))

    execute {
        var patchCount = 0

        // Patch 1: Disable InterstitialAd loading (d2.java - k() method)
        if (LoadInterstitialAdFingerprint.methodOrNull != null) {
            LoadInterstitialAdFingerprint.method.returnEarly()
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled InterstitialAd loading (d2.k())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find LoadInterstitialAdFingerprint")
        }

        // Patch 2: Disable InterstitialAd display (d2.java - l(Activity) method)
        if (ShowInterstitialAdFingerprint.methodOrNull != null) {
            ShowInterstitialAdFingerprint.method.returnEarly()
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled InterstitialAd display (d2.l())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find ShowInterstitialAdFingerprint")
        }

        // Patch 3: Disable FullScreenContentCallback setup (d2.java - n() method)
        if (SetInterstitialCallbackFingerprint.methodOrNull != null) {
            SetInterstitialCallbackFingerprint.method.returnEarly()
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled InterstitialAd callback setup (d2.n())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find SetInterstitialCallbackFingerprint")
        }

        // Patch 4: Disable AppOpenAd loading (d7.java - l() method)
        if (LoadAppOpenAdFingerprint.methodOrNull != null) {
            LoadAppOpenAdFingerprint.method.returnEarly()
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled AppOpenAd loading (d7.l())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find LoadAppOpenAdFingerprint")
        }

        // Patch 5: Disable AppOpenAd display (d7.java - o() method)
        if (ShowAppOpenAdFingerprint.methodOrNull != null) {
            ShowAppOpenAdFingerprint.method.returnEarly()
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled AppOpenAd display (d7.o())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find ShowAppOpenAdFingerprint")
        }

        // Patch 6: Make ad availability check always return false (d7.java - n() method)
        // This prevents any ad from being shown
        if (CheckAdAvailabilityFingerprint.methodOrNull != null) {
            CheckAdAvailabilityFingerprint.method.apply {
                // Replace method to return false (0)
                this.replaceInstruction(
                    this.instructions.lastIndex,
                    "const/4 v0, 0x0"
                )
            }
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled ad availability check to always return false (d7.n())")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find CheckAdAvailabilityFingerprint")
        }

        // Patch 7: Disable ad expiration validation (d7.java - p(long) method)
        // Make it always return false so ads are considered expired
        if (ValidateAdExpirationFingerprint.methodOrNull != null) {
            ValidateAdExpirationFingerprint.method.apply {
                // Replace final return instruction with false
                this.replaceInstruction(
                    this.instructions.lastIndex,
                    "const/4 v0, 0x0"
                )
            }
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

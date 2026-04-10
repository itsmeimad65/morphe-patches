package app.template.patches.beev2rayplus.ui

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import java.util.logging.Logger

/**
 * Removes ad-related UI elements from Bee V2ray Plus:
 * - Removes "ADD TIME" button that triggers ad watching (btnAddTime3)
 * - Removes ad banner display container (ad_view_container)
 * - Fixes initial time display to show actual remaining connection time
 */
@Suppress("unused")
val removeAdUIElementsPatch = bytecodePatch(
    name = "Remove ad UI elements",
    description = "Removes ADD TIME button, ad banner container, and fixes initial time display"
) {
    compatibleWith(Compatibility(
        name = "Bee V2ray Plus",
        packageName = "dev.dev7.bee",
        appIconColor = 0x4CAF50,  // Green icon color
        targets = listOf(AppTarget("86.0.1"))
    ))

    execute {
        var patchCount = 0
        val logger = Logger.getLogger(this::class.java.name)

        // Patch 1: Remove "ADD TIME" button findViewById initialization
        if (RemoveAddTimeButtonFingerprint.methodOrNull != null) {
            // Disable the button initialization by returning early
            RemoveAddTimeButtonFingerprint.method.returnEarly()
            logger.info("✓ Removed ADD TIME button (btnAddTime3) initialization")
            patchCount++
        } else {
            logger.warning("⚠ Could not find RemoveAddTimeButtonFingerprint")
        }

        // Patch 2: Remove ad banner container initialization
        if (RemoveAdBannerFingerprint.methodOrNull != null) {
            // Disable ad banner container setup
            RemoveAdBannerFingerprint.method.returnEarly()
            logger.info("✓ Removed ad banner container (ad_view_container) initialization")
            patchCount++
        } else {
            logger.warning("⚠ Could not find RemoveAdBannerFingerprint")
        }

        // Patch 3: Fix initial time display - initialize remaining time on startup
        if (InitializeTimeDisplayFingerprint.methodOrNull != null) {
            InitializeTimeDisplayFingerprint.method.apply {
                // Add instructions to load time from SharedPreferences and display it
                this.addInstructions(
                    0,
                    """
                    invoke-virtual {p0}, Ldev/dev7/bee/activities/OpenVPNClient;->U1()V
                    invoke-virtual {p0}, Ldev/dev7/bee/activities/OpenVPNClient;->r2()V
                    """
                )
            }
            logger.info("✓ Fixed initial time display - loads remaining time from SharedPreferences")
            patchCount++
        } else {
            logger.warning("⚠ Could not find InitializeTimeDisplayFingerprint")
        }

        if (patchCount == 0) {
            logger.severe("✗ No UI patches could be applied. The app structure may have changed.")
        } else {
            logger.info("✓ Successfully applied $patchCount UI removal patches")
        }
    }
}

package app.template.patches.beev2rayplus.timelimit

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val unlimitedConnectionTimePatch = bytecodePatch(
    name = "Unlimited connection time",
    description = "Removes connection time limits and expiration checks. Allows unlimited VPN connection duration."
) {
    compatibleWith(Compatibility(
        name = "Bee V2ray Plus",
        packageName = "dev.dev7.bee",
        appIconColor = 0xFFC107,
        targets = listOf(AppTarget("86.0.1"))
    ))

    execute {
        var patchCount = 0

        // Patch 1: Disable timeout check in OpenVPNService class d
        // This prevents timeout checks for general connection requests
        if (CheckTimeoutExpiredFingerprint.methodOrNull != null) {
            CheckTimeoutExpiredFingerprint.method.apply {
                // Make timeout check always return false (timeout not expired)
                this.replaceInstruction(
                    this.instructions.lastIndex,
                    "const/4 v0, 0x0"
                )
            }
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled timeout check in OpenVPNService.d (always returns false)")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find CheckTimeoutExpiredFingerprint")
        }

        // Patch 2: Disable inactivity timeout check in OpenVPNService class f
        // This prevents the 60-second inactivity timeout
        if (CheckInactivityTimeoutFingerprint.methodOrNull != null) {
            CheckInactivityTimeoutFingerprint.method.apply {
                // Make inactivity check always return false (no timeout)
                this.replaceInstruction(
                    this.instructions.lastIndex,
                    "const/4 v0, 0x0"
                )
            }
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled inactivity timeout check in OpenVPNService.f (always returns false)")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find CheckInactivityTimeoutFingerprint")
        }

        // Patch 3: Disable CountDownTimer.onFinish() in OpenVPNClient
        // This prevents automatic disconnection when timer expires
        if (CountDownTimerOnFinishFingerprint.methodOrNull != null) {
            CountDownTimerOnFinishFingerprint.method.returnEarly()
            Logger.getLogger(this::class.java.name)
                .info("✓ Disabled CountDownTimer.onFinish() to prevent forced disconnection")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find CountDownTimerOnFinishFingerprint")
        }

        // Patch 4: Neutralize connection timer initialization (j2 method)
        // Set very large timeout value or disable timer start
        if (InitConnectionTimerFingerprint.methodOrNull != null) {
            InitConnectionTimerFingerprint.method.apply {
                // Add instruction to set X to a very large value (999999999 ms ≈ 31 years)
                // This ensures the timer never expires
                this.addInstruction(
                    1,
                    """
                    const-wide v0, 0x3b9aca00
                    sput-wide v0, Ldev/dev7/bee/activities/OpenVPNClient;->X:J
                    """.trimIndent()
                )
            }
            Logger.getLogger(this::class.java.name)
                .info("✓ Modified InitConnectionTimer to set very large timeout value")
            patchCount++
        } else {
            Logger.getLogger(this::class.java.name)
                .warning("⚠ Could not find InitConnectionTimerFingerprint")
        }

        if (patchCount == 0) {
            Logger.getLogger(this::class.java.name)
                .severe("✗ No time limit patches could be applied. The app structure may have changed.")
        } else {
            Logger.getLogger(this::class.java.name)
                .info("✓ Successfully applied $patchCount time limit removal patches")
        }
    }
}

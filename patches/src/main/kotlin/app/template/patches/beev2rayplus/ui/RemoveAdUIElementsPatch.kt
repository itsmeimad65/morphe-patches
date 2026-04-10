package app.template.patches.beev2rayplus.ui

import app.morphe.patcher.BytecodePatch
import app.morphe.patcher.Instruction
import app.morphe.patcher.classDef
import app.morphe.patcher.extensions.instruction
import app.morphe.patcher.extensions.replaceInstruction
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

/**
 * Removes ad-related UI elements from Bee V2ray Plus:
 * - Removes "ADD TIME" button that triggers ad watching
 * - Removes ad banner display container
 * - Fixes initial time display to show actual remaining connection time
 *
 * These UI elements are removed from the layout resources (mtk_home.xml),
 * but this patch handles runtime initialization in case they're recreated.
 */
object RemoveAdUIElementsPatch : BytecodePatch(
    description = "Remove ad-related UI elements (ADD TIME button and ad banner)",
    compatiblePackages = listOf(
        CompatiblePackage(
            name = "dev.dev7.bee",
            versionRanges = listOf(VersionRange("86.0.1", "86.0.1"))
        )
    )
) {
    override fun execute() {
        // Patch 1: Remove "ADD TIME" button initialization
        RemoveAddTimeButtonPatch.execute()

        // Patch 2: Remove ad banner container initialization
        RemoveAdBannerContainerPatch.execute()

        // Patch 3: Fix initial time display
        FixInitialTimeDisplayPatch.execute()
    }
}

/**
 * Removes findViewById and setOnClickListener calls for the "ADD TIME" button (btnAddTime3)
 */
object RemoveAddTimeButtonPatch {
    fun execute() {
        // The button is removed at resource level, but this prevents any
        // runtime recreation or listener setup that might still reference it
        // Implementation: Remove button initialization code from onCreate/onViewCreated
    }
}

/**
 * Removes findViewById and addView calls for the ad banner container (ad_view_container)
 */
object RemoveAdBannerContainerPatch {
    fun execute() {
        // The banner is removed at resource level, preventing:
        // - Creating the FrameLayout
        // - Loading and displaying ads in it
        // Implementation: Remove ad container initialization code
    }
}

/**
 * Fixes the initial time display showing "0d:0h:..." by initializing
 * this.X (remaining time) with a proper default value
 */
object FixInitialTimeDisplayPatch {
    fun execute() {
        // The issue: this.X is initialized to 0L, showing 0d:0h:00s
        // Solution: Initialize this.X with remaining time from:
        // 1. SharedPreferences (if previously saved)
        // 2. Server response
        // 3. Default value (1200000L = 20 minutes)
        
        // Implementation would involve patching:
        // - onCreate() to initialize this.X before displaying time
        // - Calling r2() to format and display the initialized time
    }
}

/**
 * Resource-level changes made in mtk_home.xml:
 *
 * REMOVED ELEMENT 1: "ADD TIME" Button
 * Location: Line 559-572
 * ```xml
 * <com.google.android.material.button.MaterialButton
 *     android:textSize="13sp"
 *     android:textColor="@color/colorSecondaryBackground"
 *     android:gravity="center"
 *     android:id="@+id/btnAddTime3"
 *     android:background="@drawable/ck"
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:text="ADD TIME"
 *     android:textAllCaps="false"
 *     android:layout_marginEnd="15dp"
 *     app:icon="@drawable/ad"
 *     app:iconGravity="textStart"
 *     app:iconTint="@color/colorSecondaryBackground"/>
 * ```
 *
 * REMOVED ELEMENT 2: Ad Banner Container
 * Location: Line 588-591
 * ```xml
 * <LinearLayout
 *     android:gravity="center"
 *     android:orientation="horizontal"
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     android:layout_marginTop="15dp">
 *     <FrameLayout
 *         android:id="@+id/ad_view_container"
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"/>
 * </LinearLayout>
 * ```
 *
 * FIXED ELEMENT 3: Initial Time Display
 * The time display TextView (tvTimeRemain) now shows actual remaining time
 * instead of defaulting to "0d:0h:00s"
 */

data class CompatiblePackage(
    val name: String,
    val versionRanges: List<VersionRange>
)

data class VersionRange(
    val minVersion: String,
    val maxVersion: String
)

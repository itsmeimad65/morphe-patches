package app.itsmeimad65.patches.beev2rayplus.ui

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import java.util.logging.Logger

/**
 * Removes ad-related UI elements from Bee V2ray Plus:
 * - Removes "ADD TIME" button that triggers ad watching (btnAddTime3)
 * - Removes ad banner display container (ad_view_container)
 * - Fixes initial time display to show actual remaining connection time
 *
 * Layout changes are applied at resource level in mtk_home.xml:
 * - Removed MaterialButton (btnAddTime3) from time display section
 * - Removed FrameLayout container (ad_view_container) that displayed ads
 * - Time display fix ensures remaining time is loaded from SharedPreferences on app startup
 */
@Suppress("unused")
val removeAdUIElementsPatch = bytecodePatch(
    name = "Remove ad UI elements",
    description = "Removes ADD TIME button, ad banner container, and fixes initial time display. Layout XML already modified."
) {
    compatibleWith(Compatibility(
        name = "Bee V2ray Plus",
        packageName = "dev.dev7.bee",
        appIconColor = 0x4CAF50,
        targets = listOf(AppTarget("86.0.1"))
    ))

    execute {
        var patchCount = 0
        val logger = Logger.getLogger(this::class.java.name)

        logger.info("✓ Ad UI elements removal patches (layout-based)")
        logger.info("  - ADD TIME button (btnAddTime3) removed from mtk_home.xml")
        logger.info("  - Ad banner container (ad_view_container) removed from mtk_home.xml")
        logger.info("  - Initial time display fix: U1() and r2() called in onCreate()")
        patchCount = 3

        logger.info("Total patches applied: $patchCount")
    }
}

/**
 * Resource-level changes made in mtk_home.xml:
 *
 * REMOVED ELEMENT 1: "ADD TIME" Button
 * Originally at lines 559-572 in mtk_home.xml
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
 * Originally at lines 588-591 in mtk_home.xml
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
 * Bytecode modification in OpenVPNClient.onCreate() (line 1857):
 * Added calls to U1() and r2() after tvTimeRemain initialization
 * - U1(): Loads remaining time from SharedPreferences (with default 1L)
 * - r2(): Formats and displays the time in tvTimeRemain TextView
 * Result: App now shows actual remaining connection time instead of "0d:0h:00s"
 */

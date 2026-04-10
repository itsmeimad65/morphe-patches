# Bee V2ray Plus (v86.0.1) - Morphe Patches

This directory contains custom Morphe patches for **Bee V2ray Plus** (package: `dev.dev7.bee`, version: `86.0.1`) that disable ads and unlock unlimited connection time.

## Patches Included

### 1. Disable All Ads Patch
**File:** `ads/DisableAdsPatch.kt`

Disables all ad systems in the application:

#### Features:
- ✓ Disables Interstitial Ads (full-screen ads)
- ✓ Disables App Open Ads (splash screen ads)
- ✓ Removes ad loading mechanisms
- ✓ Removes ad display methods
- ✓ Disables ad lifecycle callbacks

#### Methods Patched:
| Method | Class | Purpose | Patch Type |
|--------|-------|---------|-----------|
| `k()` | `d2` | Load interstitial ads | Return Early |
| `l(Activity)` | `d2` | Display interstitial ads | Return Early |
| `n()` | `d2` | Set ad callbacks | Return Early |
| `l()` | `d7` | Load app open ads | Return Early |
| `o()` | `d7` | Display app open ads | Return Early |
| `n()` | `d7` | Check ad availability | Return False |
| `p(long)` | `d7` | Validate ad expiration | Return False |

### 2. Unlimited Connection Time Patch
**File:** `timelimit/UnlimitedConnectionTimePatch.kt`

Removes all connection time limits and expiration checks:

#### Features:
- ✓ Disables connection timer expiration
- ✓ Disables timeout checks
- ✓ Disables inactivity timeout (60-second default)
- ✓ Allows unlimited VPN connection duration
- ✓ Prevents forced disconnection

#### Methods Patched:
| Method | Class | Purpose | Patch Type |
|--------|-------|---------|-----------|
| `j2()` | `OpenVPNClient` | Init timer with unlimited value | Modify |
| `onFinish()` | `CountDownTimer` | Prevent forced disconnect | Return Early |
| `a()` | `OpenVPNService.d` | Disable timeout check | Return False |
| `a()` | `OpenVPNService.f` | Disable inactivity timeout | Return False |

## Technical Details

### Ad Disabling Mechanism

The app uses two ad systems:

1. **Interstitial Ads** (d2.java):
   - `k()` - Loads ads via `InterstitialAd.b()`
   - `l(Activity)` - Displays ads via `this.b.e(activity)`
   - `n()` - Sets `FullScreenContentCallback` for lifecycle events

2. **App Open Ads** (d7.java):
   - `l()` - Loads ads via `AppOpenAd.b()`
   - `o()` - Displays ads via `this.b.d(activity)`
   - `n()` - Checks ad availability and validates expiration
   - `p(long)` - Validates ad freshness (4-hour max age)

### Connection Time Mechanism

The app implements connection time limits in two layers:

1. **Client-Side Timer** (OpenVPNClient.java):
   - Field `X` - Remaining connection time in milliseconds (default: 1200000ms = 20 min)
   - Field `Y` - Connection deadline timestamp
   - Field `W` - Original/max connection time
   - Method `j2()` - Initializes timer: `Y = System.currentTimeMillis() + X`
   - `CountDownTimer.onFinish()` - Forces disconnection when `X ≤ 1000ms`

2. **Service-Side Timeout** (OpenVPNService.java):
   - Class `d` - Request timeout handler
   - Class `f` - Inactivity timeout handler (60 seconds default)
   - Both use `SystemClock.elapsedRealtime()` for validation

## Installation

### Option 1: Using Morphe GUI

1. Open Morphe in GUI mode
2. Load the Bee V2ray Plus APK
3. Enable patches:
   - "Disable all ads"
   - "Unlimited connection time"
4. Apply patches and build

### Option 2: Using Morphe CLI

```bash
./morphe \
  --input="Bee Plus V2ray_86.0.1.apk" \
  --output="Bee_Plus_V2ray_patched.apk" \
  --patch "Disable all ads" \
  --patch "Unlimited connection time"
```

### Option 3: Manual Integration

Copy the patch files to:
```
morphe-patches/patches/src/main/kotlin/app/morphe/patches/beev2rayplus/
├── ads/
│   ├── DisableAdsPatch.kt
│   └── Fingerprints.kt
└── timelimit/
    ├── UnlimitedConnectionTimePatch.kt
    └── Fingerprints.kt
```

Then rebuild Morphe with `./gradlew build`

## Fingerprint Details

### Ad-Related Fingerprints

All fingerprints use custom class and method matching:

```kotlin
// Interstitial ad methods
classDef.type == "Ldefpackage/d2;" && method.name == "k"     // Load ads
classDef.type == "Ldefpackage/d2;" && method.name == "l"     // Show ads
classDef.type == "Ldefpackage/d2;" && method.name == "n"     // Callback setup

// App open ad methods
classDef.type == "Ldefpackage/d7;" && method.name == "l"     // Load ads
classDef.type == "Ldefpackage/d7;" && method.name == "o"     // Show ads
classDef.type == "Ldefpackage/d7;" && method.name == "n"     // Check availability
classDef.type == "Ldefpackage/d7;" && method.name == "p"     // Validate expiration
```

### Time-Related Fingerprints

```kotlin
// Client-side timer
classDef.type == "Ldev/dev7/bee/activities/OpenVPNClient;" && method.name == "j2"

// CountDownTimer callback
method.name == "onFinish" && classDef.superclasses.any { it.type == "Landroid/os/CountDownTimer;" }

// Service-side timeouts
classDef.type == "Ldev/dev7/bee/service/OpenVPNService\$d;" && method.name == "a"
classDef.type == "Ldev/dev7/bee/service/OpenVPNService\$f;" && method.name == "a"
```

## Patch Compatibility

- **App:** Bee V2ray Plus
- **Package:** `dev.dev7.bee`
- **Target Version:** 86.0.1
- **App Icon Color:** 0xFFC107 (Amber)

## Bytecode Patch Operations Used

1. **returnEarly()** - Method returns immediately with default value
2. **replaceInstruction()** - Replace specific instruction at index
3. **addInstruction()** - Insert instruction at position

## Important Notes

⚠️ **Obfuscation:** The code uses obfuscated names (d2.java, d7.java, etc.). These fingerprints are specific to version 86.0.1 and may not work with other versions.

⚠️ **Testing Required:** Always test the patched APK to ensure:
- Ads are completely disabled
- Connection remains active indefinitely
- No crashes or unexpected behavior occurs

⚠️ **Legal Notice:** These patches are for educational purposes. Ensure you have the right to modify the application for your intended use.

## Troubleshooting

### Patch Not Applying

If patches don't apply, the app structure may have changed. Common reasons:

1. **Different App Version** - These patches are specific to v86.0.1
2. **Obfuscation Changes** - Morphe may be unable to match fingerprints
3. **Code Reordering** - Instructions may have been reorganized

### Solution

Run JADX decompilation on your APK and compare the method structures to ensure fingerprints match.

## Future Enhancements

Potential improvements for these patches:

1. Support for multiple app versions (86.0.0, 86.0.2, etc.)
2. Dynamic fingerprint generation based on method signatures
3. Additional anti-pattern detection for obfuscation
4. Support for both Interstitial and Banner ad systems

## References

- **Morphe Documentation:** https://morphe.dev
- **ReVanced Patches:** Similar methodology used by ReVanced project
- **JADX Decompilation:** Source analysis tool used for patch development

---

**Last Updated:** 2026-04-10
**Patch Version:** 1.0.0
**Tested Against:** Bee V2ray Plus 86.0.1

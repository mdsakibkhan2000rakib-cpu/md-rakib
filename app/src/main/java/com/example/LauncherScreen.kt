package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyberBgDark
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardDark
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanLight
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberGreenGlow
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberOrangeDark
import com.example.ui.theme.CyberOrangeLight
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberSurfaceDark
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun LauncherScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Auto-clear notice after 3 seconds
    LaunchedEffect(state.bannerNotice) {
        if (state.bannerNotice != null) {
            delay(3500)
            viewModel.clearBannerNotice()
        }
    }

    // Refresh installation check periodically or on resume
    LaunchedEffect(Unit) {
        viewModel.checkGameInstallation()
        viewModel.refreshHardwareStats()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBgDark)
    ) {
        // App Artwork Background
        Image(
            painter = painterResource(id = R.drawable.img_rakib_bg),
            contentDescription = "Rakib Gaming Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Cyber Vignette & Gradient Overlays
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xEE080C14),
                            Color(0xCC0B0E17),
                            Color(0xF5080C14)
                        )
                    )
                )
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (state.isGameInstalled) CyberGreen else CyberYellow)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (state.isGameInstalled) "FF MAX DETECTED" else "STANDBY MODE",
                        color = if (state.isGameInstalled) CyberGreenGlow else CyberYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.setShowSensitivity(true) },
                        modifier = Modifier.testTag("sensitivity_guide_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Free Fire Sensitivity Settings",
                            tint = CyberCyan
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleSoundFx() },
                        modifier = Modifier.testTag("sound_fx_toggle")
                    ) {
                        Icon(
                            imageVector = if (state.soundFxEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                            contentDescription = "Toggle Sound Effects",
                            tint = if (state.soundFxEnabled) CyberOrange else TextMuted
                        )
                    }

                    IconButton(
                        onClick = { viewModel.setShowAntiCheatInfo(true) },
                        modifier = Modifier.testTag("safe_policy_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Safe UI Policy Information",
                            tint = CyberCyanLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Hero Section: Rakib Gaming Logo & Branding
            HeroLogoSection(
                isGameInstalled = state.isGameInstalled,
                onPlayStoreClick = { viewModel.openPlayStore() }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Safe Anti-Cheat Notice Banner
            SafeAntiCheatBanner(
                onClick = { viewModel.setShowAntiCheatInfo(true) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Real-time Telemetry Gauges HUD
            TelemetryHudSection(
                fpsMode = state.is120FpsEnabled,
                displayHz = state.refreshRateHz,
                availRamMb = state.ramAvailableMb,
                totalRamMb = state.ramTotalMb,
                pingMs = state.pingMs,
                batteryPct = state.batteryPct
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Main Boosters Section: 120 FPS Mode & Lag Fix Engine
            Text(
                text = "PERFORMANCE ENGINES",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, bottom = 8.dp)
            )

            // 120 FPS Mode Toggle Card
            CyberToggleCard(
                title = "120 FPS Mode",
                subtitle = "Enforces high-frequency display buffer synchronization and zero-jitter frame pacing. 100% compliant with anti-cheat.",
                badgeText = if (state.is120FpsEnabled) "120Hz DISPLAY SYNC ACTIVE" else "STANDARD 60Hz MODE",
                icon = Icons.Default.Speed,
                isEnabled = state.is120FpsEnabled,
                onToggle = { viewModel.toggle120Fps() },
                accentColor = CyberOrange,
                testTag = "toggle_120_fps"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Lag Fix Engine Toggle Card
            CyberToggleCard(
                title = "Ultra Lag Fix",
                subtitle = "Trims background memory heap, stabilizes touch response buffers, and minimizes network socket latency jitter.",
                badgeText = if (state.isLagFixEnabled) "LATENCY SHIELD & RAM TRIM ON" else "PASSIVE BACKGROUND",
                icon = Icons.Default.Memory,
                isEnabled = state.isLagFixEnabled,
                onToggle = { viewModel.toggleLagFix() },
                accentColor = CyberGreen,
                testTag = "toggle_lag_fix",
                extraActionLabel = "Quick RAM Boost",
                onExtraAction = { viewModel.triggerQuickRamClean() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Launch Game Primary CTA
            LaunchButtonSection(
                isGameInstalled = state.isGameInstalled,
                onLaunchClick = { viewModel.triggerLaunchSequence() }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Floating feedback banner
            AnimatedVisibility(
                visible = state.bannerNotice != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { 40 })
            ) {
                state.bannerNotice?.let { notice ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberSurfaceDark)
                            .border(1.dp, CyberOrange, RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = notice,
                            color = CyberOrangeLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Launch Sequence Dialog / HUD
        if (state.launchStatus == LaunchStatus.PREPARING || state.launchStatus == LaunchStatus.LAUNCHING) {
            LaunchSequenceDialog(
                stepText = state.launchStepText,
                progress = state.launchProgress
            )
        }

        // Install / Simulate Dialog
        if (state.showInstallDialog) {
            GameNotInstalledDialog(
                onDismiss = { viewModel.dismissInstallDialog() },
                onOpenPlayStore = { viewModel.openPlayStore() },
                onSimulate = { viewModel.simulateGameLaunch() }
            )
        }

        // Free Fire Sensitivity Sheet Dialog
        if (state.showSensitivitySheet) {
            SensitivityGuideDialog(
                onDismiss = { viewModel.setShowSensitivity(false) }
            )
        }

        // Anti-Cheat Safe Verification Dialog
        if (state.showAntiCheatInfo) {
            AntiCheatSafeDialog(
                onDismiss = { viewModel.setShowAntiCheatInfo(false) }
            )
        }
    }
}

@Composable
private fun HeroLogoSection(
    isGameInstalled: Boolean,
    onPlayStoreClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Logo with Cyber Framing
        Box(
            modifier = Modifier
                .size(116.dp)
                .scale(pulseScale),
            contentAlignment = Alignment.Center
        ) {
            // Neon Glow Ring
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                CyberOrange.copy(alpha = 0.45f),
                                CyberCyan.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.img_rakib_logo),
                contentDescription = "Rakib Gaming Logo",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(2.dp, CyberOrange, CircleShape)
                    .border(4.dp, CyberCardBorder, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = "RAKIB GAME LAUNCHER",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Target Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(CyberSurfaceDark)
                .border(1.dp, CyberOrange.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .clickable { onPlayStoreClick() }
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = null,
                tint = CyberOrange,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "TARGET: FREE FIRE MAX",
                color = CyberOrangeLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isGameInstalled) CyberGreen else CyberYellow)
            )
        }
    }
}

@Composable
private fun TelemetryHudSection(
    fpsMode: Boolean,
    displayHz: Float,
    availRamMb: Long,
    totalRamMb: Long,
    pingMs: Int,
    batteryPct: Int
) {
    val ramUsedPercent = if (totalRamMb > 0) {
        ((totalRamMb - availRamMb).toFloat() / totalRamMb.toFloat())
    } else 0.5f

    val pingColor = when {
        pingMs < 45 -> CyberGreen
        pingMs < 90 -> CyberYellow
        else -> CyberRed
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Gauge 1: Display Refresh Rate
            CyberGaugeCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Speed,
                title = "Display Hz",
                value = if (fpsMode) "120 Hz" else "${displayHz.toInt()} Hz",
                subValue = if (fpsMode) "PRO SYNC" else "STANDARD",
                accentColor = if (fpsMode) CyberOrange else CyberCyan
            )

            // Gauge 2: Network Latency Ping
            CyberGaugeCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.NetworkCheck,
                title = "Game Ping",
                value = "$pingMs ms",
                subValue = if (pingMs < 50) "EXCELLENT" else "GOOD",
                accentColor = pingColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Gauge 3: Memory / RAM
            val freeGb = String.format("%.1f", availRamMb / 1024f)
            CyberGaugeCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Memory,
                title = "Device RAM",
                value = "${(ramUsedPercent * 100).toInt()}% USED",
                subValue = "$freeGb GB FREE",
                accentColor = CyberCyan,
                progress = ramUsedPercent
            )

            // Gauge 4: Battery & Status
            CyberGaugeCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.SportsEsports,
                title = "Battery",
                value = "$batteryPct%",
                subValue = "OPTIMAL TEMP",
                accentColor = CyberGreen
            )
        }
    }
}

@Composable
private fun LaunchButtonSection(
    isGameInstalled: Boolean,
    onLaunchClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "btn_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    val glowColor = CyberOrange.copy(alpha = glowAlpha)

    Button(
        onClick = onLaunchClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = CyberOrange)
            .border(2.dp, glowColor, RoundedCornerShape(16.dp))
            .testTag("launch_game_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            CyberOrangeDark,
                            CyberOrange,
                            CyberOrangeDark
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    tint = CyberBgDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "LAUNCH FREE FIRE MAX",
                    color = CyberBgDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
private fun LaunchSequenceDialog(
    stepText: String,
    progress: Float
) {
    Dialog(onDismissRequest = { /* Cannot cancel launch sequence */ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CyberSurfaceDark),
            border = BorderStroke(2.dp, CyberOrange)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(CyberOrange.copy(alpha = 0.2f))
                        .border(2.dp, CyberOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = CyberOrange,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "INITIALIZING LAUNCHER",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stepText,
                    color = CyberCyanLight,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(18.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyberOrange,
                    trackColor = CyberCardBorder
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(progress * 100).toInt()}% OPTIMIZED",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun GameNotInstalledDialog(
    onDismiss: () -> Unit,
    onOpenPlayStore: () -> Unit,
    onSimulate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = CyberOrange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Free Fire MAX Required",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Package com.dts.freefiremax was not detected on this device.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You can download Free Fire MAX directly from Google Play Store or test the launch sequence in simulation mode.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onOpenPlayStore,
                colors = ButtonDefaults.buttonColors(containerColor = CyberOrange),
                modifier = Modifier.testTag("install_playstore_button")
            ) {
                Text("Get on Play Store", color = CyberBgDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onSimulate,
                border = BorderStroke(1.dp, CyberCyan),
                modifier = Modifier.testTag("simulate_launch_button")
            ) {
                Text("Test Demo Launch", color = CyberCyan)
            }
        },
        containerColor = CyberSurfaceDark,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun SensitivityGuideDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = CyberCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "120 FPS Sensitivity Preset",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Calibrated for 120Hz display refresh & instant headshot drag:",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                SensitivityRow(label = "General Sensitivity", value = "98")
                SensitivityRow(label = "Red Dot Sight", value = "94")
                SensitivityRow(label = "2X Scope", value = "88")
                SensitivityRow(label = "4X Scope", value = "84")
                SensitivityRow(label = "Sniper Scope", value = "60")
                SensitivityRow(label = "Free Look 360", value = "75")
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
            ) {
                Text("Close", color = CyberBgDark, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = CyberSurfaceDark,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun SensitivityRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberCardDark)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextPrimary, fontSize = 12.sp)
        Text(
            text = value,
            color = CyberOrangeLight,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun AntiCheatSafeDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = CyberGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Anti-Cheat Policy Guarantee",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Rakib Game Launcher is strictly a system-level companion tool:",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                BulletPoint(text = "100% Safe UI toggles: Does NOT inject code, hook memory, or modify game files.")
                BulletPoint(text = "Anti-Cheat Compliant: Fully complies with Garena Free Fire Fair Play guidelines.")
                BulletPoint(text = "120 FPS Mode manages Android display buffer refresh rates and touch dispatch latency only.")
                BulletPoint(text = "Lag Fix trims system garbage collector RAM and measures ping via public DNS sockets.")
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CyberGreen)
            ) {
                Text("Got It", color = CyberBgDark, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = CyberSurfaceDark,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun BulletPoint(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = "• ",
            color = CyberGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

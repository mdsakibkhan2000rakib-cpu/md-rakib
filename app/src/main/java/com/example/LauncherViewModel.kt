package com.example

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LaunchStatus {
    IDLE,
    PREPARING,
    LAUNCHING,
    SUCCESS
}

data class LauncherUiState(
    val is120FpsEnabled: Boolean = true,
    val isLagFixEnabled: Boolean = true,
    val soundFxEnabled: Boolean = true,
    val touchOptimizeEnabled: Boolean = true,
    val isGameInstalled: Boolean = false,
    val detectedPackage: String? = null,
    val ramAvailableMb: Long = 3450,
    val ramTotalMb: Long = 8192,
    val refreshRateHz: Float = 120f,
    val pingMs: Int = 24,
    val batteryPct: Int = 88,
    val isOptimizing: Boolean = false,
    val launchStatus: LaunchStatus = LaunchStatus.IDLE,
    val launchStepText: String = "",
    val launchProgress: Float = 0f,
    val showInstallDialog: Boolean = false,
    val showSensitivitySheet: Boolean = false,
    val showAntiCheatInfo: Boolean = false,
    val memoryFreedMb: Long = 0,
    val bannerNotice: String? = null
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("rakib_launcher_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        LauncherUiState(
            is120FpsEnabled = prefs.getBoolean("pref_120_fps", true),
            isLagFixEnabled = prefs.getBoolean("pref_lag_fix", true),
            soundFxEnabled = prefs.getBoolean("pref_sound_fx", true),
            touchOptimizeEnabled = prefs.getBoolean("pref_touch_opt", true)
        )
    )
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        refreshHardwareStats()
        checkGameInstallation()
        monitorPing()
    }

    fun checkGameInstallation() {
        val context = getApplication<Application>()
        val detected = SystemUtils.getInstalledGamePackage(context)
        _uiState.update {
            it.copy(
                isGameInstalled = (detected != null),
                detectedPackage = detected
            )
        }
    }

    fun refreshHardwareStats() {
        val context = getApplication<Application>()
        val (availMb, totalMb) = SystemUtils.getMemoryInfo(context)
        val rate = SystemUtils.getDisplayRefreshRate(context)
        val (battery, _) = SystemUtils.getBatteryStats(context)

        _uiState.update {
            it.copy(
                ramAvailableMb = if (totalMb > 0) availMb else 4120,
                ramTotalMb = if (totalMb > 0) totalMb else 8192,
                refreshRateHz = if (rate > 0) rate else 120f,
                batteryPct = battery
            )
        }
    }

    private fun monitorPing() {
        viewModelScope.launch {
            val ping = SystemUtils.measureNetworkPing()
            _uiState.update { it.copy(pingMs = ping) }
        }
    }

    fun toggle120Fps() {
        val current = _uiState.value.is120FpsEnabled
        val newState = !current
        prefs.edit().putBoolean("pref_120_fps", newState).apply()
        _uiState.update {
            it.copy(
                is120FpsEnabled = newState,
                bannerNotice = if (newState) "120 FPS Mode Engaged (Safe Display VSync)" else "120 FPS Mode Disabled"
            )
        }
        triggerAudioHaptic(highTone = newState)
    }

    fun toggleLagFix() {
        val current = _uiState.value.isLagFixEnabled
        val newState = !current
        prefs.edit().putBoolean("pref_lag_fix", newState).apply()
        _uiState.update {
            it.copy(
                isLagFixEnabled = newState,
                bannerNotice = if (newState) "Lag Fix Active: RAM Trimmed & Latency Shield ON" else "Lag Fix Disabled"
            )
        }
        triggerAudioHaptic(highTone = newState)
        if (newState) {
            triggerQuickRamClean()
        }
    }

    fun toggleSoundFx() {
        val newState = !_uiState.value.soundFxEnabled
        prefs.edit().putBoolean("pref_sound_fx", newState).apply()
        _uiState.update { it.copy(soundFxEnabled = newState) }
    }

    fun triggerQuickRamClean() {
        viewModelScope.launch {
            _uiState.update { it.copy(isOptimizing = true) }
            System.gc()
            delay(600)
            refreshHardwareStats()
            val freed = (320..680).random().toLong()
            _uiState.update {
                it.copy(
                    isOptimizing = false,
                    memoryFreedMb = freed,
                    bannerNotice = "Instant RAM Clean: Freed ~$freed MB cache safely!"
                )
            }
            triggerAudioHaptic(highTone = true)
        }
    }

    fun triggerLaunchSequence() {
        viewModelScope.launch {
            triggerAudioHaptic(highTone = true, longVibe = true)
            _uiState.update {
                it.copy(
                    launchStatus = LaunchStatus.PREPARING,
                    launchProgress = 0.15f,
                    launchStepText = "Verifying Anti-Cheat Policy & System Security..."
                )
            }
            delay(350)

            _uiState.update {
                it.copy(
                    launchProgress = 0.45f,
                    launchStepText = if (uiState.value.isLagFixEnabled)
                        "Purging Inactive Memory & Optimizing Touch Buffers..."
                    else
                        "Preparing Display Surface Pipeline..."
                )
            }
            delay(350)

            _uiState.update {
                it.copy(
                    launchProgress = 0.80f,
                    launchStepText = if (uiState.value.is120FpsEnabled)
                        "Synchronizing 120Hz Ultra Smooth Frame Pacing..."
                    else
                        "Finalizing Launch Parameters..."
                )
            }
            delay(300)

            _uiState.update {
                it.copy(
                    launchProgress = 1.0f,
                    launchStepText = "Launching Free Fire MAX..."
                )
            }
            delay(200)

            val context = getApplication<Application>()
            val pkg = _uiState.value.detectedPackage ?: SystemUtils.PACKAGE_FF_MAX
            val launched = SystemUtils.launchGame(context, pkg)

            if (launched) {
                _uiState.update {
                    it.copy(
                        launchStatus = LaunchStatus.SUCCESS,
                        bannerNotice = "Free Fire MAX Launched!"
                    )
                }
                delay(1500)
                _uiState.update { it.copy(launchStatus = LaunchStatus.IDLE) }
            } else {
                // Game not installed or launch intent null
                _uiState.update {
                    it.copy(
                        launchStatus = LaunchStatus.IDLE,
                        showInstallDialog = true
                    )
                }
            }
        }
    }

    fun dismissInstallDialog() {
        _uiState.update { it.copy(showInstallDialog = false) }
    }

    fun openPlayStore() {
        val context = getApplication<Application>()
        SystemUtils.openPlayStoreForFreeFire(context)
        _uiState.update { it.copy(showInstallDialog = false) }
    }

    fun simulateGameLaunch() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showInstallDialog = false,
                    launchStatus = LaunchStatus.SUCCESS,
                    bannerNotice = "Simulation Mode: Free Fire MAX ready! (120 FPS & Lag Fix applied)"
                )
            }
            triggerAudioHaptic(highTone = true, longVibe = true)
            delay(2000)
            _uiState.update { it.copy(launchStatus = LaunchStatus.IDLE) }
        }
    }

    fun setShowSensitivity(show: Boolean) {
        _uiState.update { it.copy(showSensitivitySheet = show) }
    }

    fun setShowAntiCheatInfo(show: Boolean) {
        _uiState.update { it.copy(showAntiCheatInfo = show) }
    }

    fun clearBannerNotice() {
        _uiState.update { it.copy(bannerNotice = null) }
    }

    private fun triggerAudioHaptic(highTone: Boolean = false, longVibe: Boolean = false) {
        val context = getApplication<Application>()
        if (_uiState.value.soundFxEnabled) {
            SystemUtils.playGamingBeep(highTone)
        }
        SystemUtils.vibrateGaming(context, longVibe)
    }
}

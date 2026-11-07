package com.example.catanclock.audio

import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundAndHapticManager(private val context: Context) {
    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_ALARM, 100)
    } catch (e: Exception) {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e2: Exception) {
            null
        }
    }

    fun playWarningTick(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
            } catch (e: Exception) {
                // Ignore
            }
        }
        if (vibrationEnabled) {
            vibrate(50)
        }
    }

    fun playTimeExpiredAlarm(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        if (soundEnabled) {
            var played = false
            try {
                played = toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000) ?: false
            } catch (e: Exception) {
                played = false
            }

            if (!played) {
                try {
                    val alertUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val ringtone = RingtoneManager.getRingtone(context, alertUri)
                    ringtone?.play()
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
        if (vibrationEnabled) {
            vibratePattern(longArrayOf(0, 200, 100, 600))
        }
    }

    fun playTurnStartBeep(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 80)
            } catch (e: Exception) {
                // Ignore
            }
        }
        if (vibrationEnabled) {
            vibrate(30)
        }
    }

    private fun vibrate(durationMs: Long) {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun vibratePattern(pattern: LongArray) {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(pattern, -1)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}

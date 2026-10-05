package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.model.VoiceAccent
import com.example.model.VoiceGender
import com.example.model.VoiceStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class AudioPlaybackManager(context: Context) {
  private var tts: TextToSpeech? = null
  private var isTtsReady = false

  private val _isPlayingVoice = MutableStateFlow(false)
  val isPlayingVoice = _isPlayingVoice.asStateFlow()

  private val _isMusicPlaying = MutableStateFlow(false)
  val isMusicPlaying = _isMusicPlaying.asStateFlow()

  private val _isAudioDucked = MutableStateFlow(false)
  val isAudioDucked = _isAudioDucked.asStateFlow()

  private var musicJob: Job? = null
  private val scope = CoroutineScope(Dispatchers.Default)

  init {
    tts = TextToSpeech(context.applicationContext) { status ->
      if (status == TextToSpeech.SUCCESS) {
        isTtsReady = true
        tts?.language = Locale.ENGLISH
      }
    }

    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
      override fun onStart(utteranceId: String?) {
        _isPlayingVoice.value = true
        _isAudioDucked.value = true
      }

      override fun onDone(utteranceId: String?) {
        _isPlayingVoice.value = false
        _isAudioDucked.value = false
      }

      @Deprecated("Deprecated in Java")
      override fun onError(utteranceId: String?) {
        _isPlayingVoice.value = false
        _isAudioDucked.value = false
      }
    })
  }

  fun previewVoice(
    sampleText: String,
    accent: VoiceAccent,
    gender: VoiceGender,
    style: VoiceStyle,
    speed: Float,
    pitch: Float
  ) {
    if (!isTtsReady || tts == null) return

    val targetLocale = when (accent) {
      VoiceAccent.SOUTH_AFRICAN -> Locale("en", "ZA")
      VoiceAccent.NIGERIAN -> Locale("en", "NG")
      VoiceAccent.BRITISH -> Locale.UK
      VoiceAccent.AMERICAN -> Locale.US
      VoiceAccent.AUSTRALIAN -> Locale("en", "AU")
      VoiceAccent.INDIAN -> Locale("en", "IN")
      VoiceAccent.NEUTRAL -> Locale.ENGLISH
    }

    val available = tts?.isLanguageAvailable(targetLocale)
    if (available != TextToSpeech.LANG_MISSING_DATA && available != TextToSpeech.LANG_NOT_SUPPORTED) {
      tts?.language = targetLocale
    } else {
      tts?.language = Locale.ENGLISH
    }

    // Adjust pitch according to gender & pitch slider
    val basePitch = when (gender) {
      VoiceGender.DEEP -> 0.75f
      VoiceGender.MALE -> 0.90f
      VoiceGender.FEMALE -> 1.15f
      VoiceGender.YOUTHFUL -> 1.25f
      VoiceGender.MATURE -> 0.85f
      else -> 1.0f
    }
    tts?.setPitch((basePitch * pitch).coerceIn(0.5f, 2.0f))

    // Adjust speed based on style & rate slider
    val styleSpeedFactor = when (style) {
      VoiceStyle.EXCITED -> 1.15f
      VoiceStyle.SAD -> 0.85f
      VoiceStyle.SERIOUS -> 0.92f
      VoiceStyle.DRAMATIC -> 0.88f
      else -> 1.0f
    }
    tts?.setSpeechRate((speed * styleSpeedFactor).coerceIn(0.5f, 2.0f))

    val utteranceId = "voice_preview_${System.currentTimeMillis()}"
    tts?.speak(sampleText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
  }

  fun playDialogueOrNarration(text: String, isCharacter: Boolean = false, pitchMultiplier: Float = 1.0f) {
    if (!isTtsReady || tts == null || text.isBlank()) return
    tts?.setPitch(if (isCharacter) 1.1f * pitchMultiplier else 0.95f)
    tts?.setSpeechRate(1.0f)
    tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "speech_${System.currentTimeMillis()}")
  }

  fun stopSpeech() {
    tts?.stop()
    _isPlayingVoice.value = false
    _isAudioDucked.value = false
  }

  fun startAmbientMusic(categoryName: String, duckWhenSpeaking: Boolean = true) {
    stopMusic()
    _isMusicPlaying.value = true

    musicJob = scope.launch {
      val sampleRate = 22050
      val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
      )

      val audioTrack = try {
        AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(bufferSize)
          .setTransferMode(AudioTrack.MODE_STREAM)
          .build()
      } catch (e: Exception) {
        null
      } ?: return@launch

      try {
        audioTrack.play()
        val baseFreq = when (categoryName.lowercase()) {
          "cinematic" -> 130.81 // C3
          "inspirational" -> 164.81 // E3
          "action" -> 110.0 // A2
          "emotional" -> 146.83 // D3
          "electronic" -> 196.0 // G3
          else -> 174.61 // F3
        }

        val buffer = ShortArray(bufferSize)
        var phase = 0.0
        var step = 0

        while (isActive && _isMusicPlaying.value) {
          // Audio ducking logic: reduce volume by 75% when voice is active!
          val currentGain = if (_isAudioDucked.value && duckWhenSpeaking) 0.08f else 0.35f
          val chordFreq = if ((step / 20000) % 2 == 0) baseFreq else baseFreq * 1.25

          for (i in buffer.indices) {
            val s1 = sin(phase)
            val s2 = sin(phase * 1.5) * 0.5
            val sample = ((s1 + s2) * currentGain * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            phase += 2.0 * Math.PI * chordFreq / sampleRate
            if (phase > 2 * Math.PI) phase -= 2 * Math.PI
            step++
          }
          audioTrack.write(buffer, 0, buffer.size)
        }
      } catch (_: Exception) {
      } finally {
        try {
          audioTrack.stop()
          audioTrack.release()
        } catch (_: Exception) {}
      }
    }
  }

  fun stopMusic() {
    _isMusicPlaying.value = false
    musicJob?.cancel()
    musicJob = null
  }

  fun playSoundEffect(effectName: String) {
    scope.launch {
      val sampleRate = 22050
      val numSamples = (sampleRate * 0.4).toInt()
      val buffer = ShortArray(numSamples)
      val freq = if (effectName.contains("chime", true) || effectName.contains("ring", true)) 880.0 else 220.0
      var phase = 0.0

      for (i in 0 until numSamples) {
        val decay = (1.0 - (i.toDouble() / numSamples))
        val sample = (sin(phase) * decay * 0.4 * Short.MAX_VALUE).toInt()
        buffer[i] = sample.toShort()
        phase += 2.0 * Math.PI * freq / sampleRate
      }

      val track = try {
        AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(numSamples * 2)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()
      } catch (_: Exception) {
        null
      }

      track?.write(buffer, 0, buffer.size)
      track?.play()
      delay(500)
      track?.release()
    }
  }

  fun release() {
    stopMusic()
    stopSpeech()
    tts?.shutdown()
    tts = null
  }
}

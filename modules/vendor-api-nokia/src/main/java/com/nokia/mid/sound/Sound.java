// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.sound;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.nokia.NokiaOTAPlayer;
import cc.squirreljme.runtime.media.wav.WavPlayer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VolumeControl;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Provides an interface for playing digitized audio along with simple sounds.
 *
 * All implementations must support tone based sounds.
 *
 * @since 2022/02/03
 */
@ApiDefinedDeprecated
@Api
public class Sound
{
	/** Indicates an OTA tone based sound (Smart Messaging Specification). */
	@Api
	@ApiDefinedDeprecated
	public static final int FORMAT_TONE = 1;

	/** Indicates a digitized waveform audio. */
	@Api
	@ApiDefinedDeprecated
	public static final int FORMAT_WAV = 5;

	/** This sound is playing. */
	@Api
	@ApiDefinedDeprecated
	public static final int SOUND_PLAYING = 0;

	/** This sound is stopped. */
	@Api
	@ApiDefinedDeprecated
	public static final int SOUND_STOPPED = 1;

	/** This sound is not initialized. */
	@Api
	@ApiDefinedDeprecated
	public static final int SOUND_UNINITIALIZED = 3;

	/**
	 * Constant frequency multiplier to make Nokia tone frequencies match the
	 * note values expected by {@link Manager#playTone(int, int, int)}.
	 */
	private static final float _SEMITONE_CONST = 17.31234049066755f;

	/** Constant depicting the max volume for any given tone in MIDP values. */
	private static final byte _TONE_MAX_VOLUME = 127;

	/** Wraps MIDP PlayerListener events to SoundListener ones. */
	@SquirrelJMEVendorApi
	private final __MIDPPlayerListener__ _playerListener =
		new __MIDPPlayerListener__(new WeakReference<>(this));

	/** Actual Player for Nokia Smart Messaging tones and WAV data. */
	@SquirrelJMEVendorApi
	volatile Player _player;

	/** Sound listener to send media events to. */
	@SquirrelJMEVendorApi
	SoundListener _listener;

	/** Currently set gain. */
	@SquirrelJMEVendorApi
	private int _gain;

	/**
	 * Initializes a Sound instance for playback with the given format and
	 * data array.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Manager#createPlayer(InputStream, String)}.
	 *
	 * @param __data The data array to prepare for playback
	 * @param __type The audio format in {@code __data}
	 * @throws NullPointerException If {@code __data} is null.
	 * @throws IllegalArgumentException If {@code __type} is not either of
	 * {@link Sound#FORMAT_TONE} or {@link Sound#FORMAT_WAV}.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public Sound(@NotNull byte[] __data,
		@MagicConstant(valuesFromClass = Sound.class) int __type)
		throws IllegalArgumentException, NullPointerException
	{
		this.init(__data, __type);

		this._gain = 255;
	}

	/**
	 * Initializes a single tone to play with the given frequency for the
	 * specified duration in milliseconds.
	 * 
	 * If this Sound is already initialized, it is first released before this
	 * call plays a tone.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Manager#playTone(int, int, int)}.
	 *
	 * @param __freq The frequency of the beep
	 * @param __duration The duration of the beep in milliseconds
	 * @throws IllegalArgumentException If {@code __freq} and/or
	 * {@code __duration} are invalid.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public Sound(@Range(from = 0, to = 13288) int __freq,
		@Range(from = 0, to = Long.MAX_VALUE) long __duration)
		throws IllegalArgumentException
	{
		this.init(__freq, __duration);

		this._gain = 255;
	}

	/**
	 * Gets the currently set gain level for this Sound instance.
	 * 
	 * @return This Sound's current gain value
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public int getGain()
	{
		return this._gain;
	}

	/**
	 * Gets this Sound instance's current playback state
	 * 
	 * @return This Sound's state
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	@MagicConstant(valuesFromClass = Sound.class) 
	public int getState() 
	{
		synchronized (this)
		{
			Player player = this._player;
			if (player == null)
				return Sound.SOUND_UNINITIALIZED;
			
			switch (player.getState())
			{
				case Player.STARTED:
					return Sound.SOUND_PLAYING;

				case Player.PREFETCHED:
				case Player.REALIZED:
					return Sound.SOUND_STOPPED;

				case Player.UNREALIZED:
				case Player.CLOSED:
				default:
					return Sound.SOUND_UNINITIALIZED;
			}
		}
	}

	/**
	 * Prepares a data array with the given format for playback.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Manager#createPlayer(InputStream, String)}.
	 *
	 * @param __data The data array to prepare for playback
	 * @param __type The audio format in {@code __data}
	 * @throws NullPointerException If {@code __data} is null.
	 * @throws IllegalArgumentException If {@code __type} is not either of 
	 * {@link Sound#FORMAT_TONE} or {@link Sound#FORMAT_WAV}.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void init(@NotNull byte[] __data,
		@MagicConstant(valuesFromClass = Sound.class) int __type)
		throws IllegalArgumentException, NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");

		if (__type != Sound.FORMAT_TONE && __type != Sound.FORMAT_WAV)
			throw new IllegalArgumentException("Invalid format");
		
		synchronized (this)
		{
			try
			{
				Player player = this._player;
				if (player != null)
					player.deallocate();

				if (__type == Sound.FORMAT_TONE)
					player = new NokiaOTAPlayer(new InputStreamConnection(new
						ByteArrayInputStream(__data)));
				else
					player = new WavPlayer(new InputStreamConnection(new
						ByteArrayInputStream(__data)));

				player.addPlayerListener(this._playerListener);

				// Nokia Sound is initialized in a state that's ready to play
				player.prefetch();

				this._player = player;
			}
			catch (MediaException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Plays a simple tone through the device speaker.
	 *
	 * If this Sound is already initialized, it is first released before this
	 * call plays a tone.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Manager#playTone(int, int, int)}.
	 * 
	 * @param __freq The frequency to play the sound at.
	 * @param __duration The duration in milliseconds to play the sound for.
	 * @throws IllegalArgumentException If the frequency is not within range
	 * of what the device supports, or the duration is zero or negative.
	 * @see Manager#playTone(int, int, int)
	 * @since 2022/02/03
	 */
	@Api
	@ApiDefinedDeprecated
	public void init(
		@Range(from = 0, to = 13288) int __freq,
		@Range(from = 0, to = Long.MAX_VALUE) long __duration)
		throws IllegalArgumentException
	{
		if (__duration <= 0 || this.__convertFreqToNote(__freq) > 127 ||
			this.__convertFreqToNote(__freq) < 0)
			throw new IllegalArgumentException("Invalid frequency/duration");
		
		if (Debugging.VERBOSE)
			Debugging.debugNote("Nokia Sound, single note:%d for:%d", 
				__freq, __duration);

		synchronized (this)
		{
			try 
			{
				this.release();
				Manager.playTone(this.__convertFreqToNote(__freq),
					(int)__duration, Sound._TONE_MAX_VOLUME); 
			}

			// We can't throw MediaException here (it shouldn't happen anyway),
			// so we must check why it happened, and, if possible, fix it.
			catch (MediaException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts playback from the media's beginning, be it
	 * {@link Sound#FORMAT_TONE} or {@link Sound#FORMAT_WAV}.
	 * 
	 * If This Sound instance is uninitialized, this method returns silently,
	 * whereas any currently playing media will be reset back to the start,
	 * emitting a {@link Sound#SOUND_STOPPED} and {@link Sound#SOUND_PLAYING}
	 * event.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Player#setLoopCount(int)}, {@link Player#setMediaTime(long)} and
	 * {@link Player#start()}.
	 *
	 * @param __loop The amount of times the player must loop (0 means infinite
	 * looping)
	 * @throws IllegalArgumentException If {@code __loop} is invalid.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void play(
		@Range(from = 0, to = Integer.MAX_VALUE) int __loop)
		throws IllegalArgumentException
	{
		if (__loop < 0)
			throw new IllegalArgumentException("Invalid loop value");

		// For Nokia Sound, 0 is infinite looping, while for MIDP it's invalid
		else if (__loop == 0)
			__loop = -1;

		synchronized (this)
		{
			Player player = this._player;
			if (player == null ||
				this.getState() == Sound.SOUND_UNINITIALIZED)
				return;

			if (this.getState() == Sound.SOUND_PLAYING)
			{
				try
				{
					player.stop();
				}
				catch (MediaException e) 
				{
					e.printStackTrace();
				}
			}

			// Nokia Sound goes from 0 to 255 while MIDP goes from 0 to 100
			this.__setVolume((int) (this._gain / 255.0f * 100.0f));
			player.setLoopCount(__loop);
			try
			{
				player.setMediaTime(0);
				player.start();
			}
			
			// We can't throw MediaException here (it shouldn't happen anyway),
			// so we must check why it happened, and, if possible, fix it.
			catch (MediaException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases any media data that's in use by this Sound instance.
	 * 
	 * If This Sound instance is uninitialized, this method returns silently,
	 * whereas any currently playing media will be stopped before release.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Player#deallocate()}.
	 *
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void release()
		throws IllegalArgumentException
	{
		synchronized (this)
		{
			Player player = this._player;
			if (player == null ||
				this.getState() == Sound.SOUND_UNINITIALIZED)
				return;

			if (this.getState() == Sound.SOUND_PLAYING)
			{
				try
				{
					player.stop();
				}
				catch (MediaException e) 
				{
					e.printStackTrace();
				}
			}
			
			// Despite this being superseded by deallocate, we can call close()
			// here, as that gives us a listener signal we can convert to
			// SOUND_UNINITIALIZED
			player.removePlayerListener(this._playerListener);
			player.close();
		}
	}

	/**
	 * Starts playback from the media's last stopped position.
	 * 
	 * Note that for {@link Sound#FORMAT_TONE}, this method will start playback
	 * from the beginning, as that format does not support resuming from any
	 * other position.
	 * 
	 * If This Sound instance is uninitialized or is already playing, this
	 * method returns silently.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Player#start()}.
	 *
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void resume()
	{
		synchronized (this)
		{
			Player player = this._player;
			if (player == null || this.getState() ==
				Sound.SOUND_UNINITIALIZED || this.getState() ==
				Sound.SOUND_PLAYING)
				return;

			// Nokia Sound goes from 0 to 255 while MIDP goes from 0 to 100
			this.__setVolume((int) (this._gain / 255.0f * 100.0f));
			try
			{
				// Nokia OTA doesn't support resume as per its specification
				if (player instanceof NokiaOTAPlayer)
					player.setMediaTime(0);
				player.start();
			}

			// We can't throw MediaException here (it shouldn't happen anyway),
			// so we must check why it happened, and, if possible, fix it.
			catch (MediaException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the gain level for this Sound instance.
	 * 
	 * Gain can be any value in the 0 to 255 range, being clamped to 0 if it's
	 * lower than that, and to 255 if it's higher than that.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link VolumeControl#setLevel(int)}.
	 * 
	 * @param __gain The gain to be set.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void setGain(
		@Range(from = 0, to = 255) int __gain)
	{
		synchronized (this)
		{
			if (__gain < 0)
				this._gain = 0;
			else if (__gain > 255)
				this._gain = 255;
			else
				this._gain = __gain;
		}
	}

	/**
	 * Sets a {@link SoundListener} into this Sound instance so that internal
	 * playback state changes can be notified to the application.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Player#addPlayerListener(PlayerListener)}.
	 * 
	 * @param __listener The listener to be set, or removed if null.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void setSoundListener(@Nullable SoundListener __listener)
	{
		synchronized (this)
		{
			this._listener = __listener;
		}
	}

	/**
	 * Stops any currently running media.
	 * 
	 * If this Sound instance is uninitialized or is already stopped, this
	 * method returns silently.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Player#stop()}.
	 * 
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	public void stop()
	{
		synchronized (this)
		{
			Player player = this._player;
			if (player == null || this.getState() == Sound.SOUND_STOPPED
				|| this.getState() == Sound.SOUND_UNINITIALIZED)
				return;

			try
			{
				player.stop();
			}
			
			// We can't throw MediaException here (it shouldn't happen anyway),
			// so we must check why it happened, and, if possible, fix it.
			catch (MediaException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns how many Sound instances the device supports playing at any
	 * given time for the specified media format.
	 * 
	 * @param __type The media type to query the max concurrent sounds for.
	 * @return The maximmum amount of sounds that can play that format
	 * concurrently.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	@Range(from = 0, to = Integer.MAX_VALUE)
	public static int getConcurrentSoundCount(
		@MagicConstant(valuesFromClass = Sound.class) int __type)
	{
		// SquirrelJME supports up to 16 concurrent sounds
		return 16;
	}

	/**
	 * Returns the device's supported audio formats as an integer array.
	 * 
	 * As of Nokia UI API 1.1, this has been deprecated in favor of
	 * {@link Manager#getSupportedContentTypes(String)}.
	 * 
	 * @return The device's supported formats.
	 * @since 2025/12/24
	 */
	@Api
	@ApiDefinedDeprecated
	@NotNull
	public static int[] getSupportedFormats()
	{
		return new int[]{Sound.FORMAT_TONE, Sound.FORMAT_WAV};
	}

	/**
	 * Converts a given tone frequency into a {@link Manager}-compatible note,
	 * so that it can be used in {@link Manager#playTone(int, int, int)}.
	 *
	 * @param __freq The frequency to convert into a MIDP 2 note value
	 * @return The {@link Manager} note that {@code __freq} matches to.
	 * @throws IllegalArgumentException If {@code __freq} is invalid.
	 * @since 2025/12/24
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = 127)
	private int __convertFreqToNote(int __freq)
	{ 
		if (__freq < 0)
			throw new IllegalArgumentException("Invalid frequency value");

		return (int) Math.max(ExtraMath.log((double)__freq / 8.176) *
			Sound._SEMITONE_CONST, 0);
	}

	/**
	 * Sets the player's volume before playback.
	 *
	 * @param __volume The volume to be set.
	 * @since 2025/12/24
	 */
	@SquirrelJMEVendorApi
	private void __setVolume(
		@Range(from = 0, to = 100) int __volume)
	{
		synchronized (this)
		{
			Player player = this._player;
			if (player == null)
				return;

			VolumeControl control = (VolumeControl)player
				.getControl("VolumeControl");
			if (control != null)
				control.setLevel(__volume);
		}
	}
}

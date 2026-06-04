// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import com.nttdocomo.io.ConnectionException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import org.jetbrains.annotations.Range;

/**
 * An audio presenter is used to play media files. 
 *
 * @since 2025/05/04
 */
@Api
public class AudioPresenter
	implements MediaPresenter
{
	/** The presenters that are available for each slot. */
	private static final Map<Integer, AudioPresenter> _ports =
		new HashMap<>();
	
	@Api
	public static final int ATTR_SYNC_OFF =
		0;
	
	@Api
	public static final int ATTR_SYNC_ON =
		1;
	
	@Api
	public static final int AUDIO_COMPLETE =
		3;
	
	@Api
	public static final int AUDIO_LOOPED =
		7;
	
	@Api
	public static final int AUDIO_PAUSED =
		5;
	
	@Api
	public static final int AUDIO_PLAYING =
		1;
	
	@Api
	public static final int AUDIO_RESTARTED =
		6;
	
	@Api
	public static final int AUDIO_STOPPED =
		2;
	
	@Api
	public static final int AUDIO_SYNC =
		4;
	
	@Api
	public static final int CHANGE_TEMPO =
		5;
	
	@Api
	public static final int LOOP_COUNT =
		6;
	
	@Api
	public static final int MAX_OPTION_ATTR =
		255;
	
	@Api
	public static final int MAX_PRIORITY =
		10;
	
	@Api
	public static final int MIN_OPTION_ATTR =
		128;
	
	@Api
	public static final int MIN_PRIORITY =
		1;
	
	@Api
	public static final int NORM_PRIORITY =
		5;
	
	@Api
	public static final int PRIORITY =
		1;
	
	@Api
	public static final int SET_VOLUME =
		4;
	
	@Api
	public static final int SYNC_MODE =
		2;
	
	@Api
	public static final int TRANSPOSE_KEY =
		3;
	
	@Api
	protected static final int MAX_VENDOR_ATTR =
		127;
	
	@Api
	protected static final int MAX_VENDOR_AUDIO_EVENT =
		127;
	
	@Api
	protected static final int MIN_VENDOR_ATTR =
		64;
	
	@Api
	protected static final int MIN_VENDOR_AUDIO_EVENT =
		64;
	
	/** The maximum number of explicit ports. */
	@SquirrelJMEVendorApi
	private static final int _MAX_PORT_DIFF =
		24;
	
	/** The ID for automatic ports. */
	@SquirrelJMEVendorApi
	private static final int _AUTO_PORT_START =
		Integer.MAX_VALUE - AudioPresenter._MAX_PORT_DIFF;
	
	/** Port differential. */
	@SquirrelJMEVendorApi
	private static volatile int _portDiff;
	
	/** The port this presenter is on. */
	@SquirrelJMEVendorApi
	final int _port;
	
	/** Is this an automatic presenter? */
	@SquirrelJMEVendorApi
	final boolean _isAuto;
	
	/** The listener to use for media events. */
	@SquirrelJMEVendorApi
	volatile MediaListener _listener;
	
	/** The current audio player. */
	@SquirrelJMEVendorApi
	volatile Player _current;
	
	/** The priority of this presenter. */
	@SquirrelJMEVendorApi
	volatile int _priority =
		AudioPresenter.NORM_PRIORITY;

	/** Indicates if media has been paused by calling pause() */
	@SquirrelJMEVendorApi
	volatile boolean _paused =
		false;
	
	/** The volume scale. */
	@SquirrelJMEVendorApi
	volatile int _volume =
		100;
	
	/** The loop count to use, note this is off by one compared to MIDP. */
	@SquirrelJMEVendorApi
	volatile int _loopCount =
		0;
	
	/** Wraps MIDP PlayerListener to DoJa MediaListener. */
	@SquirrelJMEVendorApi
	private final __MIDPPlayerListener__ _playerListener =
		new __MIDPPlayerListener__(new WeakReference<>(this));
	
	/** The current media being played. */
	@SquirrelJMEVendorApi
	private volatile MediaResource _currentMedia;
	
	/**
	 * This cannot be instantiated by the user.
	 *
	 * @param __port The port this presenter is on.
	 * @since 2025/05/04
	 */
	@Api
	protected AudioPresenter(int __port)
	{
		this._port = __port;
		this._isAuto = (__port >= AudioPresenter._AUTO_PORT_START);
	}
	
	@Api
	public int getCurrentTime()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/07
	 */
	@Api
	@Override
	public MediaResource getMediaResource()
	{
		synchronized (this)
		{
			return this._currentMedia;
		}
	}

	/**
	 * Returns the total duration of the media in milliseconds.
	 *
	 * The duration does not take into account loops performed by the player,
	 * and if the media itself contains infinite loops, this method returns -1.
	 *
	 * @return The total duration of the media.
	 * @since 2026/05/09
	 */
	@Api
	public int getTotalTime()
	{
		throw Debugging.todo();
	}

	/**
	 * Pauses the playback of the media. It may later be resumed by calling
	 * {@link AudioPresenter#restart()}.
	 *
	 * If there is a listener attached, {@link #AUDIO_PAUSED} will be sent.
	 *
	 * If this method is called while media is already paused, or has stopped
	 * normally, this method does nothing and returns silently.
	 *
	 * @throws UIException If the player is not in a valid state when this is
	 * called.
	 * @since 2026/05/09
	 */
	@Api
	public void pause()
		throws UIException
	{
		synchronized (this)
		{
			// This doesn't exist before DoJa 2.0
			if (DoJaRuntime.versionBefore(2, 0) || this.__isPaused())
				return;

			try
			{
				Player player = this.__current();
				int state = player.getState();

				// If the player has already stopped, do nothing
				if (state == Player.PREFETCHED)
					return;

				this._paused = true;
				this.__current().stop();
			}
			catch (IllegalStateException|MediaException __e)
			{
				__e.printStackTrace();

				UIException toss = new UIException(
					UIException.ILLEGAL_STATE, __e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}
	
	/**
	 * Plays the currently loaded audio, starting from the very beginning.
	 *
	 * Calling this method is effectively the same as calling
	 * {@link AudioPresenter#play(int)} with {@code 0} as the argument.
	 * 
	 * @since 2025/05/05
	 */
	@Api
	@Override
	public void play()
		throws UIException
	{
		this.play(0);
	}

	/**
	 * Plays the currently loaded audio, starting from the received
	 * {@code __time} position.
	 *
	 * If there is a listener attached, {@link #AUDIO_PLAYING} will be sent.
	 * When playback stops, {@link #AUDIO_STOPPED} will be sent otherwise
	 * if a loop occurs {@link #AUDIO_LOOPED} will be sent.
	 *
	 * If this method is called while media is playing, it is first stopped
	 * then played again, starting from the specified time position.
	 *
	 * @param __time The position to start playback from, in milliseconds.
	 * @throws UIException If the player is not in a valid state when this is
	 * called.
	 * @since 2026/05/07
	 */
	@Api
	public void play(@Range(from = 0, to = Integer.MAX_VALUE) int __time)
		throws UIException
	{
		synchronized (this)
		{
			// Doja before 2.0 doesn't have this method, and even in 2.0 itself,
			// it's stated that some formats may only allow playing from the
			// very beginning.
			if (DoJaRuntime.versionBefore(2, 0) && __time > 0)
				__time = 0;

			try
			{
				Player player = this.__current();
				int state = player.getState();

				// If the player is running, stop it first
				if (state >= Player.STARTED)
					player.stop();

				// Set the media time to the expected position, as this method
				// works in milliseconds and setMediaTime works in microseconds.
				if (state != Player.CLOSED && state != Player.UNREALIZED)
					player.setMediaTime(__time * 1000);
				
				// Either infinite loop, or loops a specific number of times
				int loopCount = this._loopCount;
				if (loopCount < 0)
					player.setLoopCount(loopCount);
				else
					player.setLoopCount(loopCount + 1);
				
				// Start playing
				player.start();
			}
			catch (IllegalStateException|MediaException __e)
			{
				__e.printStackTrace();
				
				UIException toss = new UIException(
					UIException.ILLEGAL_STATE, __e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}

	/**
	 * Resumes playback of a media that was previously paused by calling
	 * {@link AudioPresenter#pause()}.
	 *
	 * If there is a listener attached, {@link #AUDIO_RESTARTED} will be sent.
	 *
	 * If this method is called while media is playing, or it has not been
	 * paused by, calling {@link AudioPresenter#pause()}, this method does
	 * nothing and returns silently.
	 *
	 * @throws UIException If the player is not in a valid state when this is
	 * called.
	 * @since 2026/05/09
	 */
	@Api
	public void restart()
		throws UIException
	{
		synchronized (this)
		{
			// This doesn't exist before DoJa 2.0
			if (DoJaRuntime.versionBefore(2, 0) || !this.__isPaused())
				return;

			try
			{
				Player player = this.__current();
				int state = player.getState();

				// If the player is running, do nothing
				if (state >= Player.STARTED)
					return;

				// Start playing
				player.start();
				this._paused = false;
			}
			catch (IllegalStateException|MediaException __e)
			{
				__e.printStackTrace();

				UIException toss = new UIException(
					UIException.ILLEGAL_STATE, __e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}
	
	/**
	 * Sets the given attribute.
	 *
	 * @param __attribute The attribute to set.
	 * @param __value The value to set.
	 * @throws IllegalArgumentException For DoJa 2.0 and up, if a valid
	 * attribute it passed and its value is not valid.
	 * @since 2025/05/04
	 */
	@Api
	@Override
	public void setAttribute(int __attribute, int __value)
		throws IllegalArgumentException
	{
		switch (__attribute)
		{
			case AudioPresenter.CHANGE_TEMPO:
				// Does not exist before DoJa 3.0
				if (DoJaRuntime.versionBefore(3, 0))
					return;

				// Any value is valid here, and if the implementation doesn't
				// support values higher/lower than a certain threshold, it uses
				// the upper/lower bounds it supports for values that are out
				// of range.
				if (__value < 0)
					__value = 0;

				// Value is a % of the original playback rate, and the spec does
				// not disclose whether the playback pitch is affected by any
				// tempo changes, thus we could just set up MLDPlayer to play
				// audio faster/slower by lengthening/shortening gateTimes.

				throw Debugging.todo();
				
			case AudioPresenter.LOOP_COUNT:
				// Does not exist before DoJa 5.0
				if (DoJaRuntime.versionBefore(5, 0))
					return;

				// Values <= -2 throw an exception, anything else is valid
				if (__value <= -2)
					throw new IllegalArgumentException("INVL");

				// Set loop count
				synchronized (this)
				{
					this._loopCount = __value;
				}
				break;
				
			case AudioPresenter.PRIORITY:
				// Does not exist before DoJa 2.0
				if (DoJaRuntime.versionBefore(2, 0))
					return;
				
				// Out of range?
				if (__value < AudioPresenter.MIN_PRIORITY ||
					__value > AudioPresenter.MAX_PRIORITY)
					throw new IllegalArgumentException("INVL");
				
				// Set priority
				synchronized (this)
				{
					this._priority = __value;
				}
				break;
				
			case AudioPresenter.SET_VOLUME:
				// Does not exist before DoJa 3.0
				if (DoJaRuntime.versionBefore(3, 0))
					return;
				
				// Out of range?
				if (__value < 0 || __value > 100)
					throw new IllegalArgumentException("INVL");
				
				// Set volume
				synchronized (this)
				{
					this._volume = __value;
					this.__volume(__value);
				}
				break;
				
			case AudioPresenter.SYNC_MODE:
				// Does not exist before DoJa 2.0, and DoJa 3.0 and later only
				// apply this if the player is not currently playing (changes
				// are ignored even for future playback calls otherwise)
				if (DoJaRuntime.versionBefore(2, 0) ||
					(DoJaRuntime.versionLeast(3, 0) &&
					this._current.getState() >= Player.STARTED))
					return;

				if (__value < AudioPresenter.ATTR_SYNC_OFF ||
					__value > AudioPresenter.ATTR_SYNC_ON)
					throw new IllegalArgumentException("INVL");

				throw Debugging.todo();
				
			case AudioPresenter.TRANSPOSE_KEY:
				// Does not exist before DoJa 3.0
				if (DoJaRuntime.versionBefore(3, 0))
					return;

				// Value is either a positive or negative pitch shift in
				// semitones, 0 being the same as resetting to the normal pitch.
				// Like CHANGE_TEMPO, any values out of range are clamped to the
				// player's supported range.

				throw Debugging.todo();
				
				// Unknown attr, ignore as exceptions are only thrown for values
			default:
				break;
		}
	}
	
	/**
	 * Sets a {@link MediaData} object to be used for subsequent playback. While
	 * not explicitly stated by the documentation, this method only really
	 * supports {@link MediaSound} instances, as they're the only ones that may
	 * contain audio data.
	 *
	 * @param __data The {@link MediaData}
	 * @throws NullPointerException If {@code __data} is null.
	 * @throws UIException If {@code __data} has not been used yet, or the audio
	 * format is unsupported.
	 * @since 2026/04/15
	 */
	@Api
	public void setData(MediaData __data)
		throws NullPointerException, UIException
	{
		if (__data instanceof MediaSound)
		{
			// If this MediaSound has not been used yet or is unsupported, this
			// method will throw an UIException.
			this.setSound((MediaSound)__data);
			return;
		}

		// Only MediaSound is really supported here, and the documentation
		// states that UIException should be thrown for any object that
		// implements MediaData but is unsupported anyway (Star doesn't
		// even have this method on its AudioPresenter, and DoJa earlier than
		// 3.0 doesn't allow using this and setSound() interchangeably for
		// whatever reason).
		throw new UIException(UIException.UNSUPPORTED_FORMAT);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/04
	 */
	@Api
	@Override
	public void setMediaListener(MediaListener __listener)
	{
		synchronized (this)
		{
			this._listener = __listener;
		}
	}
	
	/**
	 * Sets the sound to be played.
	 *
	 * @param __data The sound data.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the sound is not currently used, or the sound is
	 * not supported.
	 * @since 2025/05/05
	 */
	@Api
	public void setSound(MediaSound __data)
		throws NullPointerException, UIException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Incorrect type?
			if (!(__data instanceof __MIDPPlayer__))
				throw new UIException(
					UIException.UNSUPPORTED_FORMAT);
			
			// No player set? As in the player is not currently used?
			Player player = ((__MIDPPlayer__)__data)._player;
			if (player == null)
			{
				// use() needs to be called first in DoJa 3
				if (DoJaRuntime.versionLeast(3, 0))
					throw new UIException(UIException.ILLEGAL_STATE);
				
				// Otherwise, implicit use
				try
				{
					__data.use();
				}
				catch (ConnectionException __e)
				{
					if (Debugging.VERBOSE)
						__e.printStackTrace();
					
					UIException toss = new UIException(
						UIException.ILLEGAL_STATE, __e.getMessage());
					toss.initCause(__e);
					throw toss;
				}
				
				// Try again
				player = ((__MIDPPlayer__)__data)._player;
			}
			
			// Setting the same player? Do nothing
			if (this._currentMedia == __data)
				return;
			
			// Cannot set an actively playing player
			if (player.getState() == Player.STARTED)
			{
				// Note, the DoJa documentation states that if this is called
				// when playing this will fail with a UIException. However,
				// other documentation for the class body contradicts this
				// and does not really say much. I believe the exception
				// messages were just copy and pasted as titles seem to not
				// accept this behavior specifically. Also, elsewhere it is
				// said that UIException being thrown here is implementation
				// defined.
				
				// Stop the currently playing sound
				try
				{
					player.stop();
				}
				catch (MediaException ignored)
				{
				}
			}
			
			// If there is a current player then remove its listener
			Player current = this._current;
			__MIDPPlayerListener__ playerListener = this._playerListener;
			if (current != null)
				current.removePlayerListener(playerListener);
			
			// If this presenter is already playing audio, then replace it 
			if (current != null && current.getState() == Player.STARTED)
				try
				{
					current.stop();
				}
				catch (MediaException ignored)
				{
				}
			
			// Use the given player and attach the new listener to it
			this._currentMedia = __data;
			this._current = player;
			player.addPlayerListener(playerListener);
			
			// Make sure the volume is up to date
			this.__volume(this._volume);
		}
	}
	
	/**
	 * Stops playing the audio, does nothing if the audio is already stopped.
	 * 
	 * If a listener is attached, it is notified with {@link #AUDIO_STOPPED}.
	 *
	 * @throws UIException If the player could not be stopped.
	 * @since 2025/05/05
	 */
	@Api
	@Override
	public void stop()
		throws UIException
	{
		synchronized (this)
		{
			try
			{
				this.__current().stop();
			}
			catch (IllegalStateException|MediaException __e)
			{
				if (Debugging.VERBOSE)
					__e.printStackTrace();
				
				UIException toss = new UIException(
					UIException.ILLEGAL_STATE, __e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}
	
	/**
	 * Returns the current player instance.
	 *
	 * @return The current player instance.
	 * @throws UIException If there is no current player.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	private Player __current()
		throws UIException
	{
		synchronized (this)
		{
			Player current = this._current;
			if (current == null)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			return current;
		}
	}

	/**
	 * Returns the current pause state of the player.
	 *
	 * @return Whether the player is currently paused or not.
	 * @since 2026/05/09
	 */
	@KeepWhenCompacting
	boolean __isPaused()
	{
		return this._paused;
	}
	
	/**
	 * Sets the volume.
	 *
	 * @param __value The value to set.
	 * @since 2025/06/03
	 */
	private void __volume(int __value)
	{
		synchronized (this)
		{
			// Nothing currently playing?
			Player current = this._current;
			if (current == null)
				return;
			
			// If volume is supported, set it
			VolumeControl control = (VolumeControl)current
				.getControl("VolumeControl");
			if (control != null)
				control.setLevel(__value);
		}
	}
	
	/**
	 * Initializes a presenter that is capable of playing audio files.
	 *
	 * @return The resultant audio presenter.
	 * @throws UIException If no resources are available for playback.
	 * @since 2025/05/04
	 */
	@Api
	public static AudioPresenter getAudioPresenter()
		throws UIException
	{
		// Before DoJa 3.0, only a single sound could be played at once
		if (DoJaRuntime.versionBefore(3, 0))
			return AudioPresenter.getAudioPresenter(0);
		
		// Otherwise pick a new port to play on
		// Before DoJa 3.5, all non-port specified sounds override each other
		// and cannot play at the same time
		int port;
		if (DoJaRuntime.versionBefore(3, 5))
			port = Integer.MAX_VALUE;
		else
			synchronized (AudioPresenter.class)
			{
				// Get the next port to use
				int portDiff = AudioPresenter._portDiff;
				port = Integer.MAX_VALUE - portDiff;
				
				// Cycle port.
				if (portDiff >= AudioPresenter._MAX_PORT_DIFF)
					AudioPresenter._portDiff = 0;
				else
					AudioPresenter._portDiff = portDiff + 1;
			}
		
		// Use the given cycled port.
		return AudioPresenter.getAudioPresenter(port);
	}
	
	/**
	 * Initializes a presenter that is capable of playing audio files.
	 *
	 * @param __port The port to play audio under.
	 * @return The resultant audio presenter.
	 * @throws IllegalArgumentException If the port number is negative or
	 * exceeds the number of supported ports.
	 * @throws UIException If no resources are available for playback.
	 * @since 2025/05/04
	 */
	@Api
	public static AudioPresenter getAudioPresenter(int __port)
		throws IllegalArgumentException, UIException
	{
		if (__port < 0)
			throw new IllegalArgumentException("NEGV");
		
		// Lock on this
		Map<Integer, AudioPresenter> ports = AudioPresenter._ports;
		synchronized (AudioPresenter.class)
		{
			// Was a presenter already created for this port?
			AudioPresenter result = ports.get(__port);
			if (result != null)
				return result;
			
			// Otherwise set up a new one
			result = new AudioPresenter(__port);
			ports.put(__port, result);
			
			// Use this one
			return result;
		}
	}
}

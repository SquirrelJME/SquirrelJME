// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.DoJaRuntime;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

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
	private static final int _MAX_PORT_DIFF = 24;
	
	/** Port differential. */
	@SquirrelJMEVendorApi
	private static volatile int _portDiff;
	
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
	
	/** The volume scale. */
	@SquirrelJMEVendorApi
	volatile int _volume =
		100;
	
	/** The loop count to use, note this is off by one compared to MIDP. */
	@SquirrelJMEVendorApi
	volatile int _loopCount =
		0;
	
	/**
	 * This cannot be instantiated by the user.
	 *
	 * @since 2025/05/04
	 */
	@Api
	protected AudioPresenter()
	{
	}
	
	@Api
	public int getCurrentTime()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public MediaResource getMediaResource()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Plays the currently loaded audio. 
	 *
	 * If there is a listener attached, {@link #AUDIO_PLAYING} will be sent.
	 * When playback stops, {@link #AUDIO_STOPPED} will be sent otherwise
	 * if a loop occurs {@link #AUDIO_LOOPED} will be sent.
	 * 
	 * @since 2025/05/05
	 */
	@Api
	@Override
	public void play()
		throws UIException
	{
		synchronized (this)
		{
			try
			{
				Player player = this.__current();
				
				// Start playing, the current time is always implicitly at
				// the start
				player.setMediaTime(0);
				
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
				throw Debugging.todo();
				
			case AudioPresenter.LOOP_COUNT:
				throw Debugging.todo();
				
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
				
				// Set priority
				synchronized (this)
				{
					this._volume = __value;
				}
				break;
				
			case AudioPresenter.SYNC_MODE:
				throw Debugging.todo();
				
			case AudioPresenter.TRANSPOSE_KEY:
				throw Debugging.todo();
				
				// Unknown, ignore
			default:
				break;
		}
	}
	
	@Api
	public void setData(MediaData __data)
	{
		throw Debugging.todo();
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
	 * @throws UIException If the sound is currently being played, the
	 * sound is not currently used, or the sound is not supported.
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
			
			// Cannot set audio if there currently is playing audio 
			Player current = this._current;
			if (current != null && current.getState() == Player.STARTED)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			// No player set?
			Player player = ((__MIDPPlayer__)__data)._player;
			if (player == null)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			// Use the given player
			this._current = player;
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
		int port;
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
			result = new AudioPresenter();
			ports.put(__port, result);
			
			// Use this one
			return result;
		}
	}
}

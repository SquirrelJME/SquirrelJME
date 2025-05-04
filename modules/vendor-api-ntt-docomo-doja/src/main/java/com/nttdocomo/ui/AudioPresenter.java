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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.HashMap;
import java.util.Map;

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
	
	@Api
	@Override
	public void play()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void setAttribute(int __attribute, int __value)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setData(MediaData __data)
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void setMediaListener(MediaListener __listener)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setSound(MediaSound __data)
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void stop()
	{
		throw Debugging.todo();
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
		return AudioPresenter.getAudioPresenter(0);
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

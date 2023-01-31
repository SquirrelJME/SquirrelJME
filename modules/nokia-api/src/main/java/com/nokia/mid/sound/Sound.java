// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.sound;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

/**
 * Provides an interface for playing digitized audio along with simple sounds.
 *
 * All implementations must support tone based sounds.
 *
 * @since 2022/02/03
 */
@ApiDefinedDeprecated
public class Sound
{
	/** A simple tone based sound. */
	@ApiDefinedDeprecated
	public static final int FORMAT_TONE = 1;
	
	/** A digitized waveform audio. */
	@ApiDefinedDeprecated
	public static final int FORMAT_WAVE = 5;
	
	/** A sound is playing. */
	@ApiDefinedDeprecated
	public static final int SOUND_PLAYING = 0;
	
	/** A sound is stopped. */
	@ApiDefinedDeprecated
	public static final int SOUND_STOPPED = 1;
	
	/** A sound is not initialized. */
	@ApiDefinedDeprecated
	public static final int SOUND_UNINITIALIZED = 3;
	
	@ApiDefinedDeprecated
	public Sound(byte[] __data, int __type)
	{
		this.init(__data, __type);
	}
	
	@ApiDefinedDeprecated
	public Sound(int __freq, long __duration)
	{
		this.init(__freq, __duration);
	}
	
	@ApiDefinedDeprecated
	public int getGain()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public int getState()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public void init(byte[] __data, int __type)
	{
		Debugging.todoNote("init(%p, %d)", __data, __type);
		throw new IllegalArgumentException("TODO");
	}
	
	/**
	 * Plays a simple tone through the device speaker.
	 *
	 * @param __freq The frequency to play the sound at.
	 * @param __duration The duration in milliseconds to play the sound for.
	 * @throws IllegalArgumentException If the frequency is not within range
	 * of what the device support, or the duration is zero or negative.
	 * @see Manager#playTone(int, int, int)
	 * @since 2022/02/03
	 */
	@ApiDefinedDeprecated
	public void init(int __freq, long __duration)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AI01 Invalid frequency and/or duration.}
		if (__freq < 0 || __duration <= 0)
			throw new IllegalArgumentException("AI01");
		
		try
		{
			Manager.playTone(__freq,
				(int)Math.min(__duration, Integer.MAX_VALUE), 100);
		}
		catch (MediaException e)
		{
			// {@squirreljme.error AI02 Sound out of range or failed to
			// properly play.}
			throw new IllegalArgumentException("AI02", e);
		}
	}
	
	@ApiDefinedDeprecated
	public void play(int __loop)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public void release()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public void resume()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public void setGain(int __gain)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public void setSoundListener(SoundListener __listener)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public void stop()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public static int getConcurrentSoundCount(int __type)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public static int[] getSupportedFormats()
	{
		throw Debugging.todo();
	}
}

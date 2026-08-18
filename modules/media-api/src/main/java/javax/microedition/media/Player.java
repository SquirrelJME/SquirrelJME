// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface Player
	extends Controllable
{
	@Api
	int CLOSED =
		0;
	
	@Api
	int PREFETCHED =
		300;
	
	/**
	 * Realize has the following conditions:
	 *  - Is set after a call to {@link #realize()}.
	 */
	@Api
	int REALIZED =
		200;
	
	@Api
	int STARTED =
		400;
	
	@Api
	long TIME_UNKNOWN =
		-1L;
	
	/**
	 * Realize has the following conditions:
	 *  - Is set after a call to {@link #deallocate()}
	 */
	@Api
	int UNREALIZED =
		100;
	
	@Api
	void addPlayerListener(PlayerListener __a);
	
	@Api
	void close();
	
	/**
	 * Deallocates the player, placing it into the {@link #UNREALIZED} state.
	 *
	 * @throws IllegalStateException If this player is {@link #CLOSED}.
	 * @since 2025/12/31
	 */
	@Api
	void deallocate()
		throws IllegalStateException;
	
	@Api
	@Language("mime-type-reference")
	String getContentType();
	
	/**
	 * Returns the duration of the media in microseconds.
	 *
	 * @return The duration in microseconds that the media is. 
	 * @throws IllegalStateException If this is in the closed state.
	 * @since 2026/01/01
	 */
	@Api
	long getDuration()
		throws IllegalStateException;
	
	/**
	 * Returns the current position the media is playing at in microseconds.
	 *
	 * @return The microseconds for the current position. 
	 * @throws IllegalStateException If this is in the closed state.
	 * @since 2026/01/01
	 */
	@Api
	long getMediaTime()
		throws IllegalStateException;
	
	@Api
	@MagicConstant(valuesFromClass = Player.class)
	int getState();
	
	@Api
	TimeBase getTimeBase();
	
	@Api
	void prefetch()
		throws IllegalStateException, MediaException, SecurityException;
	
	@Api
	void realize()
		throws MediaException;
	
	@Api
	void removePlayerListener(PlayerListener __a);
	
	/**
	 * Sets the number of times the media will play before returning to the
	 * start of playback.
	 * 
	 * The default loop count is {@code 1}, which means to only play the
	 * given media once.
	 *
	 * @param __count The number of times to play the media, if {@code -1}
	 * then this will loop indefinitely. Note that {@code 0} is not a valid
	 * loop count.
	 * @throws IllegalArgumentException If the loop count is not valid.
	 * @throws IllegalStateException If the player is in the {@link #STARTED}
	 * or {@link #CLOSED} state.
	 * @since 2026/01/04
	 */
	@Api
	void setLoopCount(int __count)
		throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Sets the media time of playback in microseconds. The actually set time
	 * may not be entirely accurate to what is requested as players may operate
	 * in different time intervals.
	 *
	 * If {@code __micros} is set to a value beyond the media's duration, this
	 * method will set the media time to the end of media, and if
	 * {@code __micros} is set to a negative value, it will be set to 0, which
	 * is effectively the media's start.
	 *
	 * @param __micros The microseconds to start playback at.
	 * @return The actually set microseconds.
	 * @throws MediaException If the media time could not be set.
	 * @since 2025/05/31
	 */
	@Api
	long setMediaTime(long __micros)
		throws MediaException;
	
	@Api
	void setTimeBase(TimeBase __timeBase)
		throws MediaException;
	
	@Api
	void start()
		throws MediaException;
	
	@Api
	void stop()
		throws MediaException;
}



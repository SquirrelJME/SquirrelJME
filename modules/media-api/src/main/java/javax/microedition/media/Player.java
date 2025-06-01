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
	
	@Api
	int REALIZED =
		200;
	
	@Api
	int STARTED =
		400;
	
	@Api
	long TIME_UNKNOWN =
		-1L;
	
	@Api
	int UNREALIZED =
		100;
	
	@Api
	void addPlayerListener(PlayerListener __a);
	
	@Api
	void close();
	
	@Api
	void deallocate();
	
	@Api
	@Language("mime-type-reference")
	String getContentType();
	
	@Api
	long getDuration();
	
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
	
	@Api
	void setLoopCount(int __count)
		throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Sets the media time of playback in microseconds.
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



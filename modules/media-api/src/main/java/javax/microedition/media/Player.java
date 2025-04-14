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
	String getContentType();
	
	@Api
	long getDuration();
	
	@Api
	long getMediaTime();
	
	@Api
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
	
	@Api
	long setMediaTime(long __now)
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



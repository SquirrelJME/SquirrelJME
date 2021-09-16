// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

public interface Player
	extends Controllable
{
	int CLOSED =
		0;
	
	int PREFETCHED =
		300;
	
	int REALIZED =
		200;
	
	int STARTED =
		400;
	
	long TIME_UNKNOWN =
		-1L;
	
	int UNREALIZED =
		100;
	
	void addPlayerListener(PlayerListener __a);
	
	void close();
	
	void deallocate();
	
	String getContentType();
	
	long getDuration();
	
	long getMediaTime();
	
	int getState();
	
	TimeBase getTimeBase();
	
	void prefetch()
		throws MediaException;
	
	void realize()
		throws MediaException;
	
	void removePlayerListener(PlayerListener __a);
	
	void setLoopCount(int __a);
	
	long setMediaTime(long __a)
		throws MediaException;
	
	void setTimeBase(TimeBase __a)
		throws MediaException;
	
	void start()
		throws MediaException;
	
	void stop()
		throws MediaException;
}



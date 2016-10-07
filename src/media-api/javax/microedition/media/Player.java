// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media;


public interface Player
	extends Controllable
{
	public static final int CLOSED =
		0;
	
	public static final int PREFETCHED =
		300;
	
	public static final int REALIZED =
		200;
	
	public static final int STARTED =
		400;
	
	public static final long TIME_UNKNOWN =
		-1L;
	
	public static final int UNREALIZED =
		100;
	
	public abstract void addPlayerListener(PlayerListener __a);
	
	public abstract void close();
	
	public abstract void deallocate();
	
	public abstract String getContentType();
	
	public abstract long getDuration();
	
	public abstract long getMediaTime();
	
	public abstract int getState();
	
	public abstract TimeBase getTimeBase();
	
	public abstract void prefetch()
		throws MediaException;
	
	public abstract void realize()
		throws MediaException;
	
	public abstract void removePlayerListener(PlayerListener __a);
	
	public abstract void setLoopCount(int __a);
	
	public abstract long setMediaTime(long __a)
		throws MediaException;
	
	public abstract void setTimeBase(TimeBase __a)
		throws MediaException;
	
	public abstract void start()
		throws MediaException;
	
	public abstract void stop()
		throws MediaException;
}



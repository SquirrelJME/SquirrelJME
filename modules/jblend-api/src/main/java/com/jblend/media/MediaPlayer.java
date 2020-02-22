// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media;

import com.jblend.io.j2me.events.NativeMediaEventDispatcher;

public abstract class MediaPlayer
{
    public static final int NO_DATA = 0;
    public static final int READY = 1;
    public static final int PLAYING = 2;
    public static final int PAUSED = 3;
    public static final int ERROR = 65536;
    
    protected static final int REAL_WIDTH =
    	todo.TODO.missingInteger();
    	
    protected static final int REAL_HEIGHT =
    	todo.TODO.missingInteger();

    public MediaPlayer()
	{
		throw new todo.TODO();
	}

    public abstract void setData(MediaData var1);

    public abstract void play();

    public abstract void play(boolean var1);

    public abstract void play(int var1);

    public abstract void stop();

    public abstract void pause();

    public abstract void resume();

    public abstract int getState();

    public abstract void addMediaPlayerListener(MediaPlayerListener var1);

    public abstract void removeMediaPlayerListener(MediaPlayerListener var1);

    protected static void addNativeMediaEventDispatcher(NativeMediaEventDispatcher var0)
	{
		throw new todo.TODO();
	}
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.media;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class MediaPlayer extends Canvas {
    public static final int NO_DATA = 0;
    public static final int READY = 1;
    public static final int PLAYING = 2;
    public static final int PAUSED = 3;
    public static final int ERROR = 65536;

    public MediaPlayer(byte[] var1) {
    }

    public MediaPlayer(String var1) throws IOException {
    }

    public void setMediaData(byte[] var1)
	{
		throw new todo.TODO();
	}

    public void setMediaData(String var1) throws IOException
	{
		throw new todo.TODO();
	}

    public int getState()
	{
		throw new todo.TODO();
	}

    public int getMediaWidth()
	{
		throw new todo.TODO();
	}

    public int getMediaHeight()
	{
		throw new todo.TODO();
	}

    public int getWidth()
	{
		throw new todo.TODO();
	}

    public int getHeight()
	{
		throw new todo.TODO();
	}

    public void setContentPos(int var1, int var2)
	{
		throw new todo.TODO();
	}

    public void play()
	{
		throw new todo.TODO();
	}

    public void play(boolean var1)
	{
		throw new todo.TODO();
	}

    public void stop()
	{
		throw new todo.TODO();
	}

    public void pause()
	{
		throw new todo.TODO();
	}

    public void resume()
	{
		throw new todo.TODO();
	}

    public void setMediaPlayerListener(MediaPlayerListener var1)
	{
		throw new todo.TODO();
	}

    protected  void paint(Graphics var1)
	{
		throw new todo.TODO();
	}

    protected  void showNotify()
	{
		throw new todo.TODO();
	}

    protected  void hideNotify()
	{
		throw new todo.TODO();
	}
}


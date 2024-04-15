// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.graphics.sprite;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;

@Api
public abstract class SpriteCanvas
	extends Canvas
{
	@Api
	public SpriteCanvas(int var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void createFrameBuffer(int var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void disposeFrameBuffer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int getVirtualWidth()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int getVirtualHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPalette(int var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPattern(int var1, byte[] var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static short createCharacterCommand(int var0, boolean var1,
		int var2, boolean var3, boolean var4, int var5)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawSpriteChar(short var1, short var2, short var3)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawBackground(short var1, short var2, short var3)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void copyArea(int var1, int var2, int var3, int var4, int var5,
		int var6)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void copyFullScreen(int var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawFrameBuffer(int var1, int var2)
	{
		throw Debugging.todo();
	}
}

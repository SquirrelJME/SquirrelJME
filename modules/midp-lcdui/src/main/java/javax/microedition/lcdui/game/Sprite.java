// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

@Api
public class Sprite
	extends Layer
{
	@Api
	public static final int TRANS_MIRROR =
		2;
	
	@Api
	public static final int TRANS_MIRROR_ROT180 =
		1;
	
	@Api
	public static final int TRANS_MIRROR_ROT270 =
		4;
	
	@Api
	public static final int TRANS_MIRROR_ROT90 =
		7;
	
	@Api
	public static final int TRANS_NONE =
		0;
	
	@Api
	public static final int TRANS_ROT180 =
		3;
	
	@Api
	public static final int TRANS_ROT270 =
		6;
	
	@Api
	public static final int TRANS_ROT90 =
		5;
	
	@Api
	public Sprite(Image __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Sprite(Image __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Sprite(Sprite __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean collidesWith(Image __a, int __b, int __c, boolean 
		__d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean collidesWith(Sprite __a, boolean __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean collidesWith(TiledLayer __a, boolean __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void defineCollisionRectangle(int __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void defineReferencePixel(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getFrame()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getFrameSequenceLength()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getRawFrameCount()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getRefPixelX()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getRefPixelY()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void nextFrame()
	{
		throw Debugging.todo();
	}
	
	@Override
	public final void paint(Graphics __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void prevFrame()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setCollisionAlpha(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setFrame(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setFrameSequence(int[] __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setImage(Image __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setRefPixelPosition(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setTransform(int __a)
	{
		throw Debugging.todo();
	}
}



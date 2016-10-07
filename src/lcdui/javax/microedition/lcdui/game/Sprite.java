// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Sprite
	extends Layer
{
	public static final int TRANS_MIRROR =
		2;
	
	public static final int TRANS_MIRROR_ROT180 =
		1;
	
	public static final int TRANS_MIRROR_ROT270 =
		4;
	
	public static final int TRANS_MIRROR_ROT90 =
		7;
	
	public static final int TRANS_NONE =
		0;
	
	public static final int TRANS_ROT180 =
		3;
	
	public static final int TRANS_ROT270 =
		6;
	
	public static final int TRANS_ROT90 =
		5;
	
	public Sprite(Image __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Sprite(Image __a, int __b, int __c)
	{
		super();
		throw new Error("TODO");
	}
	
	public Sprite(Sprite __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public final boolean collidesWith(Image __a, int __b, int __c, boolean 
		__d)
	{
		throw new Error("TODO");
	}
	
	public final boolean collidesWith(Sprite __a, boolean __b)
	{
		throw new Error("TODO");
	}
	
	public final boolean collidesWith(TiledLayer __a, boolean __b)
	{
		throw new Error("TODO");
	}
	
	public void defineCollisionRectangle(int __a, int __b, int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	public void defineReferencePixel(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public final int getFrame()
	{
		throw new Error("TODO");
	}
	
	public int getFrameSequenceLength()
	{
		throw new Error("TODO");
	}
	
	public int getRawFrameCount()
	{
		throw new Error("TODO");
	}
	
	public int getRefPixelX()
	{
		throw new Error("TODO");
	}
	
	public int getRefPixelY()
	{
		throw new Error("TODO");
	}
	
	public void nextFrame()
	{
		throw new Error("TODO");
	}
	
	@Override
	public final void paint(Graphics __a)
	{
		throw new Error("TODO");
	}
	
	public void prevFrame()
	{
		throw new Error("TODO");
	}
	
	public void setFrame(int __a)
	{
		throw new Error("TODO");
	}
	
	public void setFrameSequence(int[] __a)
	{
		throw new Error("TODO");
	}
	
	public void setImage(Image __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public void setRefPixelPosition(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public void setTransform(int __a)
	{
		throw new Error("TODO");
	}
}



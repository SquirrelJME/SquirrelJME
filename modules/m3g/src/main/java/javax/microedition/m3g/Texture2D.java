// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.m3g;


import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class Texture2D
	extends Transformable
{
	@Api
	public static final int FILTER_BASE_LEVEL =
		208;
	
	@Api
	public static final int FILTER_LINEAR =
		209;
	
	@Api
	public static final int FILTER_NEAREST =
		210;
	
	@Api
	public static final int FUNC_ADD =
		224;
	
	@Api
	public static final int FUNC_BLEND =
		225;
	
	@Api
	public static final int FUNC_DECAL =
		226;
	
	@Api
	public static final int FUNC_MODULATE =
		227;
	
	@Api
	public static final int FUNC_REPLACE =
		228;
	
	@Api
	public static final int WRAP_CLAMP =
		240;
	
	@Api
	public static final int WRAP_REPEAT =
		241;
	
	@Api
	public Texture2D(Image2D __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getBlendColor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getBlending()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Image2D getImage()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getImageFilter()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getLevelFilter()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getWrappingS()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getWrappingT()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setBlendColor(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setBlending(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setFiltering(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setImage(Image2D __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setWrapping(int __a, int __b)
	{
		throw Debugging.todo();
	}
}



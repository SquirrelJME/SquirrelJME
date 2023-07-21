// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a raster image.
 *
 * @see javax.microedition.lcdui.Image
 * @since 2021/11/30
 */
@Api
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
public abstract class Image
{
	protected Image()
	{
	}
	
	@Api
	public abstract void dispose();
	
	public Graphics getGraphics()
	{
		// Only valid for Image.createGraphics(int, int, ...)
		throw Debugging.todo();
	}
	
	@Api
	public int getHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getWidth()
	{
		throw Debugging.todo();
	}
	
	public static Image createImage(int __w, int __h)
	{
		throw Debugging.todo();
	}
}

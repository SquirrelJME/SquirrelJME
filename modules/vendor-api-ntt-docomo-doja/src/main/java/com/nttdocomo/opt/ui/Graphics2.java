// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.opt.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;
import cc.squirreljme.runtime.nttdocomo.ui.LockFlush;
import com.nttdocomo.ui.Graphics;
import com.nttdocomo.ui.MediaImage;

@Api
public abstract class Graphics2
	extends Graphics
{
	/**
	 * Wraps the given graphics object.
	 *
	 * @param __g The graphics to wrap.
	 * @param __bgColor The background color for
	 * {@link #clearRect(int, int, int, int)}.
	 * @param __flush Optional flush callback to be executed when this
	 * occurs.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	protected Graphics2(javax.microedition.lcdui.Graphics __g,
		BGColor __bgColor, LockFlush __flush)
		throws NullPointerException
	{
		super(__g, __bgColor, __flush);
	}
	
	@Api
	public void drawNthImage(MediaImage __a, int __b,
		int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawSpriteSet(SpriteSet __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawSpriteSet(SpriteSet __a, int __b,
		int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawImageMap(ImageMap __a, int __b,
		int __c)
	{
		throw Debugging.todo();
	}
}

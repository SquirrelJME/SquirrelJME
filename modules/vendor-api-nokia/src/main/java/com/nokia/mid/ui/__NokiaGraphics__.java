// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import javax.microedition.lcdui.Image;

/**
 * This is an implementation of Nokia's graphic interface which allows for
 * direct pixel access.
 *
 * @since 2021/01/04
 */
class __NokiaGraphics__
	implements DirectGraphics
{
	/** The raw graphics to use. */
	protected final PencilGraphics graphics;
	
	/**
	 * Initializes the Nokia Graphics wrapper.
	 *
	 * @param __g The graphics to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/04
	 */
	public __NokiaGraphics__(PencilGraphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this.graphics = __g;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void drawImage(Image __var1, int __var2, int __var3, int __var4,
		int __var5)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void drawPixels(byte[] __var1, byte[] __var2, int __var3,
		int __var4, int __var5, int __var6, int __var7, int __var8,
		int __var9,
		int __var10)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void drawPixels(short[] __var1, boolean __var2, int __var3,
		int __var4, int __var5, int __var6, int __var7, int __var8,
		int __var9,
		int __var10)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void drawPixels(int[] __var1, boolean __var2, int __var3,
		int __var4, int __var5, int __var6, int __var7, int __var8,
		int __var9,
		int __var10)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void drawPolygon(int[] __var1, int __var2, int[] __var3,
		int __var4,
		int __var5, int __var6)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void drawTriangle(int __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void fillPolygon(int[] __var1, int __var2, int[] __var3,
		int __var4,
		int __var5, int __var6)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void fillTriangle(int __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void getPixels(byte[] __var1, byte[] __var2, int __var3,
		int __var4,
		int __var5, int __var6, int __var7, int __var8, int __var9)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void getPixels(int[] __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void getPixels(short[] __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/04
	 */
	@Override
	public void setARGBColor(int __var1)
	{
		throw Debugging.todo();
	}
}

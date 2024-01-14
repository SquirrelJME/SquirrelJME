// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Loader for DoJa 8-bit images.
 *
 * @since 2024/01/14
 */
public class DoJa8BitImageLoader
	implements NativeImageLoadCallback
{
	/** The image width. */
	private volatile int _width;
	
	/** The image height. */
	private volatile int _height;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void addImage(int[] __buf, int __off, int __len, int __frameDelay,
		boolean __hasAlpha)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void cancel()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public Object finish()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void initialize(int __width, int __height, boolean __animated,
		boolean __scalable)
	{
		this._width = __width;
		this._height = __height;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void setLoopCount(int __loopCount)
	{
		throw Debugging.todo();
	}
}

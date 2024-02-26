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
	/** Does this have alpha to the colors? */
	private boolean _hasAlpha;
	
	/** The image width. */
	private volatile int _width;
	
	/** The image height. */
	private volatile int _height;
	
	/** The image palette. */
	private volatile int[] _palette;
	
	/** 8-bit pixel data. */
	private volatile byte[] _pixels;
	
	/** Transparent index. */
	private volatile int _transIndex =
		-1;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void addImage(int[] __buf, int __off, int __len, int __frameDelay,
		boolean __hasAlpha)
	{
		// Map values down from int to 8-bit
		byte[] actual = new byte[__len];
		for (int i = 0, at = __off; i < __len; i++, at++)
			actual[i] = (byte)__buf[at];
		
		// Set raw indexed pixels
		this._pixels = actual;
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
		return new EightBitImageStore(this._pixels, this._width, this._height,
			this._palette, this._hasAlpha, this._transIndex);
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
	
	/**
	 * {@inheritDoc}
	 *
	 * @return
	 * @since 2024/01/14
	 */
	@Override
	public boolean setPalette(int[] __colors, int __off, int __len,
		boolean __hasAlpha, int __transDx)
	{
		// Copy palette
		int[] palette = new int[__len];
		System.arraycopy(__colors, __off,
			palette, 0, __len);
		
		// Store within
		this._palette = palette;
		this._hasAlpha = __hasAlpha || __transDx >= 0;
		this._transIndex = __transDx;
		
		// Use indexed mode
		return true;
	}
}

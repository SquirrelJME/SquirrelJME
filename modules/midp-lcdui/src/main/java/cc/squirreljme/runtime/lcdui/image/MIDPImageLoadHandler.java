// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.runtime.cldc.util.IntegerList;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.AnimatedImage;
import javax.microedition.lcdui.Image;

/**
 * Handles loading of native images.
 *
 * @since 2022/06/28
 */
public final class MIDPImageLoadHandler
	implements NativeImageLoadCallback
{
	/** The factory used to create images. */
	private final ImageFactory<AnimatedImage, Image> _factory;
	
	/** The images that have been added. */
	private final List<Image> _images =
		new ArrayList<>();
	
	/** Frame delay times. */
	private final IntegerList _frameDelay =
		new IntegerList();
	
	/** The image width. */
	private volatile int _width =
		-1;
	
	/** The image height. */
	private volatile int _height =
		-1;
	
	/** Is this scalable? */
	private volatile boolean _isScalable;
	
	/** Is this animated? */
	private volatile boolean _isAnimated;
	
	/** Loop count for animated images. */
	private volatile int _loopCount =
		-1;
	
	/** Canceled? */
	volatile boolean _canceled;
	
	/**
	 * Initializes the load handler.
	 * 
	 * @param __factory The factory used to initialize images.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/28
	 */
	public MIDPImageLoadHandler(ImageFactory<AnimatedImage, Image> __factory)
		throws NullPointerException
	{
		if (__factory == null)
			throw new NullPointerException("NARG");
		
		this._factory = __factory;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public void addImage(int[] __buf, int __off, int __len, int __frameDelay,
		boolean __hasAlpha)
	{
		List<Image> images = this._images;
		IntegerList frameDelay = this._frameDelay;
		synchronized (this)
		{
			// If not animated, only allow a single image at once
			if (!this._isAnimated && !images.isEmpty())
				return;
			
			// Store the image data directly
			images.add(this._factory.stillImage(__buf, __off,
				__len, false, __hasAlpha,
				this._width, this._height));
			frameDelay.add(__frameDelay);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void cancel()
	{
		synchronized (this)
		{
			this._canceled = true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public Object finish()
	{
		List<Image> images = this._images;
		IntegerList frameDelay = this._frameDelay;
		synchronized (this)
		{
			if (this._canceled)
				return null;
			
			// Animated image
			if (this._isAnimated)
				return this._factory.animatedImage(
					images.toArray(new Image[images.size()]),
					frameDelay.toIntegerArray(), this._loopCount);
			
			// Still image otherwise
			else
			{
				// {@squirreljme.error EB34 No still image specified.}
				if (images.isEmpty())
					throw new IllegalStateException("EB34");
				
				return images.get(0);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public void initialize(int __width, int __height, boolean __animated,
		boolean __scalable)
	{
		synchronized (this)
		{
			this._width = __width;
			this._height = __height;
			this._isAnimated = __animated;
			this._isScalable = __scalable;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public void setLoopCount(int __loopCount)
	{
		synchronized (this)
		{
			this._loopCount = __loopCount;
		}
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
		// We do not care about palettes in MIDP
		return false;
	}
}

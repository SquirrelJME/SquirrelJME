// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.swing;

import java.awt.AWTError;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Transparency;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageType;
import net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.JVMJavaSEKernel;
import net.multiphasicapps.squirreljme.ui.PIBase;
import net.multiphasicapps.squirreljme.ui.PIDisplay;
import net.multiphasicapps.squirreljme.ui.PILabel;
import net.multiphasicapps.squirreljme.ui.PIList;
import net.multiphasicapps.squirreljme.ui.PIManager;
import net.multiphasicapps.squirreljme.ui.PIMenu;
import net.multiphasicapps.squirreljme.ui.PIMenuItem;
import net.multiphasicapps.squirreljme.ui.UIBase;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UILabel;
import net.multiphasicapps.squirreljme.ui.UIList;
import net.multiphasicapps.squirreljme.ui.UIListData;
import net.multiphasicapps.squirreljme.ui.UIManager;
import net.multiphasicapps.squirreljme.ui.UIMenu;
import net.multiphasicapps.squirreljme.ui.UIMenuItem;

/**
 * This is a display manager which interfaces with Java's Swing and uses it
 * to interact with the user.
 *
 * @since 2016/05/20
 */
public class SwingManager
	extends PIManager
{
	/** The kernel which created this. */
	protected final JVMJavaSEKernel kernel;
	
	/** Cache of image datas to buffered images. */
	private final Map<ImageData, BufferedImage> _bicache =
		new WeakHashMap<>();
	
	/** ImageIcon wrapping of the buffered images. */
	private final Map<ImageData, ImageIcon> _iicache =
		new WeakHashMap<>();
	
	/**
	 * Initializes the swing based display manager.
	 *
	 * @param __k The kernel which created this display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public SwingManager(JVMJavaSEKernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public PIDisplay createDisplay(Reference<UIDisplay> __ref)
		throws UIException
	{
		// Create it
		try
		{
			return new SwingDisplay(this, __ref);
		}
		
		// {@squirreljme.error AZ01 Could not create a display.}
		catch (AWTError|HeadlessException e)
		{
			throw new UIException("AZ01", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public PILabel createLabel(Reference<UILabel> __ref)
	{
		return new SwingLabel(this, __ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public PIList createList(Reference<UIList<Object>> __ref,
		UIListData<Object> __ld)
		throws UIException
	{
		return new SwingList(this, __ref, __ld);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public PIMenu createMenu(Reference<UIMenu> __ref)
		throws UIException
	{
		return new SwingMenu(this, __ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public PIMenuItem createMenuItem(Reference<UIMenuItem> __ref)
		throws UIException
	{
		return new SwingMenuItem(this, __ref);
	}
	
	/**
	 * Checks in the cache or translates and caches the given {@link ImageData}
	 * as a Swing {@link BufferedImage}.
	 *
	 * @param __id The image data to convert.
	 * @return The specified image as a {@link BufferedImage}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/23
	 */
	public BufferedImage imageDataToBufferedImage(ImageData __id)
		throws NullPointerException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			Map<ImageData, BufferedImage> bicache = this._bicache;
			BufferedImage rv = bicache.get(__id);
			
			// Needs creation?
			if (rv == null)
			{
				// Get details
				int width = __id.width();
				int height = __id.height();
		
				// Depends on the image type
				switch (__id.type())
				{
						// Unknown, use a slow means of creating a mapping copy
					default:
						{
							// Create compatible image
							rv = GraphicsEnvironment.
								getLocalGraphicsEnvironment().
								getDefaultScreenDevice().
								getDefaultConfiguration().
								createCompatibleImage(width, height,
									Transparency.TRANSLUCENT);
					
							// Copy pixel by pixel
							for (int y = 0; y < height; y++)
								for (int x = 0; x < width; x++)
									rv.setRGB(x, y, __id.atARGB(x, y));
						}
						break;
				}
				
				// Store into the cache
				bicache.put(__id, rv);
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * This translates image data to an {@link ImageIcon} which is used in
	 * labels and such.
	 *
	 * @param __id The image data to wrap.
	 * @return The wrapped image as an {@link ImageIcon}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/26
	 */
	public ImageIcon imageDataToImageIcon(ImageData __id)
		throws NullPointerException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
			
		// Lock
		synchronized (this.lock)
		{
			Map<ImageData, ImageIcon> iicache = this._iicache;
			ImageIcon rv = iicache.get(__id);
			
			// Needs creation?
			if (rv == null)
				iicache.put(__id,
					(rv = new ImageIcon(imageDataToBufferedImage(__id))));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public int[] preferredIconSizes()
		throws UIException
	{
		// Use common icon sizes used on desktop systems
		return new int[]
			{
				16, 16,
				20, 20,
				24, 24,
				32, 32,
				40, 40,
				48, 48,
				64, 64
			};
	}
}


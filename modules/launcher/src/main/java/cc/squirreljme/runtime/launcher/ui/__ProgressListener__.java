// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.launch.Application;
import cc.squirreljme.jvm.launch.SuiteScanListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import org.freedesktop.tango.TangoIconLoader;

/**
 * This listens for when suites have been detected by the scanner.
 *
 * @since 2020/12/29
 */
final class __ProgressListener__
	implements SuiteScanListener
{
	/** The scale shift. */
	private static final short _SCALE_SHIFT =
		8;
	
	/** The scale mask. */
	private static final short _SCALE_MASK =
		0xFF;
	
	/** The suites that have been listed. */
	protected final ArrayList<Application> listedSuites;
	
	/** The program list. */
	protected final List programList;
	
	/** The main display. */
	protected final Display mainDisplay;
	
	/** Comparator for sorting applications. */
	private final Comparator<Application> _comparator =
		new __ApplicationComparator__();
	
	/** The canvas to refresh on updates. */
	protected final SplashScreen refreshCanvas;
	
	/** The current refresh state. */
	protected final __RefreshState__ refreshState;
	
	/** The default application icon, if one is missing. */
	protected volatile Image _defaultIcon;
	
	/**
	 * Initializes the progress listener.
	 * 
	 * @param __programList The program list used.
	 * @param __listedSuites The suites that are available.
	 * @param __refreshCanvas The canvas to update on refreshes.
	 * @param __refreshState The current refresh state.
	 * @param __mainDisplay The main display.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	public __ProgressListener__(List __programList,
		ArrayList<Application> __listedSuites, SplashScreen __refreshCanvas,
		__RefreshState__ __refreshState, Display __mainDisplay)
		throws NullPointerException
	{
		if (__programList == null || __listedSuites == null)
			throw new NullPointerException("NARG");
		
		this.programList = __programList;
		this.listedSuites = __listedSuites;
		this.refreshCanvas = __refreshCanvas;
		this.refreshState = __refreshState;
		this.mainDisplay = __mainDisplay;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/29
	 */
	@Override
	public void scanned(Application __app, int __dx, int __total)
	{
		// Do nothing if this is not to appear on the launcher
		if (__app.isNoLauncher())
			return;
		
		ArrayList<Application> listedSuites = this.listedSuites;
		List programList = this.programList;
		SplashScreen refreshCanvas = this.refreshCanvas;
		__RefreshState__ refreshState = this.refreshState;
		
		// Update title to reflect this discovery
		String updateMessage = String.format(
			"Querying Suites (Found %d of %d)...", __dx + 1, __total);
		synchronized (programList)
		{
			programList.setTitle(updateMessage);
		}
		
		// Update splash screen
		synchronized (refreshState)
		{
			refreshState.set(updateMessage, __dx + 1, __total);
			if (refreshCanvas != null)
				synchronized (refreshCanvas)
				{
					refreshCanvas.requestRepaint();
				}
		}
		
		// Try to load the image for the application
		// Do this first, so we can keep the application list update
		// synchronized nicely...
		Image icon = null;
		try (InputStream iconData = __app.iconStream())
		{
			if (iconData != null)
				icon = Image.createImage(iconData);
			else
				icon = this.__defaultIcon();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Make the icon fit nicely?
		if (icon != null)
		{
			// Get the preferred icon size
			Display mainDisplay = this.mainDisplay;
			if (mainDisplay == null)
				try
				{
					mainDisplay = Display.getDisplays(0)[0];
				}
				catch (IllegalStateException ignored)
				{
					mainDisplay = null;
				}
			
			int prefW;
			int prefH;
			if (mainDisplay != null)
			{
				prefW = mainDisplay.getBestImageWidth(Display.LIST_ELEMENT);
				prefH = mainDisplay.getBestImageHeight(Display.LIST_ELEMENT);
			}
			else
				prefW = prefH = 16;
			
			// Scale the icon
			if (icon.getWidth() > prefW ||
				icon.getHeight() > prefH)
				try
				{
					// If this is the default icon, we scale it only once!
					boolean isDefault = (icon == this._defaultIcon);
					
					icon = __ProgressListener__.__scaleIcon(
						icon, prefW, prefH);
					
					// Use new default if we did scale
					if (isDefault)
						this._defaultIcon = icon;
				}
				catch (IndexOutOfBoundsException e)
				{
					e.printStackTrace();
				}
		}
		
		// Determine where this should go and remember the suite
		synchronized (listedSuites)
		{
			int at = Collections.binarySearch(listedSuites, __app,
				this._comparator);
			if (at < 0)
				at = -(at + 1);
			listedSuites.add(at, __app);
			
			// Add entry to the list
			synchronized (programList)
			{
				programList.insert(at, __app.displayName(), icon);
			}
		}
	}
	
	/**
	 * Loads the default icon.
	 * 
	 * @return The default icon.
	 * @throws IOException On read errors.
	 * @since 2022/10/03
	 */
	private Image __defaultIcon()
		throws IOException
	{
		// Already loaded?
		Image defaultIcon = this._defaultIcon;
		if (defaultIcon != null)
			return defaultIcon;
		
		try (InputStream in = TangoIconLoader.loadIcon(
			16, "application-x-executable"))
		{
			if (in == null)
				return null;
			
			defaultIcon = Image.createImage(in);
			this._defaultIcon = defaultIcon;
			return defaultIcon;
		}
	}
	
	/**
	 * Scales the given icon to the given size.
	 * 
	 * @param __icon The icon to scale.
	 * @param __prefW The target width.
	 * @param __prefH The target height.
	 * @return The new resultant icon.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	private static Image __scaleIcon(Image __icon, int __prefW, int __prefH)
		throws NullPointerException
	{
		if (__icon == null)
			throw new NullPointerException("NARG");
		
		// Get original image size
		int srcW = __icon.getWidth();
		int srcH = __icon.getHeight();
		
		// Determine the pixels to scale
		int scaleX = (srcW << __ProgressListener__._SCALE_SHIFT) / __prefW;
		int scaleY = (srcH << __ProgressListener__._SCALE_SHIFT) / __prefH;
		
		// Read original image
		int[] src = new int[srcW * srcH];
		__icon.getRGB(src, 0, srcW, 0, 0, srcW, srcH);
		
		// Setup new target image
		int[] dest = new int[__prefW * __prefH];
		
		// Perform the scaling operation
		for (int destY = 0, baseSrcY = 0; destY < __prefH;
			destY++, baseSrcY += scaleY)
		{
			int srcI = srcW * (baseSrcY & (~__ProgressListener__._SCALE_MASK));
			for (int destI = __prefW * destY, endDestI = destI + __prefW;
				destI < endDestI; destI++, srcI += scaleX)
				dest[destI] = src[srcI >>> __ProgressListener__._SCALE_SHIFT]; 
		}
		
		// Return the resultant scaled image now
		return Image.createRGBImage(dest, __prefW, __prefH, __icon.hasAlpha());
	}
}

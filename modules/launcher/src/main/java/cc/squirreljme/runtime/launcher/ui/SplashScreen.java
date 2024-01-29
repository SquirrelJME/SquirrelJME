// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * This is the splash screen for the launcher which always shows.
 *
 * @since 2019/05/19
 */
public final class SplashScreen
	extends Canvas
	implements Runnable
{
	/** The generator for the splash screen. */
	protected final SplashScreenImage generator;
	
	/** The refresh state. */
	private final __RefreshState__ _refreshState;
	
	/** Loading bar X position. */
	private final int _loadingBarX;
	
	/** Loading bar Y position. */
	private final int _loadingBarY;
	
	/** Loading bar width. */
	private final int _loadingBarWidth;
	
	/** Loading bar height. */
	private final int _loadingBarHeight;
	
	/** Loading string X position. */
	private final int _loadingStrX;
	
	/** Loading String Y position. */
	private final int _loadingStrY;
	
	/** Are we in a refresh? */
	volatile boolean _inRefresh;
	
	/** Raw image pixels. */
	private volatile int[] _image;
	
	/**
	 * Initializes the splash screen with a precached image.
	 *
	 * @param __sw The screen width.
	 * @param __sh The screen height.
	 * @param __refreshState The refresh state.
	 * @since 2019/05/19
	 */
	public SplashScreen(int __sw, int __sh, __RefreshState__ __refreshState)
	{
		this._refreshState = __refreshState;
		
		// Set a nice title!
		this.setTitle("SquirrelJME is Loading!");
		
		// Full-screen mode for the entire image
		this.setFullScreenMode(true);
		
		// Add command to exit the VM, so it does something
		this.addCommand(MidletMain.EXIT_COMMAND);
		
		// Need to handle commands and such
		__ExitHandler__ ch = new __ExitHandler__();
		this.setCommandListener(ch);
		
		// Splash screen image setup
		SplashScreenImage gen = new SplashScreenImage();
		this.generator = gen;
		
		// Height of the screen
		int width = SplashScreenImage.WIDTH;
		int height = SplashScreenImage.HEIGHT;
		int centerX = width / 2;
		int centerY = height / 2;
		
		// Loading
		this._loadingBarX = 0;
		this._loadingBarY = centerY + (height / 8) + (gen._verHeight / 2);
		this._loadingBarWidth = width;
		this._loadingBarHeight = gen._verHeight;
		this._loadingStrX = centerX;
		this._loadingStrY = this._loadingBarY + (gen._verHeight / 2);
		
		// Load the launcher image in a background thread so the splash screen
		// can still display the copyright notice
		new Thread(this, "LauncherImageLoader").start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	public final void paint(Graphics __g)
	{
		// If we are in a refresh, set the state
		synchronized (this)
		{
			this._inRefresh = true;
		}
		
		// We are not in a refresh
		try
		{
			// Draw the raw image data, is the fastest
			int[] image = this._image;
			if (image != null)
				__g.drawRGB(image, 0, SplashScreenImage.WIDTH,
					0, 0,
					SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT,
					false);
			
			// The image is not fully loaded yet, so draw the copyright
			// at least
			else
				this.generator.drawCopyright(__g, true);
			
			// Put in the current refresh status
			__RefreshState__ refreshState = this._refreshState;
			String message = refreshState._message;
			if (message != null)
			{
				// How much progress is there?
				int at = refreshState._at;
				int total = refreshState._total;
				
				// Where is the loading bar at?
				int lBX = this._loadingBarX;
				int lBY = this._loadingBarY;
				int lBW = this._loadingBarWidth;
				int lBH = this._loadingBarHeight;
				
				// Draw progress bar?
				if (total != 0)
				{
					float percent = (float)at / (float)total;
					int leftBarPx = (int)((float)lBW * percent);
					int rightBarPx = lBW - leftBarPx;
					
					// Left side of bar
					__g.setColor(0xFF7900);
					__g.fillRect(lBX, lBY,
						leftBarPx, lBH);
					
					// Right side of bar
					if (rightBarPx > 0)
					{
						__g.setColor(0x444444);
						__g.fillRect(lBX + leftBarPx, lBY,
							rightBarPx, lBH);
					}
				}
				
				// Draw message
				__g.setColor(0xFFFFFF);
				__g.drawString(message,
					this._loadingStrX, this._loadingStrY,
					Graphics.VCENTER | Graphics.HCENTER);
			}
		}
		
		// Not refreshing
		finally
		{
			synchronized (this)
			{
				this._inRefresh = false;
			}
		}
	}
	
	/**
	 * Request repaint of the canvas without overloading.
	 * 
	 * @since 2021/12/29
	 */
	public final void requestRepaint()
	{
		// Do not request another repaint if we are in one
		synchronized (this)
		{
			if (this._inRefresh)
				return;
			
			// Request it now	
			this.repaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	public final void run()
	{
		// Print copyright to the console as well
		System.err.println(SplashScreenImage.COPYRIGHT);
		
		// Generate splash image
		this._image = this.generator.generateSplash();
		
		// Perform a repaint before we return so the better looking splash
		// screen is used
		this.repaint();
	}
}

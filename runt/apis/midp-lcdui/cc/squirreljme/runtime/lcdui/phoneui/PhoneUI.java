// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This class contains the definition and holders for the phone based user
 * interface.
 *
 * @since 2019/05/16
 */
public final class PhoneUI
{
	/** The width of the phone screen. */
	public static final int DEFAULT_SCREEN_WIDTH =
		240;
	
	/** The height of the phone screen. */
	public static final int DEFAULT_SCREEN_HEIGHT =
		320;
	
	/** The width of the phone screen. */
	public final int width;
	
	/** The height of the phone screen. */
	public final int height;
	
	/** The backend this UI uses. */
	protected final PhoneDisplayBackend backend;
	
	/** Active display image. */
	protected final ActiveDisplay activedisplay;
	
	/** The buffer which represents the display itself. */
	protected final Image buffer;
	
	/** The current displayable to show. */
	private Displayable _current;
	
	/** Should the display be repainted? */
	private volatile boolean _repaint;
	
	/**
	 * Initializes the base UI using the default screen size.
	 *
	 * @param __b The display backend to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	public PhoneUI(PhoneDisplayBackend __b)
		throws NullPointerException
	{
		this(__b, DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
	}
	
	/**
	 * Initializes the UI with the given screen width and height.
	 *
	 * @param __b The display backend to use.
	 * @param __sw The screen width.
	 * @param __sh The screen height.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/15
	 */
	public PhoneUI(PhoneDisplayBackend __b, int __sw, int __sh)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.backend = __b;
		this.width = (__sw = (__sw <= 0 ? DEFAULT_SCREEN_WIDTH : __sw));
		this.height = (__sh = (__sh <= 0 ? DEFAULT_SCREEN_HEIGHT : __sh));
		
		// Set active display
		ActiveDisplay activedisplay = new ActiveDisplay(__sw, __sh);
		this.activedisplay = activedisplay;
		
		// All screen operations are done on a secondary buffer
		this.buffer = activedisplay.image;
	}
	
	/**
	 * Returns the height of the content area.
	 *
	 * @return The content area height.
	 * @since 2019/05/16
	 */
	public final int contentHeight()
	{
		return this.activedisplay._contentheight;
	}
	
	/**
	 * Returns the width of the content area.
	 *
	 * @return The content area width.
	 * @since 2019/05/16
	 */
	public final int contentWidth()
	{
		return this.activedisplay._contentwidth;
	}
	
	/**
	 * Signals that the display should be repainted.
	 *
	 * @since 2019/05/16
	 */
	public final void repaint()
	{
		this.repaint(0, 0, this.width, this.height);
	}
	
	/**
	 * Signals that the display should be repainted.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		this.backend.repaint(__x, __y, __w, __h);
	}
	
	/**
	 * Sets the current displayable to be drawn.
	 *
	 * @param __d The displayable to draw.
	 * @since 2019/05/16
	 */
	public final void setCurrent(Displayable __d)
	{
		// Set
		this._current = __d;
		
		// Activate the display
		ActiveDisplay ad = this.activedisplay;
		ad.activate(__d);
		this.backend.activate(ad);
		
		// Repaint
		this.repaint();
	}
	
	/**
	 * Sets the title of what is displayed on the screen.
	 *
	 * @param __t The title to use, {@code null} uses a default title.
	 * @since 2019/05/16
	 */
	public final void setTitle(String __t)
	{
		// Default title?
		if (__t == null)
			__t = "SquirrelJME";
		
		// Set
		this.activedisplay._title = __t;
		
		// Repaint
		if (this._current != null)
			this.repaint();
	}
	
	/**
	 * Vibrates the display for the given number of milliseconds.
	 *
	 * @param __ms The number of milliseconds to vibrate for.
	 * @since 2019/05/17
	 */
	public final void vibrate(int __ms)
	{
		// If the backend does not support vibration then simulate it via
		// the display interface
		if (!this.backend.vibrate(__ms))
			this.activedisplay.vibrate(__ms);
	}
}


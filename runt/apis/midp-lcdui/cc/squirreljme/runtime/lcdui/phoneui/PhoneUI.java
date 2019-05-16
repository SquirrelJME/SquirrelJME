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
	
	/** The buffer which represents the display itself. */
	protected final Image buffer;
	
	/** The current displayable to show. */
	private Displayable _current;
	
	/** The title to use. */
	private String _title;
	
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
		
		// The screen is drawn into this buffer, which is then draw on the
		// actual display (using possible scaling, or otherwise)
		this.buffer = Image.createImage(__sw, __sh);
	}
	
	/**
	 * Returns the height of the content area.
	 *
	 * @return The content area height.
	 * @since 2019/05/16
	 */
	public final int contentHeight()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the width of the content area.
	 *
	 * @return The content area width.
	 * @since 2019/05/16
	 */
	public final int contentWidth()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Signals that the display should be repainted on the next draw loop.
	 *
	 * @since 2019/05/16
	 */
	public final void repaint()
	{
		this._repaint = true;
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
		this._title = __t;
		
		// Repaint
		if (this._current != null)
			this.repaint();
	}
}


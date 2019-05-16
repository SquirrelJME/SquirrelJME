// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import javax.microedition.lcdui.Graphics;

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
	public final int screenwidth;
	
	/** The height of the phone screen. */
	public final int screenheight;
	
	/**
	 * Initializes the base UI using the default screen size.
	 *
	 * @since 2019/05/16
	 */
	public PhoneUI()
	{
		this(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
	}
	
	/**
	 * Initializes the UI with the given screen width and height.
	 *
	 * @param __sw The screen width.
	 * @param __sh The screen height.
	 * @since 2019/05/15
	 */
	public PhoneUI(int __sw, int __sh)
	{
		this.screenwidth = (__sw <= 0 ? DEFAULT_SCREEN_WIDTH : __sw);
		this.screenheight = (__sh <= 0 ? DEFAULT_SCREEN_HEIGHT : __sh);
	}
	
	/**
	 * Paints the actual phone interface to the given graphics target.
	 *
	 * @param __g The graphics to draw on.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	public final void paint(Graphics __g, int __bw, int __bh)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}


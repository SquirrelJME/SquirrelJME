// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.lcdui.gfx.AcceleratedGraphics;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 * This class contains the state that is contained within a {@link Display}.
 *
 * @since 2018/12/02
 */
public final class UIDisplayState
{
	/**
	 * {@squirreljme.property cc.squirreljme.lcdui.acceleration=bool
	 * Should accelerated graphics be used if it is available? This defaults
	 * to {@code true} and it is recommended it be used, otherwise it may be
	 * disabled if it causes issues with some software.}
	 */
	public static boolean USE_ACCELERATION =
		Boolean.valueOf(System.getProperty("cc.squirreljme.lcdui.acceleration",
			"true"));
	
	/** The native display ID. */
	protected final int nativeid;
	
	/** The framebuffer for this display. */
	protected UIFramebuffer framebuffer;
	
	/** The current state count of the framebuffer. */
	private int _statecount =
		-1;
	
	/**
	 * Initializes the display state for the given ID.
	 *
	 * @param __nid The native ID of the display.
	 * @since 2018/12/02
	 */
	public UIDisplayState(int __nid)
	{
		this.nativeid = __nid;
	}
	
	/**
	 * Returns the framebuffer for this display.
	 *
	 * @return The display's framebuffer.
	 * @since 2018/12/02
	 */
	public final UIFramebuffer framebuffer()
	{
		UIFramebuffer rv = this.framebuffer;
		
		// If the state count of the framebuffer has changed, then the
		// parameters have changed and it must be updated
		int nid = this.nativeid,
			statecount = this._statecount,
			newcount;
		if (statecount != (newcount =
			NativeDisplayAccess.framebufferStateCount(nid)))
		{
			this.framebuffer = (rv = UIFramebuffer.loadNativeFramebuffer(nid));
			this._statecount = newcount;
		}
		
		// Use it
		return rv;
	}
	
	/**
	 * Returns the graphics (potentially accelerated) for this framebuffer.
	 *
	 * @return The graphics object.
	 * @since 2018/12/02
	 */
	public final Graphics graphics()
	{
		// If acceleration is enabled, try to get accelerated graphics
		int nativeid = this.nativeid;
		if (USE_ACCELERATION)
			try
			{
				return AcceleratedGraphics.instance(nativeid);
			}
			catch (UnsupportedOperationException e)
			{
			}
		
		// Get graphics from the framebuffer, do it in a loop because
		// the framebuffer might have changed between a call
		for (;;)
			try
			{
				return this.framebuffer().graphics();
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
			}
	}
	
	/**
	 * Sets the title of this display.
	 *
	 * @param __t The title of the display.
	 * @since 2018/12/02
	 */
	public final void setTitle(String __t)
	{
		NativeDisplayAccess.setDisplayTitle(this.nativeid, __t);
	}
}


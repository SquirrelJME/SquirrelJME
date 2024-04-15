// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import javax.microedition.lcdui.Graphics;

/**
 * This is used for the target of any {@link ProxyGraphics}.
 *
 * @since 2022/02/25
 */
public final class ProxyGraphicsTarget
{
	/** The target graphics object. */
	volatile Graphics _target;
	
	/**
	 * Initializes the target graphics.
	 * 
	 * @param __g The graphics to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/25
	 */
	public ProxyGraphicsTarget(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this._target = __g;
	}
	
	/**
	 * Sets the graphics to draw onto.
	 * 
	 * @param __g The graphics to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/25
	 */
	public void setGraphics(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this._target = __g;
	}
}

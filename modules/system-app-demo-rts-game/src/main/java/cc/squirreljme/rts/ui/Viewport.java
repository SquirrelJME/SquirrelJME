// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

/**
 * This manages a viewport, or a player's view into a specific game.
 *
 * @since 2026/06/11
 */
public class Viewport
{
	/** The screen X coordinate of this viewport. */ 
	protected volatile int screenX;
	
	/** The screen Y coordinate of this viewport. */
	protected volatile int screenY;
	
	/** The map X coordinate being viewed. */
	protected volatile int mapX;
	
	/** The map Y coordinate being viewed. */
	protected volatile int mapY;
}

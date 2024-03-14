// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import java.awt.GraphicsDevice;

/**
 * Not Described.
 *
 * @since 2024/03/14
 */
public class SwingScreenObject
	implements ScritchScreenBracket
{
	/** The AWT screen used. */
	protected final GraphicsDevice awtScreen;
	
	/**
	 * Initializes the Swing screen.
	 *
	 * @param __awtScreen The AWT screen this is on top of.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/14
	 */
	public SwingScreenObject(GraphicsDevice __awtScreen)
		throws NullPointerException
	{
		if (__awtScreen == null)
			throw new NullPointerException("NARG");
		
		this.awtScreen = __awtScreen;
	}
	
	/**
	 * Returns the screen ID.
	 *
	 * @return The screen ID.
	 * @since 2024/03/14
	 */
	public int id()
	{
		return this.awtScreen.getIDstring().hashCode();
	}
	
	/**
	 * Returns the screen height.
	 *
	 * @return The screen height.
	 * @since 2024/03/14
	 */
	public int height()
	{
		return this.awtScreen.getDisplayMode().getHeight();
	}
	
	/**
	 * Returns the screen width.
	 *
	 * @return The screen width.
	 * @since 2024/03/14
	 */
	public int width()
	{
		return this.awtScreen.getDisplayMode().getWidth();
	}
}

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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.GraphicsDevice;
import java.awt.Insets;
import java.awt.Toolkit;

/**
 * Not Described.
 *
 * @since 2024/03/14
 */
@Deprecated
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
		GraphicsDevice awtScreen = this.awtScreen;
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
			awtScreen.getDefaultConfiguration());
		
		return awtScreen.getDisplayMode().getHeight() +
			(insets.bottom + insets.top);
	}
	
	/**
	 * Returns the screen width.
	 *
	 * @return The screen width.
	 * @since 2024/03/14
	 */
	public int width()
	{
		GraphicsDevice awtScreen = this.awtScreen;
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
			awtScreen.getDefaultConfiguration());
		
		return awtScreen.getDisplayMode().getWidth() -
			(insets.right + insets.left);
	}
}

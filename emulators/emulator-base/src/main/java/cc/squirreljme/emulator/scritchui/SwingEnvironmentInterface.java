// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Swing based environment interface for ScritchUI.
 *
 * @since 2024/03/13
 */
@Deprecated
public class SwingEnvironmentInterface
	implements ScritchEnvironmentInterface
{
	/** Look and feel interface. */
	protected final ScritchLAFInterface lookAndFeel =
		new SwingLAFInterface();
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public boolean isInhibitingSleep()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public ScritchLAFInterface lookAndFeel()
	{
		return this.lookAndFeel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public ScritchScreenBracket[] screens()
	{
		GraphicsEnvironment gfxEnv =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		// Default screen, make sure it is first later on
		GraphicsDevice defScreen = gfxEnv.getDefaultScreenDevice();
		
		// Process all attached screens
		List<ScritchScreenBracket> result = new ArrayList<>(); 
		for (GraphicsDevice awtScreen : gfxEnv.getScreenDevices())
		{
			// Wrap screen
			SwingScreenObject screen = new SwingScreenObject(awtScreen);
			
			// Store into the list
			if (awtScreen.equals(defScreen) ||
				Objects.equals(defScreen.getIDstring(),
					awtScreen.getIDstring()))
				result.add(0, screen);
			else
				result.add(screen);
		}
		
		return result.toArray(new ScritchScreenBracket[result.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public void setInhibitSleep(boolean __inhibit)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int windowManagerType()
	{
		throw Debugging.todo();
	}
	
	@Override
	public @NotNull PencilFontBracket[] builtinFonts()
	{
		throw Debugging.todo();
	}
	
	@Override
	public @NotNull PencilFontBracket fontDerive(
		@NotNull PencilFontBracket __font, int __style,
		@Range(from = 1, to = Integer.MAX_VALUE) int __pixelSize)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}

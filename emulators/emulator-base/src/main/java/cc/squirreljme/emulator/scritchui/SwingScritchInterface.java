// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Swing implementation of ScritchUI.
 *
 * @since 2024/03/01
 */
public class SwingScritchInterface
	implements ScritchInterface
{
	/** The environment interface. */
	protected final SwingEnvironmentInterface environment =
		new SwingEnvironmentInterface();
	
	/** Screen interface. */
	protected final ScritchScreenInterface screen =
		new SwingScreenInterface();
	
	/** Window interface. */
	protected final ScritchWindowInterface window =
		new SwingWindowInterface();
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/07
	 */
	@Override
	public ScritchEnvironmentInterface environment()
	{
		return this.environment;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/12
	 */
	@Override
	public ScritchScreenInterface screen()
	{
		return this.screen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/12
	 */
	@Override
	public ScritchWindowInterface window()
	{
		return this.window;
	}
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Swing implementation of ScritchUI.
 *
 * @since 2024/03/01
 */
@Deprecated
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
	
	/** The interface for panels. */
	protected final ScritchPanelInterface panel =
		new SwingPanelInterface();
	
	/** The interface for components. */
	protected final ScritchComponentInterface component =
		new SwingComponentInterface();
	
	/** The interface for containers. */
	protected final ScritchContainerInterface container =
		new SwingContainerInterface();
	
	/** Event loop interface. */
	protected final ScritchEventLoopInterface eventLoop =
		new SwingEventLoopInterface();
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/16
	 */
	@Override
	public ScritchComponentInterface component()
	{
		return this.component;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/16
	 */
	@Override
	public ScritchContainerInterface container()
	{
		return this.container;
	}
	
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
	 * @since 2024/03/16
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchEventLoopInterface eventLoop()
	{
		return this.eventLoop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/16
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchPanelInterface panel()
	{
		return this.panel;
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

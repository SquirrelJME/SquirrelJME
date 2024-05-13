// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.scritchui.fb.panel.FramebufferRawPanelInterface;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Framebuffer implementation of ScritchUI.
 *
 * @since 2024/03/07
 */
@Deprecated
@SquirrelJMEVendorApi
public class FramebufferScritchInterface
	implements ScritchInterface
{
	/** The core scritch interface providing basic means. */
	@SquirrelJMEVendorApi
	protected final ScritchInterface coreInterface;
	
	/** Component interface. */
	@SquirrelJMEVendorApi
	protected final ScritchComponentInterface component;
	
	/** Container interface. */
	@SquirrelJMEVendorApi
	protected final ScritchContainerInterface container;
	
	/** Environment interface. */
	@SquirrelJMEVendorApi
	protected final ScritchEnvironmentInterface environment;
	
	/** Event loop interface. */
	@SquirrelJMEVendorApi
	protected final ScritchEventLoopInterface eventLoop;
	
	/** Panel interface. */
	@SquirrelJMEVendorApi
	protected final ScritchPanelInterface panel;
	
	/** Screen interface. */
	@SquirrelJMEVendorApi
	protected final ScritchScreenInterface screen;
	
	/** Window interface. */
	@SquirrelJMEVendorApi
	protected final ScritchWindowInterface window;
	
	/**
	 * Initializes the framebuffer interface.
	 *
	 * @param __provider The provider for framebuffer screens.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	public FramebufferScritchInterface(FramebufferScreensProvider __provider)
		throws NullPointerException
	{
		this(new FramebufferRawPanelInterface(__provider));
	}
	
	/**
	 * Panel only interface.
	 *
	 * @param __core The panels only interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferScritchInterface(ScritchInterface __core)
		throws NullPointerException
	{
		if (__core == null)
			throw new NullPointerException("NARG");
		
		this.coreInterface = __core;
		
		// Reference to self so we can garbage collect properly
		Reference<FramebufferScritchInterface> self =
			new WeakReference<>(this);
		
		this.component = new FramebufferComponentInterface(self, __core);
		this.container = new FramebufferContainerInterface(self, __core);
		this.eventLoop = new FramebufferEventLoopInterface(self, __core);
		this.environment = new FramebufferEnvironmentInterface(self, __core);
		this.panel = new FramebufferPanelInterface(self, __core);
		this.screen = new FramebufferScreenInterface(self, __core);
		this.window = new FramebufferWindowInterface(self, __core);
	}
	
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
	@SquirrelJMEVendorApi
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
	 * @since 2024/03/10
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchScreenInterface screen()
	{
		return this.screen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/10
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchWindowInterface window()
	{
		return this.window;
	}
}

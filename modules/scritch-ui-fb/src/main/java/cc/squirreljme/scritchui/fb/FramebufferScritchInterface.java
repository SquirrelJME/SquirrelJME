// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Framebuffer implementation of ScritchUI.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public class FramebufferScritchInterface
	implements ScritchInterface
{
	/** The provider for screens. */
	protected final FramebufferScreensProvider provider;
	
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
		if (__provider == null)
			throw new NullPointerException("NARG");
		
		this.provider = __provider;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchEnvironmentInterface environment()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/10
	 */
	@Override
	@SquirrelJMEVendorApi
	public @NotNull ScritchScreenInterface screen()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/10
	 */
	@Override
	@SquirrelJMEVendorApi
	public @NotNull ScritchWindowInterface window()
	{
		throw Debugging.todo();
	}
}

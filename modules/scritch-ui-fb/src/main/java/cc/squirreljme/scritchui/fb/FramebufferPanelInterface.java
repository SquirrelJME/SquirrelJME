// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for panels.
 *
 * @since 2024/03/24
 */
@SquirrelJMEVendorApi
public class FramebufferPanelInterface
	extends FramebufferBaseInterface
	implements ScritchPanelInterface
{
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferPanelInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
		throws NullPointerException
	{
		super(__self, __core);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void enableFocus(ScritchPanelBracket __panel, boolean __enabled)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public ScritchPanelBracket newPanel()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void repaint(ScritchComponentBracket __component)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void setPaintListener(ScritchComponentBracket __component,
		ScritchPaintListener __listener)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}

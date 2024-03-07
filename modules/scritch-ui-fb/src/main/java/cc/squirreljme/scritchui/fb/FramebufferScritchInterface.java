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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Framebuffer implementation of ScritchUI.
 *
 * @since 2024/03/07
 */
public class FramebufferScritchInterface
	implements ScritchInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/07
	 */
	@Override
	public ScritchEnvironmentInterface environment()
	{
		throw Debugging.todo();
	}
}

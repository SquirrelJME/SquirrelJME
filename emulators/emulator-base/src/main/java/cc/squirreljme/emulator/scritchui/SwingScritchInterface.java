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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Swing implementation of ScritchUI.
 *
 * @since 2024/03/01
 */
public class SwingScritchInterface
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

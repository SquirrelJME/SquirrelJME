// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Terminate the game if the window is closed.
 *
 * @since 2026/06/10
 */
public class TerminateGame
	implements ScritchCloseListener
{
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public boolean closed(ScritchWindowBracket __window)
		throws MLECallError
	{
		// Just quit
		System.exit(0);
		return true;
	}
}

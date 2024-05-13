// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;

/**
 * Callback for when a display is closed.
 *
 * @since 2024/05/13
 */
class __ExecDisplayClose__
	implements ScritchCloseListener
{
	/** The display state this is linked to. */
	protected final DisplayState state;
	
	/**
	 * Initializes the callback.
	 *
	 * @param __state The state this is linked to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/13
	 */
	__ExecDisplayClose__(DisplayState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/13
	 */
	@Override
	public boolean closed(ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}

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
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import cc.squirreljme.runtime.midlet.ApplicationInterface;
import javax.microedition.midlet.MIDlet;

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
	@SuppressWarnings("unchecked")
	@Override
	public boolean closed(ScritchWindowBracket __window)
		throws MLECallError
	{
		/* Debug. */
		Debugging.debugNote("TODO: Check for exit command!");
		
		// Obtain the application we are actually running, since this
		// could be done different for different ones
		Object currentInstance = ApplicationHandler.currentInstance();
		ApplicationInterface<Object> currentInterface =
			(ApplicationInterface<Object>)
				ApplicationHandler.currentInterface();
		if (currentInstance != null && currentInterface != null)
		{
			// Request destruction, ignore any failures
			try
			{
				currentInterface.destroy(currentInstance, null);
			}
			catch (Throwable __e)
			{
				__e.printStackTrace();
			}
			
			// Application should terminate!
			return false;
		}
		
		// Not cancelling termination
		return true;
	}
}

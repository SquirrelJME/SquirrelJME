// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchActivateListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Called when the list is activated.
 *
 * @since 2024/07/28
 */
class __ExecListActivate__
	implements ScritchActivateListener
{
	/** The list being activated. */
	protected final Reference<List> list;
	
	/**
	 * Initializes the listener.
	 *
	 * @param __list The list to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/28
	 */
	__ExecListActivate__(List __list)
		throws NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		this.list = new WeakReference<>(__list);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public void activate(ScritchComponentBracket __component)
	{
		// Ignore if the list was garbage collected
		List list = this.list.get();
		if (list == null)
			return;
		
		// Forward here
		CommandListener listener = list.__getCommandListener();
		if (listener != null)
			listener.commandAction(list._selCommand, list);
	}
}

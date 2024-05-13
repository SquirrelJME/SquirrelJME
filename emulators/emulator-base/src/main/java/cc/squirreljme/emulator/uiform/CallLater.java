// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Used to execute a {@link Runnable} at some point in the future via the
 * event loop.
 *
 * @since 2020/10/03
 */
@Deprecated
public class CallLater
	implements Runnable
{
	/** The display identifier. */
	private final SwingDisplay display;
	
	/** The serial identifier. */
	private final int serialId;
	
	/**
	 * Initializes the call later.
	 * 
	 * @param __displayId The display ID.
	 * @param __serialId The serial ID.
	 * @since 2020/10/03
	 */
	public CallLater(SwingDisplay __displayId, int __serialId)
	{
		this.display = __displayId;
		this.serialId = __serialId;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void run()
	{
		SwingDisplay display = this.display;
		UIDisplayCallback callback = display.callback();
		if (callback != null)
			try
			{
				callback.later(display, this.serialId);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
	}
}

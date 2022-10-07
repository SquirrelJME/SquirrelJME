// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
public class CallLater
	implements Runnable
{
	/** The display identifier. */
	private final int displayId;
	
	/** The serial identifier. */
	private final int serialId;
	
	/**
	 * Initializes the call later.
	 * 
	 * @param __displayId The display ID.
	 * @param __serialId The serial ID.
	 * @since 2020/10/03
	 */
	public CallLater(int __displayId, int __serialId)
	{
		this.displayId = __displayId;
		this.serialId = __serialId;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void run()
	{
		// Determine the set of candidates to call
		List<UIDisplayCallback> candidates = new LinkedList<>();
		synchronized (SwingFormShelf.class)
		{
			for (Iterator<Map.Entry<Reference<?>, UIDisplayCallback>> it =
				 SwingFormShelf._DISPLAY_CALLBACKS.entrySet().iterator();
				 it.hasNext();)
			{
				Map.Entry<Reference<?>, UIDisplayCallback> entry = it.next();
				
				// Has been GCed? Drop it if so
				if (entry.getKey().get() == null)
				{
					it.remove();
					continue;
				}
				
				// Otherwise try a call later
				candidates.add(entry.getValue());
			}
		}
		
		// These are read many times
		int displayId = this.displayId;
		int serialId = this.serialId;
		
		// Call every candidate that is possible
		for (UIDisplayCallback candidate : candidates)
			try
			{
				candidate.later(displayId, serialId);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
	}
}

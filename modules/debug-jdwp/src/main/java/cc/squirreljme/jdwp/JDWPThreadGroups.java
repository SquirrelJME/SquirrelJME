// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Thread groups for JDWP access.
 *
 * @since 2021/03/13
 */
public final class JDWPThreadGroups
{
	/** Thread group mapping. */
	private final Map<Integer, JDWPThreadGroup> _groups =
		new LinkedHashMap<>();
	
	/**
	 * Binds the object to the given group.
	 * 
	 * @param __v The object to bind.
	 * @return The thread group binding.
	 * @throws NullPointerException
	 */
	public JDWPThreadGroup bind(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		Map<Integer, JDWPThreadGroup> groups = this._groups;
		synchronized (this)
		{
			int id = System.identityHashCode(__v);
			
			// Create it if it is not yet set?
			JDWPThreadGroup rv = groups.get(id);
			if (rv == null)
				groups.put(id, (rv = new JDWPThreadGroup(__v)));
			
			return rv;
		}
	}
	
	/**
	 * Returns the current thread groups.
	 * 
	 * @return The current thread groups.
	 * @since 2021/03/13
	 */
	public JDWPThreadGroup[] current()
	{
		Map<Integer, JDWPThreadGroup> groups = this._groups;
		synchronized (this)
		{
			return groups.values().toArray(new JDWPThreadGroup[groups.size()]);
		}
	}
}

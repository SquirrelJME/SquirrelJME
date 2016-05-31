// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is used to generate unique identifiers.
 *
 * @since 2016/05/31
 */
final class __IdentifierGenerator__<I extends __Identifiable__>
{
	/** The identifier list. */
	private final List<I> _idlist;
	
	/** The initial ID. */
	private volatile int _id =
		1;
	
	/**
	 * Initializes the identifier generator.
	 *
	 * @param __l The list that contains existing IDs.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/31
	 */
	__IdentifierGenerator__(List<I> __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._idlist = __l;
	}
	
	/**
	 * Finds the next free ID that is available for usage.
	 *
	 * @param __idl The list of identifiable to find an ID for.
	 * @throws KernelException If an ID could not be found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/29
	 */
	private final int __nextIdentifiableId(
		List<I> __idl)
		throws KernelException, NullPointerException
	{
		// Check
		if (__idl == null)
			throw new NullPointerException("NARG");
		
		// Go through all the sorted IDs to find an unused ID
		int at = 1;
		for (Iterator<I> it = __idl.iterator();
			it.hasNext();)
		{
			// Obtain the given identifiable ID
			int tid = it.next().id();
			
			// Place here?
			if (at < tid)
				return at;
			
			// Try the next ID
			at++;
		}
		
		// {@squirreljme.error AY06 Could not find a free ID that
		// is available for usage.}
		throw new KernelException("AY06");
	}
	
	/**
	 * Returns the next identifier.
	 *
	 * @return The next identifier.
	 * @since 2016/05/31
	 */
	final int __next()
	{
		// Lock on list
		List<I> idlist = this._idlist;
		synchronized (idlist)
		{
			// Determine the next value
			int next = _id;
			
			// Overflows? Find an ID that is positive and not used
			if (next < 0 || next == Integer.MAX_VALUE)
			{
				int rv = __nextIdentifiableId(idlist);
				return rv;
			}
			
			// Set next to use next time
			else
				_id = next + 1;
			
			// Use it
			return next;
		}
	}
}


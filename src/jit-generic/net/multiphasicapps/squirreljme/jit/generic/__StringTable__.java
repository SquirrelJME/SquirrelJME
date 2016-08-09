// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This stores the string table which is used to combine and share strings
 * across the entirety of the namespace.
 *
 * @since 2016/08/09
 */
class __StringTable__
{
	/** The internal table. */
	protected final Map<String, __Ref__> table =
		new HashMap<>();
	
	/**
	 * Initializes the string table.
	 *
	 * @since 2016/08/09
	 */
	__StringTable__()
	{
		// Add a blank string always
		__add("");
	}
	
	/**
	 * Adds the specified string to the table.
	 *
	 * @param __s The string to add.
	 * @return The reference to the string.
	 * @throws JITException If there would be more than 65,536 strings.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	final __StringTable__.__Ref__ __add(String __s)
		throws JITException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA0q Classes may only have 65,536 strings.}
		Map<String, __Ref__> table = this.table;
		if (table.size() >= 65535)
			throw new JITException("BA0q");
		
		// If the string is not in the table then add it
		__Ref__ rv = table.get(__s);
		if (rv == null)
			table.put(__s, (rv = new __Ref__(__s)));
		
		// Return it
		return rv;
	}
	
	/**
	 * This contains a string reference which is later used to refer to a
	 * string in a given table. The reference permits the string table to be
	 * sorted before being placed in the binary.
	 *
	 * @since 2016/08/09
	 */
	static final class __Ref__
		implements Comparable<__Ref__>
	{
		/** The contained string. */
		protected final String strig;
		
		/**
		 * Initializes the string reference.
		 *
		 * @param __s The string to store.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/08/09
		 */
		private __Ref__(String __s)
			throws NullPointerException
		{
			// Check
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.string;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/09
		 */
		@Override
		public boolean equals(Object __o)
		{
			// Compare against self
			if (__o instanceof __Ref__)
				return this == __o;
			
			// Not a match;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/09
		 */
		@Override
		public int hashCode()
		{
			return this.string.hashCode();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/09
		 */
		@Override
		public String toString()
		{
			return this.string;
		}
	}
}


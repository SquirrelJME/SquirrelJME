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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This stores the string table which is used to combine and share strings
 * across the entirety of the namespace.
 *
 * @since 2016/08/09
 */
class __StringTable__
	extends __NamespaceOwned__
{
	/** The internal table. */
	protected final Map<String, Integer> table =
		new HashMap<>();
	
	/** Deferring write index. */
	private volatile int _defer =
		0;
	
	/**
	 * Initializes the string table.
	 *
	 * @param __nsw The owning namespace.
	 * @since 2016/08/09
	 */
	__StringTable__(GenericNamespaceWriter __nsw)
	{
		super(__nsw);
		
		// Add a blank string always
		__add("");
	}
	
	/**
	 * Adds the specified string to the table.
	 *
	 * @param __s The string to add.
	 * @return The index of the string.
	 * @throws JITException If there would be more than 65,536 strings.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	final int __add(String __s)
		throws JITException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA0q Classes may only have 65,536 strings.}
		Map<String, Integer> table = this.table;
		int end;
		if ((end = table.size()) >= 65535)
			throw new JITException("BA0q");
		
		// If the string is not in the table then add it
		Integer rv = table.get(__s);
		if (rv == null)
			table.put(__s, (rv = end));
		
		// Return it
		return rv.intValue();
	}
	
	/**
	 * Writes deferred strings to the output stream.
	 *
	 * @param __dos The stream to output to.
	 * @throws IOException On write errors.
	 * @since 2016/08/12
	 */
	final void __defer(ExtendedDataOutputStream __dos)
		throws IOException
	{
		if (false)
			throw new IOException("TODO");
		throw new Error("TODO");
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
		protected final String string;
		
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
			this.string = __s;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/09
		 */
		@Override
		public int compareTo(__Ref__ __o)
		{
			return this.string.compareTo(__o.string);
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


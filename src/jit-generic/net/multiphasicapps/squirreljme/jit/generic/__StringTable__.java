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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;

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
	protected final LinkedHashMap<String, Integer> table =
		new LinkedHashMap<>();
	
	/** The write positions of all strings. */
	protected final List<Integer> writepos =
		new LinkedList<>();
	
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
		
		// Since in general having strings this large is a very bad idea and is
		// rare to begin with...
		// {@squirreljme.error BA0v The length of strings is limited to
		// 65,535 characters.}
		if (__s.length() > 65535)
			throw new JITException("BA0v");
		
		// {@squirreljme.error BA0q Classes may only have 65,536 strings.}
		Map<String, Integer> table = this.table;
		int end;
		if ((end = table.size()) > 65535)
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
		// Check if entries actually need to be written
		int defer = this._defer;
		LinkedHashMap<String, Integer> table = this.table;
		int n = table.size();
		if (defer >= n)
			return;
		
		// Get
		ExtendedDataOutputStream dos = this.owner.__output();
		List<Integer> writepos = this.writepos;
		
		// Write entries
		Iterator<String> it = table.keySet().iterator();
		for (int i = 0; i < n; i++)
		{
			// Ignore strings which were already written
			String s = it.next();
			if (i < defer)
				continue;
			
			// Align to 4
			while ((dos.size() & 3) != 0)
				dos.writeByte(0);
			
			// {@squirreljme.error BA0u Position of string exceeds the range
			// of 2GiB.}
			long sz = dos.size();
			if (sz < 0 || sz > Integer.MAX_VALUE)
				throw new JITException("BA0u");
			writepos.add((int)sz);
			
			// Find the highest valued character in the string
			int l = s.length();
			int highval = 0;
			boolean wide = false;
			for (int j = 0; j < l; j++)
			{
				char c = s.charAt(j);
				if (c > highval)
				{
					highval = c;
					
					// If two bytes, stop since that is the max cell size
					if (highval > 255)
					{
						wide = true;
						break;
					}
				}
			}
			
			// Write the string
			if (wide)
				__writeWide(dos, s);
			else
				__writeNarrow(dos, s);
			
			// Defer the next string
			defer = i + 1;
		}
		
		// Set
		this._defer = defer;
	}
	
	/**
	 * Writes a narrow string to the output, which is one byte wide.
	 *
	 * @param __dos The target stream.
	 * @param __s The string to print.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/12
	 */
	private void __writeNarrow(ExtendedDataOutputStream __dos, String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Write characters
		int n = __s.length();
		__dos.writeShort(1);
		__dos.writeShort(n);
		for (int i = 0; i < n; i++)
			__dos.writeByte(__s.charAt(i));
	}
	
	/**
	 * Writes a wide string to the output, which is two byte wides.
	 *
	 * @param __dos The target stream.
	 * @param __s The string to print.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/12
	 */
	private void __writeWide(ExtendedDataOutputStream __dos, String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Write characters
		int n = __s.length();
		__dos.writeShort(2);
		__dos.writeShort(n);
		for (int i = 0; i < n; i++)
			__dos.writeShort(__s.charAt(i));
	}
}


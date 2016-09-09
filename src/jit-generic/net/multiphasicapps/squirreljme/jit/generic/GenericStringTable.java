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
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This is the string table which is used within a namespace, this contains
 * every string that is used.
 *
 * @since 2016/09/09
 */
public final class GenericStringTable
{
	/** The owning namespace writer. */
	protected final GenericNamespaceWriter owner;
	
	/** Strings in the namespace. */
	private final Map<String, GenericString> _strings =
		new LinkedHashMap<>();
	
	/** The string table position. */
	volatile int _stringpos;
	
	/** The string table count. */
	volatile int _stringcount;
	
	/**
	 * Initializes the string table.
	 *
	 * @param __nsw The owning namespace writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	GenericStringTable(GenericNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
	}
	
	/**
	 * Loads a string into the string table.
	 *
	 * @param __s The string to load.
	 * @return The representing string entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	public GenericString getString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Already placed?
		Map<String, GenericString> strings = this._strings;
		GenericString rv = strings.get(__s);
		if (rv != null)
			return rv;
		
		// {@squirreljme.error BA17 The number of strings exceeds 65,536.}
		int sz = strings.size();
		if (sz >= 65535)
			throw new JITException("BA17");
		
		// Place it otherwise
		strings.put(__s, (rv = new GenericString(sz)));
		return rv;
	}
	
	/**
	 * Writes the string data.
	 *
	 * @param __dos The target stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	void __writeStrings(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Needed to align
		GenericNamespaceWriter owner = this.owner;
		
		// Write the string data
		Map<String, GenericString> strings = this._strings;
		int sn = strings.size();
		this._stringcount = sn;
		int[] spos = new int[sn];
		int at = 0;
		for (Map.Entry<String, GenericString> e : strings.entrySet())
		{
			// Align and position
			owner.__align();
			spos[at++] = (int)__dos.size();
			
			// Write string data
			owner.__writeString(__dos, 0, e.getKey());
		}
		
		// Align
		owner.__align();
		
		// Write the string table
		this._stringpos = (int)__dos.size();
		int ns = GenericBlob.NAMESPACE_SHIFT;
		for (int i = 0; i < sn; i++)
			__dos.writeShort(spos[i] >>> ns);
	}
}


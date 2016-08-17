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
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITConstantPool;

/**
 * This is the global constant pool which is shared among all classes within
 * a single namespace.
 *
 * @since 2016/08/17
 */
final class __GlobalPool__
{
	/** The owning namespace writer. */
	protected final GenericNamespaceWriter owner;
	
	/** Strings in the namespace. */
	private final Map<String, __StringEntry__> _strings =
		new HashMap<>();
	
	/** The currently active pool. */
	private volatile JITConstantPool _current;
	
	/** The next string index. */
	private volatile int _nextstring;
	
	/**
	 * Initializes the global pool.
	 *
	 * @param __nsw The namespace writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	__GlobalPool__(GenericNamespaceWriter __nsw)
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
	__StringEntry__ __loadString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Already placed?
		Map<String, __StringEntry__> strings = this._strings;
		__StringEntry__ rv = strings.get(__s);
		if (rv != null)
			return rv;
		
		// Place it otherwise
		strings.put(__s, (rv = new __StringEntry__(this._nextstring++)));
		return rv;
	}
	
	/**
	 * Sets the current constant pool.
	 *
	 * @param __pool The pool to use.
	 * @since 2016/08/17
	 */
	void __setCurrent(JITConstantPool __pool)
	{
		this._current = __pool;
	}
}


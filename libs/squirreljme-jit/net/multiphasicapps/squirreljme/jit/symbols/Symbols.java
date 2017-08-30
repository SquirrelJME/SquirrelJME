// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.symbols;

import java.util.Map;
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains the manager for symbols.
 *
 * This class is thread safe.
 *
 * @since 2017/08/24
 */
public class Symbols
{
	/** Classes which have been declared. */
	private final Map<ClassName, ClassStructure> _classes =
		new SortedTreeMap<>();
	
	/**
	 * Creates a new class structure.
	 *
	 * @param __cn The name of the class to create.
	 * @return The structure for the given class.
	 * @throws JITException If the class already has a structure on it.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public final ClassStructure createClass(ClassName __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Make this thread safe so that classes can be added concurrently
		// in the event concurrent processing of JARs is supported
		Map<ClassName, ClassStructure> classes = this._classes;
		synchronized (classes)
		{
			// {@squirreljme.error JI27 Could not create the class structure
			// because it has already been created. (The name of the class)}
			if (classes.containsKey(__cn))
				throw new JITException(String.format("JI27 %s", __cn));
		
			ClassStructure rv = new ClassStructure(__cn);
			classes.put(__cn, rv);
			return rv;
		}
	}
}


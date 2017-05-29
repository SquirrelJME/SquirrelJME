// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a single compiled class which contains fields and methods
 * for exporting to native executables or to active memory. This is the end
 * result of all JIT operations for a single class. It also includes any
 * needed imports.
 *
 * @since 2017/04/02
 */
public class CompiledClass
{
	/** The name of the exported class. */
	protected final ClassNameSymbol name;
	
	/** The flags for the exported class. */
	protected final ClassFlags flags;
	
	/** The class being extended. */
	protected final ClassNameSymbol extending;
	
	/** The classes this class implements. */
	private final Set<ClassNameSymbol> _implements =
		new LinkedHashSet<>();
	
	/** Linkages (imports). */
	private final Map<Linkage, Integer> _links =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the compiled class which exports the given class.
	 *
	 * @param __n The name of the exported class.
	 * @param __f The flags for the exported class.
	 * @param __ext The class being extended.
	 * @param __imp The classes this implements.
	 * @throws JITException If an interface has been duplicated.
	 * @throws NullPointerException On null arguments except for {@code __sn}.
	 * @since 2017/04/02
	 */
	public CompiledClass(ClassNameSymbol __n, ClassFlags __f,
		ClassNameSymbol __ext, ClassNameSymbol... __imp)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null || __f == null || __imp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.flags = __f;
		this.extending = __ext;
		
		// Store implemented interfaces
		Set<ClassNameSymbol> imps = this._implements;
		for (ClassNameSymbol i : __imp)
		{
			// Check
			if (i == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ0q An interface has been duplicated.
			// (The duplicated interface)}
			if (!imps.add(i))
				throw new JITException(String.format("AQ0q %s", i));
		}
	}
	
	/**
	 * Returns the flags of the exported class.
	 *
	 * @return The exported class flags.
	 * @since 2017/04/02
	 */
	public ClassFlags flags()
	{
		return this.flags;
	}
	
	/**
	 * Adds the specified linkage to the link table or if it is already linked
	 * it returns the existing link.
	 *
	 * @param __l The linkage to add.
	 * @return The link index in the link table.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/02
	 */
	public int link(Linkage __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Only link once
		Map<Linkage, Integer> links = this._links;
		Integer rv = links.get(__l);
		if (rv != null)
			return rv;
		
		// Add to the table
		links.put(__l, (rv = links.size()));
		return rv;
	}
	
	/**
	 * Returns the name of the exported class.
	 *
	 * @return The exported class name.
	 * @since 2017/04/02
	 */
	public ClassNameSymbol name()
	{
		return this.name;
	}
}


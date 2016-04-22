// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.library.NLClass;

/**
 * This represents a class which is loaded by the interpreter.
 *
 * @since 2016/04/21
 */
public class InterpreterClass
{
	/** The interpreter core. */
	protected final InterpreterCore core;
	
	/** The based class (if {@code null} is a virtual class). */
	protected final NLClass base;
	
	/** Is this class fully loaded? */
	protected final boolean loaded;
	
	/**
	 * Initializes an interpreted class.
	 *
	 * @param __core The core.
	 * @param __base The class to base off.
	 * @param __cns The name of the class.
	 * @param __tm The map to place a partially loaded class within.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	InterpreterClass(InterpreterCore __core, NLClass __base,
		ClassNameSymbol __cns,
		Map<ClassNameSymbol, Reference<InterpreterClass>> __tm)
		throws NullPointerException
	{
		// Check
		if (__core == null || __base == null || __tm == null || __cns == null)
			throw new NullPointerException("NARG");
		
		// Set
		core = __core;
		base = __base;
		
		// Place into the given map
		__tm.put(__cns, new WeakReference<>(this));
		
		// Class loaded
		loaded = true;
	}
	
	/**
	 * Is this class fully loaded?
	 *
	 * @return {@code true} if it is loaded.
	 * @since 2016/04/22
	 */
	public boolean isLoaded()
	{
		return loaded;
	}
}


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

/**
 * This represents an object which exists within the virtual machine.
 *
 * @since 2016/04/27
 */
public class NIObject
{
	/** The owning core. */
	protected final NICore core;
	
	/**
	 * Initializes an object (but does not construct it) for usage by the
	 * virtual machine.
	 *
	 * @param __c The owning core of the given object.
	 * @param __cl The class to initialize the object for.
	 * @throws NIException If the object could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NIObject(NICore __c, NIClass __cl)
		throws NIException, NullPointerException
	{
		this(__c, __cl, 0);
	}
	
	/**
	 * Initializes an object (but does not construct it) for usage by the
	 * virtual machine.
	 *
	 * @param __c The owning core of the given object.
	 * @param __cl The class to initialize the object for.
	 * @param __al The length of the array.
	 * @throws NIException If the object could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NIObject(NICore __c, NIClass __cl, int __al)
		throws NIException, NullPointerException
	{
		// Check
		if (__c == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error Ni0j An attempt was made to allocate an array
		// for a class which is not an array. (The class)}
		if (__al != 0 && !__cl.isArray())
			throw new NIException(__c, NIException.Issue.NOT_AN_ARRAY,
				String.format("NI0j %s", __cl.thisName()));
		
		// {@squirreljme.error NI0i Attempted to allocate an array with a
		// negative length. (The array length)}
		if (__al < 0)
			throw new NIException(__c, NIException.Issue.NEGATIVE_ARRAY_LENGTH,
				String.format("NI0i %d", __al));
		
		// {@squirreljme.error NI0h Attempted to initialize an object which
		// is an instance of an abstract class. (The class)}
		if (__cl.flags().isAbstract())
			throw new NIException(__c, NIException.Issue.NEW_ABSTRACT,
				String.format("NI0h %s", __cl.thisName()));
		
		// Set
		core = __c;
		
		throw new Error("TODO");
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a single trace point within.
 *
 * @since 2016/04/15
 */
public class JVMExceptionTrace
{
	/** The class this is in. */
	protected final ClassNameSymbol inclass;
	
	/** The name of the method. */
	protected final IdentifierSymbol name;
	
	/** The descriptor of the method. */
	protected final MethodSymbol descriptor;
	
	/** The PC address. */
	protected final int pcaddr;	
	
	/**
	 * Initializes the exception trace.
	 *
	 * @param __f The frame this is based on.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/15
	 */
	public JVMExceptionTrace(JVMStackFrame __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Get the method
		JVMMethod method = __f.method();
		
		// Set
		inclass = method.outerClass().thisName();
		name = method.name();
		descriptor = method.type();
		pcaddr = __f.getPCAddress();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	public String toString()
	{
		return inclass + "::" + name + ":" + descriptor + " +" + pcaddr;
	}
}


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

import net.multiphasicapps.classprogram.CPComputeMachine;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This is the computational machine which performs the actual logic operations
 * which are needed for the interpreter to actually perform work.
 *
 * @since 2016/04/08
 */
public class JVMComputeMachine
	implements CPComputeMachine<JVMThread, JVMVariable[]>
{
	/** The owning engine. */
	protected final JVMEngine engine;	
	
	/**
	 * Initializes the computing machine.
	 *
	 * @param __e The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/08
	 */
	JVMComputeMachine(JVMEngine __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __e;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws JVMNoClassDefFoundError If the class was not found.
	 * @since 2016/04/09
	 */
	@Override
	public void allocateObject(JVMThread __pa, JVMVariable[] __pb, int __dest,
		ClassNameSymbol __cl)
		throws JVMNoClassDefFoundError
	{
		// Find the class representation
		JVMEngine engine = __pa.threads().engine();
		JVMClass jcl = engine.classes().loadClass(__cl);
		
		// {@squirreljme.error IN0j Could not find a definition for the given
		// class. (The missing class)}
		if (jcl == null)
			throw new JVMNoClassDefFoundError(String.format("IN0j %s", __cl));
		
		// Check if the method can be accessed or not
		if (!__pa.checkAccess(jcl))
			throw new JVMEngineException();
		
		if (true)
			throw new Error("TODO");
		
		// Get the variable at the destination
		
		throw new Error("TODO");
	}
}


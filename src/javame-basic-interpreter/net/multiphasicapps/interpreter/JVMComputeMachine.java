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
	implements CPComputeMachine<JVMStackFrame>
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
	public void allocateObject(JVMStackFrame __frame, int __dest,
		ClassNameSymbol __cl)
		throws JVMNoClassDefFoundError
	{
		// This thread
		JVMThread thread = __frame.thread();
		
		// Find the class representation
		JVMEngine engine = __frame.engine();
		JVMClass jcl = engine.classes().loadClass(__cl);
		
		// {@squirreljme.error IN0j Could not find a definition for the given
		// class. (The missing class)}
		if (jcl == null)
			throw new JVMNoClassDefFoundError(String.format("IN0j %s", __cl));
		
		// Check if the method can be accessed or not
		if (!thread.checkAccess(jcl))
			throw new JVMEngineException();
		
		// Debug
		System.err.printf("DEBUG -- allocateObject(%d, %s)%n", __dest, __cl);
		
		// Allocate a new object of that kind
		JVMObject obj = engine.objects().spawnObject(thread, jcl);
		
		// Store it in the given varaible
		JVMVariable[] vars = __frame.variables();
		JVMVariable var = vars[__dest];
		if (!(var instanceof JVMVariable.OfObject))
			vars[__dest] = (var = JVMVariable.OfObject.empty());
		
		// Set it
		JVMVariable.OfObject vo = (JVMVariable.OfObject)var;
		vo.set(obj);
	}
}


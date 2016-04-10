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

import net.multiphasicapps.classfile.CFMethodFlags;
import net.multiphasicapps.classprogram.CPProgram;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a stack frame which is used to indicate the current method
 * and the position within the method that is currently being executed.
 *
 * @since 2016/04/09
 */
public class JVMStackFrame
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The thread this frame exists in. */
	protected final JVMThread thread;
	
	/** The method the frame is executing. */
	protected final JVMMethod method;
	
	/** Variables that are currently set in this frame. */
	protected final JVMVariable[] vars;
	
	/** The current PC address. */
	private volatile int _pcaddr;
	
	/**
	 * Initializes the stack frame.
	 *
	 * @param __thr The current thread of execution.
	 * @param __in The method currently being executed.
	 * @param __args Method arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	public JVMStackFrame(JVMThread __thr, JVMMethod __in, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__thr == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Must exist
		if (__args == null)
			__args = new Object[0];
		
		// Set
		thread = __thr;
		method = __in;
		
		// Get program here
		CPProgram program = method.program();
		
		// Make sure the right about of arguments were passed
		MethodSymbol desc = method.type();
		int ncargs = desc.argumentCount();
		if (!method.flags().isStatic())
			ncargs += 1;
		
		// {@squirreljme.error IN0g Incorrect number of arguments passed to
		// this method. (The number of method arguments; The number of
		// passed arguments)}
		int inac = __args.length;
		if (inac != ncargs)
			throw new JVMEngineException(String.format("IN0g %d %d",
				ncargs, inac));
		
		// Setup entry variable state
		int numlocals = program.maxLocals();
		int numvars = program.variableCount();
		vars = new JVMVariable[numvars];
		
		// Setup the initial variable state
		__initVariables(vars, numlocals, __args, desc, ncargs);
		
	}
	
	/**
	 * Returns the engine which owns this frame.
	 *
	 * @return The owning engine.
	 * @since 2016/04/09
	 */
	public JVMEngine engine()
	{
		return thread.engine();
	}
	
	/**
	 * Returns the PC address.
	 *
	 * @return The PC address.
	 * @since 2016/04/09
	 */
	public int getPCAddress()
	{
		// Lock
		synchronized (lock)
		{
			return _pcaddr;
		}
	}
	
	/**
	 * Leaves the current stack frame.
	 *
	 * @since 2016/04/09
	 */
	public void leave()
	{
		thread.exitFrame(this);
	}
	
	/**
	 * Returns the curent method beign executed.
	 *
	 * @return The executing method.
	 * @since 2016/04/09
	 */
	public JVMMethod method()
	{
		return method;
	}
	
	/**
	 * Sets the PC address.
	 *
	 * @param __addr The address to start execution at.
	 * @return {@code this}.
	 * @since 2016/04/09
	 */
	public JVMStackFrame setPCAddress(int __addr)
	{
		// Lock
		synchronized (lock)
		{
			_pcaddr = __addr;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the thread which owns this frame.
	 *
	 * @return The thread owning this frame.
	 * @since 2016/04/09
	 */
	public JVMThread thread()
	{
		return thread;
	}
	
	/**
	 * Returns the array of variables which this frame uses.
	 *
	 * @return The array of variables.
	 * @since 2016/04/09
	 */
	public JVMVariable[] variables()
	{
		return vars;
	}
	
	/**
	 * Initializes the variables on entry of a method.
	 *
	 * @param __vars Output variables.
	 * @param __nl The number of local variables.
	 * @param __args Program arguments.
	 * @param __desc Method descriptor.
	 * @param __nia The number of input arguments expected.
	 * @since 2016/04/08
	 */
	private void __initVariables(JVMVariable[] __vars, int __nl,
		Object[] __args, MethodSymbol __desc, int __nia)
	{
		// Classpaths
		JVMClassPath jcp = engine().classes();
		
		// Setup variable
		int vat = 0;
		for (int i = 0; i < __nia; i++)
		{
			// Get method argument from the descriptor and the input
			// argument
			FieldSymbol farg = __desc.get(i);
			Object carg = __args[i];
			
			// Get the system class for the given argument
			JVMClass acl = jcp.loadClass(farg.asClassName());
			
			// Setup wrapped variable
			JVMVariable<?> vw = JVMVariable.wrap(carg);
			
			// Is this possibly the null object?
			boolean isobject = (vw instanceof JVMVariable.OfObject);
			boolean isnullob = (isobject &&
				((JVMVariable.OfObject)vw).get() == null);
			
			// {@squirreljme.error IN0h An input argument which was
			// passed to the method is not of the expected type that
			// the method accepts. (The actual input method argument;
			// The argument class type)}
			// Ignore null though
			if (!isnullob && !acl.isInstance(vw))
				throw new JVMClassCastException(String.format(
					"IN0h %s %s", vw, acl));
			
			// Is this a wide variable?
			boolean iswide = (vw instanceof JVMVariable.OfLong) ||
				(vw instanceof JVMVariable.OfDouble);
			int nextvat = vat + (iswide ? 2 : 1);
			
			// {@squirreljme.error IN0i The number of method arguments
			// would exceed the number of available local variables
			// that exist within a method program. (The current
			// argument count; The maximum local variable count)}
			if (nextvat > __nl)
				throw new JVMEngineException(String.format(
					"IN0i %d %d", nextvat, __nl));
			
			// Store variable
			__vars[vat] = vw;
			vat = nextvat;
		}
	}
}


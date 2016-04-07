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

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import net.multiphasicapps.classfile.CFMethod;
import net.multiphasicapps.classfile.CFMethodFlags;
import net.multiphasicapps.classprogram.CPProgram;
import net.multiphasicapps.classprogram.CPProgramBuilder;
import net.multiphasicapps.classprogram.CPProgramException;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a bound method within a class.
 *
 * @since 2016/04/04
 */
public class JVMMethod
	extends JVMMember<MethodSymbol, CFMethodFlags, CFMethod, JVMMethod>
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The current program. */
	private volatile Reference<CPProgram> _program;
	
	/**
	 * Initializes the method.
	 *
	 * @param __o The owning group.
	 * @param __b The base for it.
	 * @since 2016/04/05
	 */
	JVMMethod(JVMMethods __o, CFMethod __b)
	{
		super(__o, __b);
	}
	
	/**
	 * Runs the interpreter logic for the given thread.
	 *
	 * @param __init Is this an class instance or static initializer, if it is
	 * then it is permitted to write to final fields.
	 * @param __thr The thread of execution, if {@code null} then there is none
	 * or the default thread is implied.
	 * @param __args The arguments to the call of the method.
	 * @throws JVMEngineException Any thrown exceptions are either handled or
	 * propogated upwards.
	 * @since 2016/04/07
	 */
	public void interpret(boolean __init, JVMThread __thr, Object... __args)
		throws JVMEngineException
	{
		// Force arguments to exist
		if (__args == null)
			__args = new Object[0];
		
		// If no thread is specified, then 
		if (__thr == null)
			__thr = engine().threads().defaultThread();
		
		// On entry of a method, add this method to the call stack.
		Deque<JVMMethod> callstack = __thr.stackTrace();
		try
		{
			// Add it to the call stack
			synchronized (callstack)
			{
				callstack.offerLast(this);
			}
			
			// Debug
			System.err.printf("DEBUG -- Interpret %s (%s)%n", this, __init);
			
			// Get program here
			CPProgram program = program();
			
			// Make sure the right about of arguments were passed
			MethodSymbol desc = type();
			int ncargs = desc.argumentCount();
			if (!flags().isStatic())
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
			JVMVariable[] vars = new JVMVariable[numvars];
			
			// Setup the initial variable state
			{
				// Classpaths
				JVMClassPath jcp = engine().classes();
				
				// Setup variable
				int vat = 0;
				for (int i = 0; i < ncargs; i++)
				{
					// Get method argument from the descriptor and the input
					// argument
					FieldSymbol farg = desc.get(i);
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
					if (nextvat > numlocals)
						throw new JVMEngineException(String.format(
							"IN0i %d %d", nextvat, numlocals));
					
					// Store variable
					vars[vat] = vw;
					vat = nextvat;
				}
			}
			
			// Keep executing until a return is reached or an unhandled
			// exception is done.
			for (int pc = 0;;)
			{
				throw new Error("TODO");
			}
		}
		
		// When execution terminates, remove the top stack item.
		finally
		{
			// Remove it from the callstack
			synchronized (callstack)
			{
				// {@squirreljme.error IN0e The callstack for the thread has
				// potentially been corrupted as the last item on the stack is
				// not the current method. (The current method; The method
				// which was at the top of the call stack)}
				JVMMethod was;
				if (this != (was = callstack.pollLast()))
					throw new JVMEngineException(String.format("IN0e %s %s",
						this, was));
			}
		}
	}
	
	/**
	 * Returns the program of the current method.
	 *
	 * @return The method's program.
	 * @since 2016/04/06
	 */
	public CPProgram program()
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<CPProgram> ref = _program;
			CPProgram rv;
			
			// Needs loading?
			if (ref == null || null == (rv = ref.get()))
				try (InputStream is = base.codeAttribute())
				{
					// {@squirreljme.error IN0a The current method has no
					// defined program, it is likely {@code abstract} or
					// {@code native}. (The current method)}
					if (is == null)
						throw new JVMClassFormatError(String.format("IN0a %s",
							this));
					
					// Load it
					_program = new WeakReference<>((rv = new CPProgramBuilder(
						container().outerClass().base(), base).parse(is)));
				}
				
				// Failed to load program
				catch (CPProgramException|IOException e)
				{
					// {@squirreljme.error IN09 Could not get the program for
					// the current method either because it does not exist or
					// it is not a valid program. (The current method)}
					throw new JVMClassFormatError(String.format("IN09 %s",
						this), e);
				}
			
			return rv;
		}
	}
}


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

import java.util.Arrays;
import net.multiphasicapps.classfile.CFMethodFlags;
import net.multiphasicapps.classprogram.CPProgram;
import net.multiphasicapps.classprogram.CPProgramException;
import net.multiphasicapps.classprogram.CPVariableType;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a stack frame which is used to indicate the current method
 * and the position within the method that is currently being executed.
 *
 * @since 2016/04/09
 */
public class JVMStackFrame
	implements JVMFrameable
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The thread this frame exists in. */
	protected final JVMThread thread;
	
	/** The method the frame is executing. */
	protected final JVMMethod method;
	
	/** Data storage window. */
	protected final JVMDataStore.Window datawindow;
	
	/** Is this an initializer? */
	protected final boolean isinit;
	
	/** Is this in an instance of an object? */
	protected final boolean isinstance;
	
	/** The current PC address. */
	private volatile int _pcaddr;
	
	/** Explicit jump address. */
	private volatile int _jumptarget =
		Integer.MIN_VALUE;
	
	/** Is this frame returning? */
	private volatile boolean _doreturn;
	
	/**
	 * Initializes the stack frame.
	 *
	 * @param __thr The current thread of execution.
	 * @param __in The method currently being executed.
	 * @param __init Is this an initializer?
	 * @param __ds Stack data storage.
	 * @param __args Method arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	public JVMStackFrame(JVMThread __thr, JVMMethod __in, boolean __init,
		JVMDataStore __ds, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__thr == null || __in == null || __ds == null)
			throw new NullPointerException("NARG");
		
		// Must exist
		if (__args == null)
			__args = new Object[0];
		
		// Set
		thread = __thr;
		method = __in;
		isinit = __init;
		
		// Instance?
		isinstance = !method.flags().isStatic();
		
		// Get program here
		CPProgram program;
		try
		{
			program = method.program();
		}
		
		// {@squirreljme.error IN0r Could not load the program.
		// (The current method)}
		catch (CPProgramException e)
		{
			throw new JVMClassFormatError(__thr, String.format(
				"IN0r %s", this), e);
		}
		
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
			throw new JVMEngineException(this, String.format("IN0g %d %d",
				ncargs, inac));
		
		// Setup entry variable state
		int numlocals = program.maxLocals();
		int numvars = program.variableCount();
		
		// Setup window
		JVMDataStore.Window dsw = __ds.pushWindow(numvars);
		datawindow = dsw;
		
		// Setup the initial variable state
		__initVariables(dsw, numlocals, __args, desc, ncargs);
	}
	
	/**
	 * Checks whether the given object can be accessed by this frame.
	 *
	 * @param __o The object to check.
	 * @return {@code true} if it can be accessed.
	 * @since 2016/04/16
	 */
	public boolean checkAccess(JVMAccessibleObject __o)
	{
		return thread().checkAccess(__o);
	}
	
	/**
	 * Clears the jump target.
	 *
	 * @return {@code this}.
	 * @since 2016/04/10
	 */
	public JVMStackFrame clearJumpTarget()
	{
		// Lock
		synchronized (lock)
		{
			_jumptarget = Integer.MIN_VALUE;
			return this; 
		}
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
	 * Returns the jump target of a jump if this performs one and does not
	 * naturally flow to the next instruction.
	 *
	 * @return The jump target, or a negative value if not set.
	 * @since 2016/04/10
	 */
	public int getJumpTarget()
	{
		// Lock
		synchronized (lock)
		{
			int rv = _jumptarget;
			return (rv < 0 ? Integer.MIN_VALUE : rv);
		}
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
	 * Returns the class that the frame is currently being executed from.
	 *
	 * @return The class this method is in.
	 * @since 2016/04/16
	 */
	public JVMClass inClass()
	{
		return method.outerClass();
	}
	
	/**
	 * Is this a static or instance initializer?
	 *
	 * @return {@code true} if it is.
	 * @since 2016/04/15
	 */
	public boolean isInitializer()
	{
		return isinit;
	}
	
	/**
	 * Is this method returning?
	 *
	 * @return {@code true} if this method is returning.
	 * @since 2016/04/16
	 */
	public boolean isReturning()
	{
		// Lock
		synchronized (lock)
		{
			return _doreturn;
		}
	}
	
	/**
	 * Leaves the current stack frame.
	 *
	 * @since 2016/04/09
	 */
	public void leave()
	{
		// Lock
		synchronized (lock)
		{
			thread.exitFrame(this);
			datawindow.popWindow();
		}
	}
	
	/**
	 * Marks that this frame should return.
	 *
	 * @return {@code this}.
	 * @since 2016/04/16
	 */
	public JVMStackFrame markReturn()
	{
		// Lock
		synchronized (lock)
		{
			_doreturn = true;
		}
		
		// Self
		return this;
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
	 * {@inheritDoc}
	 * @since 2016/04/09
	 */
	@Override
	public JVMThread thread()
	{
		return thread;
	}
	
	/**
	 * Returns the window this frame uses for data storage.
	 *
	 * @return The data storage window.
	 * @since 2016/04/09
	 */
	public JVMDataStore.Window window()
	{
		return datawindow;
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
	private void __initVariables(JVMDataStore.Window __vars, int __nl,
		Object[] __args, MethodSymbol __desc, int __nia)
	{
		// Classpaths
		JVMClassPath jcp = engine().classes();
		
		// {@squirreljme.error IN0w Input argument when initializing the
		// variables for an instance method is not an object. (The current
		// method; The used arguments)}
		if (isinstance && (__args.length <= 0 ||
			!(__args[0] instanceof JVMObject)))
			throw new JVMEngineException(this, String.format("IN0w %s %s",
				method, Arrays.asList(__args)));
		
		// Setup variable for first argument
		int vat = 0;
		if (isinstance)
			__vars.setObject(vat++, (JVMObject)__args[0]);
		
		// Setup variable
		for (int i = vat, varg = 0; i < __nia; i++)
		{
			// Get method argument from the descriptor and the input
			// argument
			FieldSymbol farg = __desc.get(varg++);
			Object carg = __args[i];
			
			// Get the variable type used for the argument
			CPVariableType type = CPVariableType.bySymbol(farg);
			
			// Could be the wrong type
			try
			{
				// Depends on the symbol
				switch (type)
				{
						// Integer
					case INTEGER:
						__vars.setInt(vat, ((Integer)carg).intValue());
						break;
						
						// Long
					case LONG:
						__vars.setLong(vat, ((Long)carg).longValue());
						break;
						
						// Float
					case FLOAT:
						__vars.setFloat(vat, ((Float)carg).floatValue());
						break;
						
						// Double
					case DOUBLE:
						__vars.setDouble(vat, ((Double)carg).doubleValue());
						break;
						
						// Object
					case OBJECT:
						__vars.setObject(vat, ((JVMObject)carg));
						break;
				
						// Unknown
					default:
						throw new RuntimeException("WTFX");
				}
			}
			
			// Was the wrong type
			catch (ClassCastException|NullPointerException e)
			{
				// {@squirreljme.error IN0h An input argument which was
				// passed to the method is not of the expected type that
				// the method accepts. (The actual input method argument;
				// The argument class type)}
				// Ignore null though
				throw new JVMClassCastException(this, String.format(
					"IN0h %s %s", type, carg.getClass()), e);
			}
			
			// {@squirreljme.error IN0i The number of method arguments
			// would exceed the number of available local variables
			// that exist within a method program. (The current
			// argument count; The maximum local variable count)}
			int nextvat = vat + (type.isWide() ? 2 : 1);
			if (nextvat > __nl)
				throw new JVMEngineException(this, String.format(
					"IN0i %d %d", nextvat, __nl));
			
			// Set write position
			vat = nextvat;
		}
	}
}


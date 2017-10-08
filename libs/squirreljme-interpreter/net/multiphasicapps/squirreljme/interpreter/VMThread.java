// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.NoSuchClassException;
import net.multiphasicapps.squirreljme.jit.VerifiedJITInput;
import net.multiphasicapps.squirreljme.jit.verifier.FamilyNode;

/**
 * This represents a thread within the virtual machine.
 *
 * @since 2017/10/06
 */
public class VMThread
{
	/** The reference to the owning interpreter. */
	protected final Reference<Interpreter> _interpreterref;
	
	/** Reference to the owning process. */
	protected final Reference<VMProcess> _processref;
	
	/** Returns the process thread ID. */
	protected final int threadid;
	
	/**
	 * Initializes the virtual machine thread.
	 *
	 * @param __i The owning interpreter.
	 * @param __p The process which owns this thread.
	 * @param __id The thread ID within the process.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/06
	 */
	public VMThread(Reference<Interpreter> __i, Reference<VMProcess> __p,
		int __id)
		throws NullPointerException
	{
		if (__i == null || __p == null)
			throw new NullPointerException("NARG");
		
		this._interpreterref = __i;
		this._processref = __p;
		this.threadid = __id;
	}
	
	/**
	 * Allocates an instance of the specified class.
	 *
	 * @param __ci The class to allocate.
	 * @return The newly allocated instance of the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	public final Instance allocateInstance(ClassInstance __ci)
		throws NullPointerException
	{
		if (__ci == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the instance for the specified class.
	 *
	 * @param __cn The name of the class to get the instance for.
	 * @return The instance of the class.
	 * @throws InterpreterClassNotFoundException If the class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/06
	 */
	public final ClassInstance classInstance(ClassName __cn)
		throws InterpreterClassNotFoundException, NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Use global interpreter lock
		Interpreter interpreter = __interpreter();
		VMProcess process = __process();
		Map<ClassName, ClassInstance> classes = process._classes;
		VerifiedJITInput input = interpreter._input;
		synchronized (interpreter._lock)
		{
			// If it has already been initialized then use that instance
			ClassInstance rv = classes.get(__cn);
			if (rv != null)
				return rv;
			
			// Need to get the node to know its family and such
			FamilyNode node;
			try
			{
				node = input.getNode(__cn);
			}
			
			// {@squirreljme.error AH05 The specified class does not exist.
			// (The name of the class)}
			catch (NoSuchClassException e)
			{
				throw new InterpreterClassNotFoundException(
					String.format("AH05 %s", __cn), e);
			}
			
			// Setup new class instances which would then be initialized
			// accordingly with their static initializers if they have any
			Deque<ClassInstance> initorder = new ArrayDeque<>();
			rv = new ClassInstance(this._processref, initorder);
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Invokes the specified method.
	 *
	 * @param __args The arguments to the method.
	 * @return The return value of the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	public final Object invoke(ClassMethod __m, Object... __args)
		throws NullPointerException
	{
		if (__m == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the interpreter which owns this thread.
	 *
	 * @return The interpreter.
	 * @throws IllegalStateException If the interpreter was garbage collected.
	 * @since 2017/10/06
	 */
	private Interpreter __interpreter()
		throws IllegalStateException
	{
		// {@squirreljme.error AH01 The interpreter has been garbage
		// collected.}
		Interpreter rv = this._interpreterref.get();
		if (rv == null)
			throw new IllegalStateException("AH01");
		return rv;
	}
	
	/**
	 * Returns the process which owns this thread.
	 *
	 * @return The owning process.
	 * @throws IllegalStateException If the process has been garbage
	 * collected.
	 * @since 2017/10/08
	 */
	final VMProcess __process()
		throws IllegalStateException
	{
		// {@squirreljme.error AH04 The process has been garbage collected.}
		VMProcess rv = this._processref.get();
		if (rv == null)
			throw new IllegalStateException("AH04");
		return rv;
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.classfile;

import net.multiphasicapps.classfile.JavaType;

/**
 * This class contains the state of the Java stack, it gets initialized to
 * a format which is used for conversion to SummerCoat.
 *
 * @since 2019/02/16
 */
public final class JavaState
{
	/** Local variables. */
	private final Slot[] _locals;
	
	/** Stack variables. */
	private final Slot[] _stack;
	
	/**
	 * Initializes the base Java state.
	 *
	 * @param __nl The number of local variables.
	 * @param __ns The number of stack variables.
	 * @since 2019/02/16
	 */
	public JavaState(int __nl, int __ns)
	{
		// Virtual register index counter
		int vr = 2;
		
		// Initialize locals
		Slot[] locals = new Slot[__nl];
		for (int i = 0; i < __nl; i++)
			locals[i] = new Slot(vr++);
		this._locals = locals;
		
		// Initialize stack
		Slot[] stack = new Slot[__ns];
		for (int i = 0; i < __ns; i++)
			stack[i] = new Slot(vr++);
		this._stack = stack;
	}
	
	/**
	 * Obtains the information in a local variable.
	 *
	 * @param __dx The local variable index.
	 * @return The result of the operation.
	 * @since 2019/02/16
	 */
	public final Result localGet(int __dx)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the local variable to the given type.
	 *
	 * @param __dx The index to set.
	 * @param __t The type to store.
	 * @return The result of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final Result localSet(int __dx, JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Pops a single value from the stack.
	 *
	 * @return The result of the pop.
	 * @since 2019/02/16
	 */
	public final Result stackPop()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Pushes the given type to the stack.
	 *
	 * @param __t The type to push.
	 * @return The result of the push.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final Result stackPush(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This contains the result of the operation when a push or pop has been
	 * performed. Since pushing/popping is complicated and will involve
	 * information on the type and registers, this is needed to
	 * simplify the design of the processor.
	 *
	 * @since 2019/02/16
	 */
	public static final class Result
	{
		/** The Java type which is involved here. */
		public final JavaType jtype;
		
		/**
		 * Initializes the result.
		 *
		 * @param __jt The Java type.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/02/16
		 */
		public Result(JavaType __jt)
			throws NullPointerException
		{
			if (__jt == null)
				throw new NullPointerException("NARG");
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Represents a slot within Java locals or the stack.
	 *
	 * @since 2019/02/16
	 */
	public static final class Slot
	{
		/** Virtual register index. */
		protected final int vrdx;
		
		/** The Java type stored here. */
		private JavaType _jtype;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __vr The virtual register index.
		 * @since 2019/02/16
		 */
		private Slot(int __vr)
		{
			this.vrdx = __vr;
		}
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.StackMapTableState;

/**
 * This class contains the state of the Java stack, it is mostly used in
 * the generation of the register code as it handles caching as well.
 *
 * This class is immutable.
 *
 * @since 2019/03/30
 */
public final class JavaStackState
{
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Destroys all local variables and stack variables returning the process
	 * that is needed to clear out the entire state.
	 *
	 * Any references that need to be cleared when the code completes will be
	 * placed in the enqueue list.
	 *
	 * @param __rv If true then a return value will be popped before everything
	 * is destroyed.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doDestroy(boolean __rv)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Removes all stack variables and places a single entry on the stack
	 * for exception handling.
	 *
	 * The input is filled with the entire stack. The output is a single
	 * entry and contains the stack entry to place the value at.
	 *
	 * @param __t The type to push.
	 * @return The result of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doExceptionHandler(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads the specified local variable onto the stack.
	 *
	 * @param __i The local to load from.
	 * @return The result of the operation.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doLocalLoad(int __i)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Pops a certain number of variables and then pushes the given types
	 * to the stack. Note that all results of this operation will treat
	 * all of the target stack operations as new freshly obtained values
	 * with no caching performed on them.
	 *
	 * @param __n The number of locals to pop.
	 * @param __t The types to push.
	 * @return The result of the operation.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doStack(int __n, JavaType... __t)
	{
		return this.doStack(__n, false, __t);
	}
	
	/**
	 * Pops a certain number of variables and then pushes the given types
	 * to the stack, this may also force caching on pushed values. Note that
	 * all results of this operation will treat
	 * all of the target stack operations as new freshly obtained values
	 * with no caching performed on them.
	 *
	 * @param __n The number of locals to pop.
	 * @param __nc If true then all the values being pushed will not be
	 * reference countable.
	 * @param __t The types to push.
	 * @return The result of the operation.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doStack(int __n, boolean __nc,
		JavaType... __t)
	{
		if (__t == null)
			__t = new JavaType[0];
		
		throw new todo.TODO();
	}
	
	/**
	 * Performs the specified stack shuffling, which may be duplication or
	 * otherwise.
	 *
	 * @param __t The type of shuffle to perform.
	 * @return The result of the shuffle.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doStackShuffle(JavaStackShuffleType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the given local.
	 *
	 * @param __i The local to obtain.
	 * @return The information for the local.
	 * @since 2019/03/30
	 */
	public final JavaStackState.Info getLocal(int __i)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the enqueues which are possible if the entire stack
	 * and locals were to be flushed.
	 *
	 * @return The maximum possible enqueue list.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList possibleEnqueue()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the stack state based off the given stack map table state,
	 * this should only be used for the initial seed of the stack state.
	 *
	 * @param __s The state to base off.
	 * @param __lw Local variables which have been written, this is used to
	 * set flags where locals are cached and can never be written to.
	 * @return The result stack state.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public static final JavaStackState of(StackMapTableState __s, int... __lw)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Optional, might not be specified
		__lw = (__lw == null ? new int[0] : __lw.clone());
		
		throw new todo.TODO();
	}
	
	/**
	 * Contains information on the individual stack slots.
	 *
	 * @since 2019/03/30
	 */
	public static final class Info
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/30
		 */
		@Override
		public final boolean equals(Object __o)
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/30
		 */
		@Override
		public final int hashCode()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/30
		 */
		@Override
		public final String toString()
		{
			throw new todo.TODO();
		}
	}
}


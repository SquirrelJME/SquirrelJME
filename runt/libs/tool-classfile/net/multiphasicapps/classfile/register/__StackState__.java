// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.StackMapTableState;

/**
 * This class contains the state of the Java stack, it gets initialized to
 * a format which is used for conversion to SummerCoat.
 *
 * @since 2019/02/16
 */
final class __StackState__
{
	/** Local variables. */
	private final Slot[] _locals;
	
	/** Stack variables. */
	private final Slot[] _stack;
	
	/** The current top of the stack. */
	private int _stacktop;
	
	/**
	 * Initializes the base Java state.
	 *
	 * @param __nl The number of local variables.
	 * @param __ns The number of stack variables.
	 * @since 2019/02/16
	 */
	__StackState__(int __nl, int __ns)
	{
		// Virtual register index counter
		// This can be set to 1 or 2 in the future to indicate that
		// temporary registers can be used.
		int vr = 0;
		
		// Initialize locals
		Slot[] locals = new Slot[__nl];
		for (int i = 0; i < __nl; i++)
			locals[i] = new Slot(vr++);
		this._locals = locals;
		
		// Initialize stack
		Slot[] stack = new Slot[__ns];
		for (int i = 0; i < __ns; i++)
		{
			Slot s;
			stack[i] = (s = new Slot(vr++));
			
			// All stack entries are written to, they are never cachable
			s._written = true;
		}
		this._stack = stack;
	}
	
	/**
	 * Freezes the given state.
	 *
	 * @return The frozen state.
	 * @since 2019/03/27
	 */
	public final __StackFreeze__ freeze()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Restores the state from a previously frozen state.
	 *
	 * @param __frz The state to restore from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/27
	 */
	public final void fromState(__StackFreeze__ __frz)
		throws NullPointerException
	{
		if (__frz == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads the Java state from the given stack map table state.
	 *
	 * @param __smt The state to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final void fromState(StackMapTableState __smt)
		throws NullPointerException
	{
		if (__smt == null)
			throw new NullPointerException("NARG");
		
		Slot[] locals = this._locals;
		Slot[] stack = this._stack;
		
		// Copy locals
		for (int i = 0, n = locals.length; i < n; i++)
		{
			Slot s = locals[i];
			
			s._type = __smt.getLocal(i).type();
			s._cached = null;
			s._nocounting = false;
		}
		
		// Copy the stack
		int depth = __smt.depth();
		for (int i = 0; i < depth; i++)
		{
			Slot s = stack[i];
			
			s._type = __smt.getStack(i).type();
			s._cached = null;
			s._nocounting = false;
		}
		this._stacktop = depth;
	}
		
	/**
	 * Obtains the information in a local variable.
	 *
	 * @param __dx The local variable index.
	 * @return The result of the operation.
	 * @since 2019/02/16
	 */
	public final __StackResult__ localGet(int __dx)
	{
		Slot sl = this._locals[__dx];
		return new __StackResult__(sl, sl._type, sl.register,
			false, sl._nocounting);
	}
	
	/**
	 * Loads the given local variable onto the stack, it may be cached.
	 *
	 * @param __dx The index to load from.
	 * @return The result of the operation, the result will be that of the
	 * destination.
	 * @since 2019/03/22
	 */
	public final __StackResult__ localLoad(int __dx)
	{
		// Perform the push logic to the stack
		Slot sl = this._locals[__dx];
		__StackResult__ result = this.stackPush(sl._type);
		
		// If the local is not quick cachable then just return the result of
		// the push
		if (sl._written)
			return result;
		
		// Otherwise, set the stack entry as cached and do not count it
		Slot xs = result.slot;
		xs._cached = sl;
		xs._nocounting = true;
		
		// Use the cached local instead
		return new __StackResult__(sl, sl._type, sl.register,
			true, sl._nocounting);
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
	public final __StackResult__ localSet(int __dx, JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Slot[] locals = this._locals;
		Slot sl = locals[__dx];
		
		// Set register details
		sl._type = __t;
		sl._cached = null;
		sl._nocounting = false;
		
		// Set top for wide local?
		boolean wide;
		if ((wide = __t.isWide()))
		{
			Slot xs = locals[__dx + 1];
			xs._type = __t.topType();
			xs._cached = null;
			xs._nocounting = false;
		}
		
		// Just a narrow set
		return new __StackResult__(sl, __t, sl.register);
	}
	
	/**
	 * Returns the slot for the local variable.
	 *
	 * @param __dx The index to get
	 * @return The slot for the given local.
	 * @since 2019/03/22
	 */
	public final __StackState__.Slot localSlot(int __dx)
	{
		return this._locals[__dx];
	}
	
	/**
	 * Returns the number of local variables available.
	 *
	 * @return The number of used locals.
	 * @since 2019/03/22
	 */
	public final int numLocals()
	{
		return this._locals.length;
	}
	
	/**
	 * Creates a snapshot of object positions for uncounting, this does not
	 * include cached stack entries.
	 *
	 * @return The snapshot of all the object positions.
	 * @since 2019/03/22
	 */
	public final __ObjectPositionsSnapshot__ objectSnapshot()
	{
		List<Integer> brv = new ArrayList<>();
		
		// Scan locals
		Slot[] locals = this._locals;
		for (int i = 0, n = locals.length; i < n; i++)
		{
			Slot s = locals[i];
			if (s.isObject() && !s._nocounting)
				brv.add(s.register);
		}
		
		// Start of stack is here
		int stackstart = brv.size();
		
		// Scan stack entries
		Slot[] stack = this._stack;
		for (int i = 0, n = this._stacktop; i < n; i++)
		{
			Slot s = stack[i];
			if (s.isObject() && s._cached == null && !s._nocounting)
				brv.add(s.register);
		}
		
		// Translate entries
		int n = brv.size();
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = brv.get(i);
		return new __ObjectPositionsSnapshot__(stackstart, rv);
	}
	
	/**
	 * Returns the register at the base of the stack.
	 *
	 * @return The register at the base of the stack.
	 * @since 2019/03/22
	 */
	public final int stackBaseRegister()
	{
		return this._stack[0].register;
	}
	
	/**
	 * Duplicates the top-most entry on the stack.
	 *
	 * @return The result of the duplication.
	 * @since 2019/03/22
	 */
	public final __StackResult__ stackDup()
	{
		// Peek the current entry so we can get the type information
		__StackResult__ cur = this.stackPeek();
		Slot cs = cur.slot;
		
		// Push entry to the stack of the same type
		__StackResult__ push = this.stackPush(cur.type);
		Slot ps = push.slot;
		
		// The top-most entry is just a cache of the one before it
		ps._cached = cs;
		return new __StackResult__(cs, cs._type, cs.register, true, true);
	}
	
	/**
	 * Modifies the stack so only the specified type is on the stack.
	 *
	 * @param __t The type to only place on the stack.
	 * @return The result of operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public final __StackResult__ stackOnly(JavaType __t)
		throw NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Peeks the value at the top of the stack.
	 *
	 * @return The peeked value and type at the top of the stack.
	 * @since 2019/03/24
	 */
	public final __StackResult__ stackPeek()
	{
		int stacktop = this._stacktop;
		Slot[] stack = this._stack;
		
		// {@squirreljme.error JC2p Stack underflow.}
		if ((--stacktop) < 0)
			throw new InvalidClassFormatException("JC2p");
		
		// Read from top of stack, also handle wide values
		Slot at = stack[stacktop];
		boolean wide;
		if ((wide = at._type.isTop()))
			at = stack[--stacktop];
		
		// If the source value was cached, then use the cached value
		Slot cached = at._cached;
		if (cached != null)
			return new __StackResult__(cached, cached._type, cached.register,
				true, true);
		return new __StackResult__(at, at._type, at.register,
			false, at._nocounting);
	}
	
	/**
	 * Pops a single value from the stack.
	 *
	 * @return The result of the pop.
	 * @since 2019/02/16
	 */
	public final __StackResult__ stackPop()
	{
		int stacktop = this._stacktop;
		Slot[] stack = this._stack;
		
		// {@squirreljme.error JC2n Stack underflow.}
		if ((--stacktop) < 0)
			throw new InvalidClassFormatException("JC2n");
		
		// Read from top of stack, also handle wide values
		Slot at = stack[stacktop];
		boolean wide;
		if ((wide = at._type.isTop()))
			at = stack[--stacktop];
		
		// Set new stack top
		this._stacktop = stacktop;
		
		// If the source value was cached, then use the cached value
		Slot cached = at._cached;
		if (cached != null)
			return new __StackResult__(cached, cached._type, cached.register,
				true, true);
		return new __StackResult__(at, at._type, at.register,
			false, at._nocounting);
	}
	
	/**
	 * Pushes the given type to the stack.
	 *
	 * @param __t The type to push.
	 * @return The result of the push.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final __StackResult__ stackPush(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC2o Stack overflow.}
		int stacktop = this._stacktop;
		Slot[] stack = this._stack;
		if (stacktop >= stack.length)
			throw new InvalidClassFormatException("JC2o");
		
		// Just needs simple set of type
		Slot at = stack[stacktop];
		at._type = __t;
		at._cached = null;
		at._nocounting = false;
		
		// Set required top type
		boolean wide;
		if ((wide = __t.isWide()))
		{
			Slot top = stack[++stacktop];
			top._type = __t.topType();
			top._cached = null;
			top._nocounting = false;
		}
		
		// Store new top
		this._stacktop = stacktop + 1;
		
		// Build result
		return new __StackResult__(at, __t, at.register);
	}
	
	/**
	 * Returns the register that would be used if the stack were to be pushed
	 * to, this may exceed the index of valid registers.
	 *
	 * @return The register at the top of the stack.
	 * @since 2019/03/21
	 */
	public final int stackTopRegister()
	{
		// This will just be the distance from the lowest register to the
		// current top of the stack
		return this._stack[0].register + this._stacktop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final String toString()
	{
		return "[L=" + Arrays.asList(this._locals) + ", S=" +
			Arrays.asList(this._stack) + "]";
	}
	
	/**
	 * Represents a slot within Java locals or the stack.
	 *
	 * @since 2019/02/16
	 */
	public static final class Slot
	{
		/** Virtual register index. */
		protected final int register;
		
		/** The Java type stored here. */
		private JavaType _type;
		
		/** Is this slot written to? */
		boolean _written;
		
		/** Reference counting is not enabled for this value. */
		boolean _nocounting;
		
		/** Is this currently a cache from a local? */
		private Slot _cached;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __vr The virtual register index.
		 * @since 2019/02/16
		 */
		private Slot(int __vr)
		{
			this.register = __vr;
		}
		
		/**
		 * Does this refer to an object?
		 *
		 * @return If this refers to an object.
		 * @since 2019/03/22
		 */
		public final boolean isObject()
		{
			JavaType type = this._type;
			return type != null && type.isObject();
		}
		
		/** 
		 * {@inheritDoc}
		 * @since 2019/03/22
		 */
		@Override
		public final String toString()
		{
			Slot cached = this._cached;
			return "R" + this.register + (this._written ? "w" : "") + "," +
				(cached == null ? "NotCached" : cached.toString()) + ":" +
				this._type;
		}
	}
}


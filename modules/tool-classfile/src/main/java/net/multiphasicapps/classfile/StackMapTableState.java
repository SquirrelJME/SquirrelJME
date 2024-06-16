// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * This represents a single state within the stack map table which contains
 * a listing of all of the types used for local and stack variable along with
 * the current depth of the stack.
 *
 * @since 2017/07/28
 */
public final class StackMapTableState
	implements Contexual
{
	/** The depth of the stack. */
	protected final int depth;
	
	/** Local variables. */
	private final StackMapTableEntry[] _locals;
	
	/** Stack variables. */
	private final StackMapTableEntry[] _stack;
	
	/** String representation of this table. */
	private Reference<String> _string;
	
	/**
	 * Initializes the stack map table state.
	 *
	 * @param __l Local variables.
	 * @param __s Stack variables.
	 * @param __d The depth of the stack.
	 * @throws InvalidClassFormatException If the state is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public StackMapTableState(StackMapTableEntry[] __l,
		StackMapTableEntry[] __s, int __d)
		throws InvalidClassFormatException, NullPointerException
	{
		this(__l, __s, __d, true);
	}
	
	/**
	 * Initializes the stack map table state.
	 *
	 * @param __l Local variables.
	 * @param __s Stack variables.
	 * @param __d The depth of the stack.
	 * @param __copyDefensive Make clones of the locals and stack? 
	 * @throws InvalidClassFormatException If the state is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	StackMapTableState(StackMapTableEntry[] __l,
		StackMapTableEntry[] __s, int __d, boolean __copyDefensive)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC3x The depth of the stack is not within the
		bounds of the stack. (The stack depth; The stack size)} */
		int ns = __s.length;
		if (__d < 0 || __d > ns)
			throw new InvalidClassFormatException(
				String.format("JC3x %d %d", __d, ns), this);
		
		// Duplicate, if doing defensive copy
		if (__copyDefensive)
		{
			__l = __l.clone();
			__s = __s.clone();
		}
		
		// Clear elements above the stack top
		for (int i = __d; i < ns; i++)
			__s[i] = null;
		
		// Verify each state
		StackMapTableState.__verify(__l);
		StackMapTableState.__verify(__s);
		
		// Set
		this._locals = __l;
		this._stack = __s;
		this.depth = __d;
	}
	
	/**
	 * Returns the depth of the stack.
	 *
	 * @return The stack depth.
	 * @since 2017/08/12
	 */
	public int depth()
	{
		return this.depth;
	}
	
	/**
	 * Derives the stack map from loading a local onto the stack.
	 * 
	 * @param __dx The local to load.
	 * @return The resultant stack state.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveLocalLoad(int __dx)
	{
		return this.deriveStackPush(this.getLocal(__dx));
	}
	
	/**
	 * Derives a set of a local variable.
	 * 
	 * @param __dx The index to set.
	 * @param __entry The entry to set.
	 * @return The derived stack map table state.
	 * @throws InvalidClassFormatException If the set is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveLocalSet(int __dx,
		StackMapTableEntry __entry)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC02 New local state would not be valid.} */
		if (__dx < 0 || __dx >= this.maxLocals() ||
			(__entry.isWide() && __dx >= this.maxLocals() - 1))
			throw new InvalidClassFormatException("JC02", this);
		
		StackMapTableEntry[] newLocals = this._locals.clone();
		
		// Set new entry
		newLocals[__dx] = __entry;
		if (__entry.isWide())
			newLocals[__dx + 1] = __entry.topType();
		
		// Initialize new state
		return new StackMapTableState(newLocals,
			this._stack, this.depth, false);
	}
	
	/**
	 * Derives the stack map from storing to a local from the stack
	 * 
	 * @param __dx The local to store.
	 * @return The resultant stack state.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveLocalStore(int __dx)
	{
		List<StackMapTableEntry> popped = new ArrayList<>();
		
		StackMapTableState result = this.deriveStackPop(popped);
		return result.deriveLocalSet(__dx, popped.get(0));
	}
	
	/**
	 * Derives a method call.
	 * 
	 * @param __isStatic Is this a static method call?
	 * @param __method The method to call.
	 * @return The resultant state.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveMethodCall(boolean __isStatic,
		MethodReference __method)
		throws NullPointerException
	{
		if (__method == null)
			throw new NullPointerException("NARG");
		
		// Pop method call arguments accordingly
		MethodDescriptor type = __method.memberType();
		
		// How much is being popped off?
		StackMapTableState result;
		int popCount = type.argumentCount() + (__isStatic ? 0 : 1);
		if (popCount > 0)
			result = this.deriveStackPop(null, popCount);
		else
			result = this;
		
		// Push any return value?
		if (type.hasReturnValue())
			return result.deriveStackPush(type.returnValue());
		return result;
	}
	
	/**
	 * Derives a stack map that pops the top of the stack.
	 * 
	 * @param __popped The entries which were popped.
	 * @return The derived table.
	 * @throws InvalidClassFormatException If the pop is not valid.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveStackPop(
		List<StackMapTableEntry> __popped)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC03 Stack is empty.} */
		int depth = this.depth;
		if (depth < 0)
			throw new InvalidClassFormatException("JC03", this);
		
		// Get top most item to add accordingly and determine if it is wide
		// or not...
		StackMapTableEntry top = this.getStackFromLogicalTop(0);
		if (__popped != null)
			__popped.add(0, top);
		
		// Remove top-most entry, double if wide... need to clone the stack
		// because it will normalize the entries!
		return new StackMapTableState(this._locals,
			this._stack.clone(), depth - (top.isWide() ? 2 : 1),
			false);
	}
	
	/**
	 * Derives a stack map that pops the top of the stack.
	 *
	 * @param __popped The entries which were popped.
	 * @param __count The number of entries to pop.
	 * @return The derived table.
	 * @throws InvalidClassFormatException If the pop is not valid.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveStackPop(
		List<StackMapTableEntry> __popped, int __count)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC05 Negative pop count.} */
		if (__count <= 0)
			throw new IllegalArgumentException("JC05");
		
		// Keep popping single values
		StackMapTableState result = this;
		for (int i = 0; i < __count; i++)
			result = result.deriveStackPop(__popped);
		return result;
	}
	
	/**
	 * Derives a stack map that pops the top of the stack and then pushes new
	 * entries.
	 * 
	 * @param __popped The items that were popped.
	 * @param __count The number of items to pop.
	 * @param __entries The entries to push.
	 * @return The derived table.
	 * @throws InvalidClassFormatException If the pop is not valid.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveStackPopThenPush(
		List<StackMapTableEntry> __popped, int __count,
		StackMapTableEntry... __entries)
		throws InvalidClassFormatException
	{
		return this.deriveStackPop(__popped, __count)
			.deriveStackPush(__entries);
	}
	
	/**
	 * Derives a push to the table.
	 * 
	 * @param __entries The entries to push.
	 * @return The derived table.
	 * @throws InvalidClassFormatException If the entries are not valid or
	 * would exceed the stack bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveStackPush(FieldDescriptor... __entries)
	{
		int count = (__entries == null ? 0 : __entries.length);
		
		StackMapTableEntry[] mapped = new StackMapTableEntry[count];
		for (int i = 0; i < count; i++)
			mapped[i] = new StackMapTableEntry(__entries[i], true);
		return this.deriveStackPush(mapped);
	}
	
	/**
	 * Derives a push to the table.
	 * 
	 * @param __entries The entries to push.
	 * @return The derived table.
	 * @throws InvalidClassFormatException If the entries are not valid or
	 * would exceed the stack bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveStackPush(StackMapTableEntry... __entries)
		throws InvalidClassFormatException, NullPointerException
	{
		// Initialize basic state
		StackMapTableEntry[] newStack = this._stack.clone();
		int newDepth = this.depth;
		
		// Add entries accordingly
		for (StackMapTableEntry entry : __entries)
		{
			/* {@squirreljme.error JC04 Stack overflow.} */
			if (newDepth + (entry.isWide() ? 2 : 1) > newStack.length)
				throw new InvalidClassFormatException("JC04", this);
			
			newStack[newDepth++] = entry;
			
			if (entry.isWide())
				newStack[newDepth++] = entry.topType();
		}
		
		// Setup new state
		return new StackMapTableState(this._locals,
			newStack, newDepth, false);
	}
	
	/**
	 * Derives stack operations for the given shuffle type.
	 * 
	 * @param __shuffle The shuffle type.
	 * @return The derived shuffled state.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public StackMapTableState deriveStackShuffle(
		JavaStackShuffleType __shuffle)
		throws NullPointerException
	{
		if (__shuffle == null)
			throw new NullPointerException("NARG");
		
		// Determine the shuffle function used
		JavaStackShuffleType.Function function = JavaStackShuffleType
			.findShuffleFunction(this, __shuffle);
		
		// Pop any inputs
		List<StackMapTableEntry> popped = new ArrayList<>();
		StackMapTableState result = this.deriveStackPop(popped,
			function.in.logicalMax);
		
		// Go through and pop all outputs
		int pushCount = function.out.logicalMax;
		StackMapTableEntry[] pushed = new StackMapTableEntry[pushCount];
		for (int i = 0; i < pushCount; i++)
			pushed[i] = popped.get(function.out.logicalSlot(i));
		
		result = result.deriveStackPush(pushed);
		
		return result;
	}
	
	/**
	 * Obtains the local at the given index.
	 *
	 * @param __i The index to get.
	 * @return The type for the variable at the given index.
	 * @throws InvalidClassFormatException If the index is out of range.
	 * @since 2017/08/12
	 */
	public StackMapTableEntry getLocal(int __i)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC3y The specified local variable is out of
		range. (The index)} */
		StackMapTableEntry[] locals = this._locals;
		if (__i < 0 || __i >= locals.length)
			throw new InvalidClassFormatException(
				String.format("JC3y %d", __i), this);
		return locals[__i];
	}
	
	/**
	 * Returns the stack.
	 * 
	 * @return The stack.
	 * @since 2023/07/03
	 */
	public List<StackMapTableEntry> getStack()
	{
		return UnmodifiableList.of(Arrays.asList(this._stack));
	}
	
	/**
	 * Obtains the stack at the given index.
	 *
	 * @param __i The index to get.
	 * @return The type for the variable at the given index.
	 * @throws InvalidClassFormatException If the index is out of range.
	 * @since 2017/08/12
	 */
	public StackMapTableEntry getStack(int __i)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC3z The specified stack variable is out of
		range. (The index)} */
		if (__i < 0 || __i >= this.depth)
			throw new InvalidClassFormatException(
				String.format("JC3z %d", __i), this);
		return this._stack[__i];
	}
	
	/**
	 * Gets the logical stack entry from the top.
	 * 
	 * @param __i The index to get from the top.
	 * @return The entry for the given entry.
	 * @throws InvalidClassFormatException If it is not valid.
	 * @since 2023/07/03
	 */
	public StackMapTableEntry getStackFromLogicalTop(int __i)
		throws InvalidClassFormatException
	{
		for (int logical = 0, actual = 0;;)
		{
			StackMapTableEntry entry = this.getStackFromTop(actual);
			
			// If this is top of a wide, then move down
			if (entry.isTop())
				actual++;
			
			// Does this match the requested item?
			else if (logical == __i)
				return entry;
			
			// Move both entries
			else
			{
				logical++;
				actual++;
			}
		}
	}
	
	/**
	 * Gets a stack entry from the top.
	 * 
	 * @param __i The index to get from the top.
	 * @return The entry from the top.
	 * @throws InvalidClassFormatException If it is not valid.
	 * @since 2023/07/03
	 */
	public StackMapTableEntry getStackFromTop(int __i)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC79 Cannot get stack from the top. (The index;
		The depth)} */
		if (__i < 0 || __i >= this.depth)
			throw new InvalidClassFormatException(
				String.format("JC79 %d %d", __i, this.depth), this);
		
		return this.getStack((this.depth - 1) - __i);
	}
	
	/**
	 * Returns the maximum number of local variables.
	 *
	 * @return The local variable count.
	 * @since 2019/02/17
	 */
	public final int maxLocals()
	{
		return this._locals.length;
	}
	
	/**
	 * Returns the maximum number of stack variables.
	 *
	 * @return The stack variable count.
	 * @since 2019/02/17
	 */
	public final int maxStack()
	{
		return this._stack.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("{locals=");
			StackMapTableState.__stringize(this._locals, sb);
			sb.append(", stack(");
			sb.append(this.depth);
			sb.append(")=");
			StackMapTableState.__stringize(this._stack, sb);
			sb.append("}");
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Stringizes the specified type array.
	 *
	 * @param __jt The type array to stringize.
	 * @param __sb The destination string builder.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	private static void __stringize(StackMapTableEntry[] __jt,
		StringBuilder __sb)
		throws NullPointerException
	{
		// Check
		if (__jt == null || __sb == null)
			throw new NullPointerException("NARG");
		
		// Open
		__sb.append('[');
		
		// Add
		for (int i = 0, n = __jt.length; i < n; i++)
		{
			if (i > 0)
				__sb.append(", ");
			
			__sb.append(__jt[i]);
		}
		
		// Close
		__sb.append(']');
	}
	
	/**
	 * Verifies the types within the map.
	 *
	 * @param __t The types to check.
	 * @throws InvalidClassFormatException If the type are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	private static void __verify(StackMapTableEntry[] __t)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Go through all entries, w acts as a kind of single entry stack which
		// is used to ensure that the tops of long/double are valid
		JavaType w = null;
		for (int i = 0, n = __t.length; i < n; i++)
		{
			StackMapTableEntry ea = __t[i];
			JavaType a = (ea != null ? ea.type() : null);
			
			// A wide type was pushed
			if (w != null)
			{
				/* {@squirreljme.error JC40 The type at the read index does
				not match the expected type following a wide type. (The wide
				type; The expected type; The actual type; The input map)} */
				JavaType t = w.topType();
				if (!t.equals(a))
					throw new InvalidClassFormatException(
						String.format("JC40 %s %s %s %s", w, t, a,
						Arrays.asList(__t)));
				
				// Clear
				w = null;
			}
			
			// No real checking has to be done unless it is a wide type
			else
			{
				if (a != null && a.isWide())
					w = a;
			}
		}
		
		/* {@squirreljme.error JC41 Long or double appears at the end of the
		type array and does not have a top associated with it.} */
		if (w != null)
			throw new InvalidClassFormatException("JC41");
	}
}


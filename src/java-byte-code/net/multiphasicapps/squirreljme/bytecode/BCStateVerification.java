// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.squirreljme.ci.CICodeAttribute;
import net.multiphasicapps.squirreljme.ci.CIMethod;

/**
 * This represents a single verification state.
 *
 * @since 2016/05/12
 */
public final class BCStateVerification
{
	/** Local variables. */
	protected final Locals locals;
	
	/** Stack variables. */
	protected final Stack stack;
	
	/** The logical address this verifies against. */
	protected final int logicaladdress;
	
	/** The string cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes a verification state for the given method.
	 *
	 * @param __m The method to initialize verification state for.
	 * @throws NullPointerException On null arguments.
	 * @sicne 2016/05/12
	 */
	public BCStateVerification(CIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Obtain the code attribute
		CICodeAttribute attribute = __m.code();
		
		// The stack is empty
		this.stack = new Stack(attribute.maxStack(), 0);
		
		// Starts at adress zero
		this.logicaladdress = 0;
		
		// Locals depends on the input arguments
		int maxlocals = attribute.maxLocals();
		final Locals locals = new Locals(maxlocals);
		this.locals = locals;
		
		// Raw array access
		BCVariableType[] ls = locals.storage;
		
		// Could fail
		MethodSymbol desc = __m.nameAndType().type();
		try
		{
			// Write position
			int at = 0;
			
			// If the method is not static then the first local is the instance
			if (!__m.flags().isStatic())
				ls[at++] = BCVariableType.OBJECT;
			
			// Go through the list of method arguments
			int n = desc.argumentCount();
			for (int i = 0; i < n; i++)
			{
				// Determine the variable type
				BCVariableType vt = BCVariableType.bySymbol(desc.get(i));
				
				// Store it
				ls[at++] = vt;
				
				// If wide, add top
				if (vt.isWide())
					ls[at++] = BCVariableType.TOP;
			}
		}
		
		// {@squirreljme.error AX08 Not enough local variables are available
		// to store the input method arguments. (The method descriptor; Is this
		// static?)}
		catch (IndexOutOfBoundsException e)
		{
			throw new BCException(String.format("AX08 %s", desc,
				__m.flags().isStatic()), e);
		}
	}
	
	/**
	 * Initializes a verification for locals and stack entries.
	 *
	 * @param __l The local variable tread.
	 * @param __s The stack tread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/13
	 */
	private BCStateVerification(Locals __l, Stack __s)
		throws NullPointerException
	{
		// Check
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.locals = __l;
		this.stack = __s;
		
		throw new Error("TODO");
	}
	
	/**
	 * Derives a verification state for the given operation.
	 *
	 * @param __op The operation to derive a state for.
	 * @return The derived verification state.
	 * @throws BCException If a local variable read is incorrect; if a written
	 * local variable is not valid; If a stack pop is not valid; If the stack
	 * overflows or underflows.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/13
	 */
	public BCStateVerification derive(BCOperation __op)
		throws BCException, NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
		/*
		
		// Get operation details
		List<BCLocalAccess> la = __op.localAccesses();
		List<BCVariableType> so = __op.stackPops();
		List<BCVariablePush> su = __op.stackPushes();
		
		// Get current stuff
		Locals locals = this.locals;
		Stack stack = this.stack;
		int maxlocals = locals.size();
		int maxstack = stack.size();
		
		// Calculate the top of the stack
		int newtop = stack.top();
		int n = so.size();
		for (int i = n - 1; i >= 0; i--)
		{
			// Simulated pop
			BCVariableType vt = so.get(i);
			boolean iswide;
			newtop -= ((iswide = vt.isWide()) ? 2 : 1);
			
			// {@squirreljme.error AX0g Stack underflow popping variables for
			// derivation. (The source stack; The pop operations; The push
			// operations)}
			if (newtop < 0)
				throw new BCException(String.format("AX0g %s %s %s", stack, so,
					su));
			
			// {@squirreljme.error AX0i Unexpected type on stack while popping
			// values. (The source stack; The pop operations)}
			BCVariableType oops;
			if ((oops = stack.get(newtop)) != vt)
				throw new BCException(String.format("AX0i %s %s", stack, so));
			
			// {@squirreljme.error AX0j Expected top of long or double to
			// follow a pop of long or double. (The source stack; The pop
			// operations)}
			if (iswide &&
				(oops = stack.get(newtop + 1)) != BCVariableType.TOP)
				throw new BCException(String.format("AX0j %s %s", stack, so));
		}
		
		// Remember the base bottom
		int bottom = newtop;
		
		// Now push entries to the stack
		n = su.size();
		for (int i = 0; i < n; i++)
		{
			// Simulated push
			BCVariableType vt = su.get(i).pushType();
			newtop += (vt.isWide() ? 2 : 1);
			
			// {@squirreljme.error AX0h Stack overflow pushing variabels for
			// derivation. (The source stack; The pop operations; The push
			// operations})
			if (newtop > maxstack)
				throw new BCException(String.format("AX0h %s %s %s", stack, so,
					su));
		}
		
		// Check local variable read/writes
		for (BCLocalAccess a : la)
		{
			// Get the type
			BCVariableType vt = a.type();
			boolean iswide = vt.isWide();
			
			// {@squirreljme.error AX0k Access of a local variable which is
			// not within the bounds of the local variable tread. (The current
			// local variables; The variables accessed; The current variable)}
			int dx = a.getIndex();
			if (dx < 0 || (dx + (iswide ? 1 : 0)) >= maxlocals)
				throw new BCException(String.format("AX0k %s %s", locals, la,
					a));
			
			// If read from, check the type
			if (a.isRead())
			{
				// {@squirreljme.error AX0l Expected the read local variable to
				// be of the given type, however it was not the specified type.
				// (The current local variables; The variables to access; The
				// current variable to check; The type that it was; The
				// expected type)}
				BCVariableType was = locals.get(dx);
				if (was != vt)
					throw new BCException(String.format("AX0l %s %s %s %s",
						locals, la, a, was, vt));
				
				// If wide, the next must be top
				if (vt.isWide())
				{
					// {@squirreljme.error AX0m Expected the read wide local
					// variable to have a top following it, however that was
					// not the case. (The current local variables; The
					// variables to access; The current variable to check; The
					// type that it was; The expected type)}
					was = locals.get(dx + 1);
					if (was != BCVariableType.TOP)
						throw new BCException(String.format("AX0m %s %s %s %s",
							locals, la, a, was, vt));
				}
			}
		}
		
		// Setup return value
		Locals ll = new Locals(maxlocals);
		Stack ss = new Stack(maxstack, newtop);
		
		// Get target storage
		BCVariableType[] mll = ll.storage;
		BCVariableType[] mss = ss.storage;
		
		// And source storage
		BCVariableType[] zll = locals.storage;
		BCVariableType[] zss = stack.storage;
		
		// Base copy locals
		for (int i = 0; i < maxlocals; i++)
			mll[i] = zll[i];
		
		// Copy stack to the base
		for (int i = 0; i < bottom; i++)
			mss[i] = zss[i];
		
		// Write local variables
		for (BCLocalAccess a : la)
			if (a.isWritten())
			{
				// Get the type to write
				BCVariableType vt = a.type();
				int dx;
				mll[(dx = a.getIndex())] = vt;
				
				// Add top
				if (vt.isWide())
					mll[dx + 1] = BCVariableType.TOP;
			}
		
		// Write target stack
		n = su.size();
		for (int i = 0, at = bottom; i < n; i++)
		{
			// Actual push
			BCVariableType vt = su.get(i).pushType();
			
			// Write here
			mss[at++] = vt;
			
			// Add top if wide
			if (vt.isWide())
				mss[at++] = BCVariableType.TOP;
		}
		
		// Return it
		return new BCStateVerification(ll, ss);
		*/
	}
	
	/**
	 * Returns the set of local variable types.
	 *
	 * @return The set of local variable types to use.
	 * @since 2016/05/12
	 */
	public Locals locals()
	{
		return locals;
	}
	
	/**
	 * Returns the logical address of the operation this verifies against.
	 *
	 * @return The logical address this defines a state for.
	 * @since 2016/06/22
	 */
	public int logicalAddress()
	{
		return this.logicaladdress;
	}
	
	/**
	 * Returns the set of stack variable types.
	 *
	 * @return The set of stack variable types to use.
	 * @since 2016/05/12
	 */
	public Stack stack()
	{
		return stack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = "{locals=" + locals +
				", stack=" + stack + "}"));
		
		// Return it
		return rv;
	}
	
	/**
	 * This represents the local variables which are currently set to given
	 * types.
	 *
	 * @since 2016/05/12
	 */
	public static final class Locals
		extends __Tread__
	{
		/**
		 * Initializes the local variable types.
		 *
		 * @param __n The number of local variables used.
		 * @since 2016/05/12
		 */
		private Locals(int __n)
		{
			super(__n);
		}
	}
	
	/**
	 * This represents the stack which stores temporary values.
	 *
	 * @since 2016/05/12
	 */
	public static final class Stack
		extends __Tread__
	{
		/** The top of the stack. */
		protected final int top;
		
		/**
		 * Initializes the stack.
		 *
		 * @param __n The number of items in the stack.
		 * @param __top The top of the stack.
		 * @throws BCException If the top of the stack is out of bounds.
		 * @since 2016/05/12
		 */
		private Stack(int __n, int __top)
			throws BCException
		{
			super(__n);
			
			// {@squirreljme.error AX06 The size of the stack either overflows
			// or underflows the number of stack entries. (The position of the
			// top of the stack; The number of entries on the stack)}
			if (__top < 0 || __top > __n)
				throw new BCException(String.format("AX06 %d %d", __top, __n));
			
			// Set
			top = __top;
		}
		
		/**
		 * Returns the top of the stack.
		 *
		 * @return The top of the stack.
		 * @since 2016/05/12
		 */
		public int top()
		{
			return top;
		}
	}
	
	/**
	 * This is the base class for variable type treads.
	 *
	 * @since 2016/05/12
	 */
	private static abstract class __Tread__
		extends AbstractList<BCVariableType>
		implements RandomAccess
	{
		/** The number of entries in the tread. */
		protected final int count;
		
		/** The variable storage area. */
		protected final BCVariableType[] storage;
		
		/**
		 * Initializes the base tread.
		 *
		 * @param __n The number of entries.
		 * @since 2016/05/12
		 */
		private __Tread__(int __n)
		{
			// Initialize
			count = __n;
			storage = new BCVariableType[__n];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/12
		 */
		@Override
		public final BCVariableType get(int __i)
		{
			return storage[__i];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/12
		 */
		@Override
		public final int size()
		{
			return count;
		}
	}
}


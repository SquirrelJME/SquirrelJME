// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.CFMethod;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This represents the state of variables for a given operation in the program.
 *
 * @since 2016/04/10
 */
public class CPVariables
	extends AbstractList<CPVariables.Slot>
{
	/** The operation this is a state for. */
	protected final CPOp operation;
	
	/** Individual variable slots. */
	protected final List<Slot> slots;
	
	/** The bottom of the stack. */
	protected final int stackbottom;
	
	/**
	 * Initializes an fully implicit variable state.
	 *
	 * @param __op The operation this contains a state for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/11
	 */
	CPVariables(CPOp __op)
		throws NullPointerException
	{
		this(__op, false, null, null);
	}
	
	/**
	 * Initializes 
	 *
	 * @param __op The operation this contains a state for.
	 * @param __method The method to initialize a state for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/11
	 */
	CPVariables(CPOp __op, CFMethod __method)
		throws NullPointerException
	{
		this(__op, true, __method, null);
	}
	
	/**
	 * Initializes the variable state with an explicit verification state, the
	 * SSA variable IDs are implicit.
	 *
	 * @param __op The operation to hold the state for.
	 * @param __vs The types that each state of variables be.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/10
	 */
	CPVariables(CPOp __op, CPVerifyState __vs)
		throws NullPointerException
	{
		this(__op, true, null, __vs);
	}
	
	/**
	 * Initializes the state of variables.
	 *
	 * @param __op The operation to hold a state for.
	 * @param __expl If {@code true} then the states are explicit and indicated
	 * by the extra parameters.
	 * @param __method The 
	 * @throws NullPointerException If no operation was specified, or if
	 * there are explicit definitions with no input.
	 * @since 2016/04/11
	 */
	private CPVariables(CPOp __op, boolean __expl, CFMethod __method,
		CPVerifyState __vs)
		throws NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Set
		operation = __op;
		
		// Setup slots
		CPProgram program = operation.program();
		int sn = program.variableCount();
		Slot[] aslots = new Slot[sn];
		for (int i = 0; i < sn; i++)
			aslots[i] = new Slot(i);
		slots = MissingCollections.<Slot>unmodifiableList(
			Arrays.<Slot>asList(aslots));
		stackbottom = program.maxLocals();
		
		// Fully implicit? Then nothing has to be done
		if (!__expl && __method == null && __vs == null)
			;
		
		// The entry state of the first instruction
		else if (__expl && __method != null && __vs == null)
			throw new Error("TODO");
		
		// Types are explicit but the SSA variable IDs are implicit
		else if (__expl && __method == null && __vs != null)
			throw new Error("TODO");
		
		// invalid input
		else
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Slot get(int __i)
	{
		return slots.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int size()
	{
		return slots.size();
	}
	
	/**
	 * This is a variable slot,
	 *
	 * @since 2016/04/10
	 */
	public class Slot
	{
		/** The index of this slot. */
		protected final int index;
		
		/** String representation. */
		private volatile Reference<String> _string;
		
		/**
		 * Internal slot initialization.
		 *
		 * @param __dx The index of this slot.
		 * @since 2016/04/11
		 */
		private Slot(int __dx)
		{
			index = __dx;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/11
		 */
		@Override
		public String toString()
		{
			// Get reference
			Reference<String> ref = _string;
			String rv;
			
			// Needs initialization?
			if (ref == null || null == (rv = ref.get()))
			{
				StringBuilder sb = new StringBuilder("<");
				
				// The slot index
				sb.append(index);
				
				// The type that is here
				sb.append(':');
				
				// Finish
				sb.append('>');
				_string = new WeakReference<>((rv = sb.toString()));
			}
			
			// Return it
			return rv;
		}
	}
}


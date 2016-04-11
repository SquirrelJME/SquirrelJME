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
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

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
	
	/** The top of the stack. */
	private volatile int _stacktop =
		Integer.MIN_VALUE;
	
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
		
		// The entry state of the first instruction, note that the types are
		// known, however the SSA IDs are still implicit due to potential "phi"
		// junctions.
		else if (__expl && __method != null && __vs == null)
		{
			// Obtain the method descriptor
			MethodSymbol desc = __method.type();
			
			// Stack starts at the base
			_stacktop = stackbottom;
			
			// There could be more method arguments than there are locals
			int vat = 0;
			try
			{
				// If the method is not static, the first type becomes the
				// first method argument
				if (!__method.flags().isStatic())
				{
					// Get slot
					Slot sl = slots.get(vat++);
				
					// Set type and the SSA ID
					sl.__setType(CPVariableType.OBJECT);
				}
			
				// Go through all method arguments
				int na = desc.argumentCount();
				for (int i = 0; i < na; i++)
				{
					// Get field type
					FieldSymbol fs = desc.get(i);
					CPVariableType vt = CPVariableType.bySymbol(fs);
				
					// Get next slot
					Slot sl = slots.get(vat++);
					
					// Set data
					sl.__setType(vt);
					
					// If top, set the next one
					if (vt.isWide())
					{
						// Get top slot
						sl = slots.get(vat++);
						
						// Make it a top
						sl.__setType(CPVariableType.TOP);
					}
				}
			}
			
			// {@squirreljme.error CP0s There are not enough local
			// variables to store method arguments in the current
			// operation. (The variable position; The bottom of the stack)}
			catch (IndexOutOfBoundsException e)
			{
				throw new CPProgramException(String.format("CP0s %d %d",
					vat, stackbottom), e);
			}
			
			// Check for bounds
			if (vat > stackbottom)
				throw new CPProgramException(String.format("CP0s %d %d",
					vat, stackbottom));
			
			// The remaining slots are nothing
			while (vat < sn)
				slots.get(vat++).__setType(CPVariableType.NOTHING);
		}
		
		// Types are explicit but the SSA variable IDs are implicit
		else if (__expl && __method == null && __vs != null)
		{
			// Copy all explicit types
			for (int i = 0; i < sn; i++)
				slots.get(i).__setType(__vs.get(i));
			
			// Set stack top
			_stacktop = __vs.getStackTop();
		}
		
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
	 * This is a variable slot, which contains type and SSA variable
	 * information.
	 *
	 * The upper 16-bits of the SSA ID represent the logical instruction of
	 * the source, while the lower 16-bit of the ID represent the slot number.
	 * SSA IDs change when the value becomes something different and it is not
	 * a copy. 
	 *
	 * @since 2016/04/10
	 */
	public final class Slot
	{
		/** Internal lock. */
		protected final Object lock =
			new Object();
		
		/** The index of this slot. */
		protected final int index;
		
		/** The type of variable contained here. */
		private volatile CPVariableType _type;
		
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
		 * Returns the type of variable this slot is.
		 *
		 * @return The type that this variable is.
		 * @since 2016/04/11
		 */
		public CPVariableType type()
		{
			// Lock
			synchronized (lock)
			{
				// Explicit or has already been determined?
				CPVariableType rv = _type;
				if (rv != null)
					return rv;
				
				// Compute all source operations, this builds the potential
				// SSA and variable trees.
				for (CPOp xop : operation.jumpSources())
					xop.__compute();
				
				// Try again
				rv = _type;
				
				// {@squirreljme.error CP0t Attempt to get the type that a
				// slot is after computation, however the type was not
				// determined during a computation. (The opcode index; The
				// slot index)}
				if (rv == null)
					throw new CPProgramException(String.format("CP0t %d %d",
						operation.address(), index));
				
				// Return it
				return rv;
			}
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
				sb.append(type());
				
				// Finish
				sb.append('>');
				_string = new WeakReference<>((rv = sb.toString()));
			}
			
			// Return it
			return rv;
		}
		
		/**
		 * Sets the type of value that this variable is.
		 *
		 * @param __vt The type of value this contains.
		 * @return {@code this}.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/04/11
		 */
		Slot __setType(CPVariableType __vt)
			throws NullPointerException
		{
			// Check
			if (__vt == null)
				throw new NullPointerException("NARG");
			
			// Lock
			synchronized (lock)
			{
				_type = __vt;
			}
			
			// Self
			return this;
		}
		
		/**
		 * Sets the SSA value ID of the current slot, this may also be a "phi"
		 * junction.
		 *
		 * @param __phi If {@code true} then this is a 
		 * @param __vid The value IDs, if not a "phi" junction then this must
		 * contain a single element containing the source ID, otherwise the
		 * values represent other instructions
		 * @return {@code this}.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/04/11
		 */
		Slot __setValue(boolean __phi, int... __vids)
			throws NullPointerException
		{
			// Check
			if (__vids == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
	}
}


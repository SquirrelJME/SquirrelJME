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
	/** Slot value ID instruction shift. */
	public static final int SSA_ADDRESS_SHIFT =
		16;
	
	/** Slot value ID instruction value mask. */
	public static final int SSA_ADDRESS_VALUE_MASK =
		0x0000_FFFF;
	
	/** Slot value ID instruction shifted mask. */
	public static final int SSA_ADDRESS_SHIFTED_MASK =
		0xFFFF_0000;
	
	/** Slot value ID slot shift. */
	public static final int SSA_SLOT_SHIFT =
		0;
	
	/** Slot value ID slot value mask. */
	public static final int SSA_SLOT_VALUE_MASK =
		0x0000_FFFF;
	
	/** Slot value ID slot shifted mask. */
	public static final int SSA_SLOT_SHIFTED_MASK =
		0x0000_FFFF;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The owning program. */
	protected final CPProgram program;
	
	/** The operation this is a state for. */
	protected final CPOp operation;
	
	/** Individual variable slots. */
	protected final List<Slot> slots;
	
	/** The bottom of the stack. */
	protected final int stackbase;
	
	/** Maximum variables permitted. */
	protected final int maxvars;
	
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
		program = operation.program();
		
		// {@squirreljme.error CP0y The number of local variables and stack
		// variables is limited to 65536 individual variables. This is a
		// limitation of this software, however having large methods in code
		// that is intended to be run on embedded and mobile environments using
		// this many variables is not recommended.
		// (The number of variables which the method uses)}
		int sn = program.variableCount();
		maxvars = sn;
		if (sn >= 65536)
			throw new CPProgramException(String.format("CP0y %d", sn));
		
		// Setup slots
		Slot[] aslots = new Slot[sn];
		for (int i = 0; i < sn; i++)
			aslots[i] = new Slot(i);
		slots = MissingCollections.<Slot>unmodifiableList(
			Arrays.<Slot>asList(aslots));
		stackbase = program.maxLocals();
		
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
			_stacktop = stackbase;
			
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
					vat, stackbase), e);
			}
			
			// Check for bounds
			if (vat > stackbase)
				throw new CPProgramException(String.format("CP0s %d %d",
					vat, stackbase));
			
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
	 * Returns the top of the stack.
	 *
	 * @return The top of the stack.
	 * @throws CPProgramException If the top of the stack was not set.
	 * @since 2016/04/11
	 */
	public int getStackTop()
		throws CPProgramException
	{
		// Lock
		synchronized (lock)
		{
			int rv = _stacktop;
			
			// {@squirreljme.error CP0w The top of the stack has never been
			// set. (The instruction address)}
			if (rv < 0)
				throw new CPProgramException(String.format("CP0w %d",
					operation.address()));
			
			// Return it
			return rv;
		}
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
	 * Sets the top of the stack and checks whether the given operation is
	 * valid.
	 *
	 * @param __top The top of the stack to set.
	 * @return The set of variables.
	 * @throws CPProgramException If the set of the stack top is not valid.
	 * @since 2016/04/12
	 */
	CPVariables __checkedSetStackTop(int __top)
		throws CPProgramException
	{
		// {@squirreljme.error CP11 Attempt to set the top of the stack to
		// an index which is nothing the bounds of the stack (The requested
		// stack top; The base of the stack; The top of the stack)}
		if (__top < stackbase || __top > maxvars)
			throw new CPProgramException(String.format("CP11 %d %d %d",
				__top, stackbase, maxvars));
		
		// Lock
		synchronized (lock)
		{
			// Get the current stack top
			int now = _stacktop;
			
			// Was never set at all?
			if (now == Integer.MIN_VALUE)
				_stacktop = __top;
			
			// {@squirreljme.error CP12 Attempt to set the stack size of an
			// operation which does not match the previously set size.
			// (The requested top; The current top)}
			else if (__top != now)
				throw new CPProgramException(String.format("CP12 %d %d",
					__top, now));
		}
		
		// Self
		return this;
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
		/** The index of this slot. */
		protected final int index;
		
		/** The type of variable contained here. */
		private volatile CPVariableType _type;
		
		/** Phi-function state. */
		private volatile CPPhiType _phitype;
		
		/** Current variable IDs. */
		private volatile int[] _vids;
		
		/** String representation. */
		private volatile Reference<String> _string;
		
		/** Value ID as a list cache. */
		private volatile Reference<List<Integer>> _vidwrap;
		
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
		 * Returns the index of this slot.
		 *
		 * @return The slot index.
		 * @since 2016/04/11
		 */
		public int index()
		{
			return index;
		}
		
		/**
		 * Returns the phi type of the current slot.
		 *
		 * @return The phy type of the slot.
		 * @throws CPProgramException If it has not been set.
		 * @since 2016/04/11
		 */
		public CPPhiType phi()
			throws CPProgramException
		{
			// Lock
			synchronized (lock)
			{
				// Must be determined
				CPPhiType rv = _phitype;
				if (rv != null)
				{
					// If there are more than two values then it is a phi, so
					// always return that state.
					int[] vx = _vids;
					if (vx != null && vx.length >= 2)
						return CPPhiType.PHI;
					
					// Otherwise use the flagged one
					return rv;
				}
				
				// {@squirreljme.error CP0v Attempt to get the phi type of a
				// slot, however the phi type was not determined yet.
				// (The opcode index; The slot index)}
				throw new CPProgramException(String.format("CP0v %d %d",
					operation.address(), index));
			}
		}
		
		/**
		 * Returns the type of variable this slot is.
		 *
		 * @return The type that this variable is.
		 * @throws CPProgramException If it has not been set.
		 * @since 2016/04/11
		 */
		public CPVariableType type()
			throws CPProgramException
		{
			// Lock
			synchronized (lock)
			{
				// Explicit or has already been determined?
				CPVariableType rv = _type;
				if (rv != null)
					return rv;
				
				// {@squirreljme.error CP0t Attempt to get the type of a
				// slot, however the type was not determined yet.
				// (The opcode index; The slot index)}
				throw new CPProgramException(String.format("CP0t %d %d",
					operation.address(), index));
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
				
				// The phi type that this is
				sb.append('^');
				sb.append(phi());
				
				// Finish
				sb.append('>');
				_string = new WeakReference<>((rv = sb.toString()));
			}
			
			// Return it
			return rv;
		}
		
		/**
		 * Returns the list of values that this may potentially have a value
		 * for.
		 *
		 * @return The boxed list of raw SSA values.
		 * @since 2016/04/11
		 */
		public List<Integer> values()
		{
			// Lock
			synchronized (lock)
			{
				// Get reference
				Reference<List<Integer>> ref = _vidwrap;
				List<Integer> rv;
				
				// Needs to be cached?
				if (ref == null || null == (rv = ref.get()))
					_vidwrap = new WeakReference<>((rv = MissingCollections.
						<Integer>unmodifiableList(
							MissingCollections.boxedList(_vids))));
				
				// Return it
				return rv;
			}
		}
		
		/**
		 * Adds an SSA value ID to the current slot.
		 *
		 * @param __vid The value ID to add.
		 * @return {@code this}.
		 * @throws CPProgramException If the value ID would be outside of the
		 * program or slot bounds.
		 * @since 2016/04/11
		 */
		Slot __addValue(int __vid)
			throws CPProgramException
		{
			// Sanity check
			int ipc = (__vid >>> SSA_ADDRESS_SHIFT) & SSA_ADDRESS_VALUE_MASK;
			int isl = (__vid >>> SSA_SLOT_SHIFT) & SSA_SLOT_VALUE_MASK;
			
			// {@squirreljme.error CP0z Attempt to add a SSA value to a slot
			// which references a value which is not within the program or
			// variable bounds. (The variable address; The variable slot)}
			if (ipc < 0 || ipc >= program.size() ||
				isl < 0 || isl >= slots.size())
				throw new CPProgramException(String.format("CP0z %d %d",
					ipc, isl));
			
			// Lock
			synchronized (lock)
			{
				// Get current array
				int[] now = _vids;
				
				// Would not exist anyway
				if (now == null)
					_vids = new int[]{__vid};
				
				// Check it
				else
				{
					// Never add the same variable twice
					for (int i : now)
						if (i == __vid)
							return this;
					
					// Resize
					int end;
					_vids = (now = Arrays.copyOf(now, (end = now.length) + 1));
					now[end] = __vid;
				}
			}
			
			// Self
			return this;
		}
		
		/**
		 * Checks set of a variable type.
		 *
		 * @param __vt The type of variable to set.
		 * @return {@code this}.
		 * @throws CPProgramException If the type verifies incorrectly.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/04/12
		 */
		Slot __checkedSetType(CPVariableType __vt)
			throws CPProgramException, NullPointerException
		{
			// Check
			if (__vt == null)
				throw new NullPointerException("NARG");
			
			// Lock
			synchronized (lock)
			{
				// Check stack bounds
				__checkStackBounds();
				
				// Is a type already set?
				CPVariableType already = _type;
				
				// One already is
				if (already != null)
				{
					// Values never replace nothing
					if (already == CPVariableType.NOTHING)
						return this;
					
					// {@squirreljme.error CP15 Setting of the type of a slot
					// however it does not match the type that is already in
					// this position. (The slot index; The type to set it as;
					// The type this is already set as)}
					if (__vt != already)
						throw new CPProgramException(String.format(
							"CP15 %d %s %s", index, __vt, already));
						
				}
				
				// Set it
				else
					_type = __vt;
			}
			
			// Self
			return this;
		}
		
		/**
		 * Checks setting a SSA value to the slot.
		 *
		 * @param __vid The value to set.
		 * @return {@code this}.
		 * @since 2016/04/12
		 */
		Slot __checkedSetValue(int __vid)
		{
			// Lock
			synchronized (lock)
			{
				// Check stack bounds
				__checkStackBounds();
				
				// Add value
				return __addValue(__vid);
			}
		}
		
		/**
		 * Checks the bounds of the stack to make sure it is within the stack.
		 *
		 * @return {@code this}.
		 * @throws CPProgramException If it is not within bounds.
		 * @since 2016/04/12
		 */
		Slot __checkStackBounds()
			throws CPProgramException
		{
			// Lock
			synchronized (lock)
			{
				// {@squirreljme.error CP14 Setting of a type of value exceeds
				// the top of the stack. (The current slot index; The top of
				// the stack)}
				int top;
				if (index >= (top = getStackTop()))
					throw new CPProgramException(String.format("CP14 %d %d",
						index, top));
			}
			
			// Self
			return this;
		}
		
		/**
		 * Sets the phi type of the current slot.
		 *
		 * @param __pt The phy type to set.
		 * @return {@code this}.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/04/11
		 */
		Slot __setPhi(CPPhiType __pt)
			throws NullPointerException
		{
			// Check
			if (__pt == null)
				throw new NullPointerException("NARG");
			
			// Lock
			synchronized (lock)
			{
				_phitype = __pt;
			}
			
			// Self
			return this;
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
	}
}


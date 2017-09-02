// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used to construct instances of {@link StackMapTable}, this
 * reads the raw data from the {@code StackMap} or {@code StackMapTable}
 * attribute.
 *
 * @since 2017/07/15
 */
public class StackMapTableBuilder
{
	/** The byte code for the method owning this, needed for {@code new}. */
	protected final ByteCode code;
	
	/** The constant pool, needed for type decoding. */
	protected final Pool pool;
	
	/** Stack map states for given addresses. */
	private final Map<Integer, StackMapTableState> _states =
		new SortedTreeMap<>();
	
	/** Entries which are in local variables. */
	private final StackMapTableEntry[] _locals;
	
	/** Entries which are on the stack. */
	private final StackMapTableEntry[] _stack;
	
	/** The depth of the stack. */
	private volatile int _depth;
	
	/**
	 * Initializes the stack map table builder.
	 *
	 * @param __f The flags used for the method.
	 * @param __n The name of the method.
	 * @param __t The descriptor for the method.
	 * @param __oc The class which contains the method to build a stack map
	 * for, this is needed for {@code this} references.
	 * @param __bc The byte code for the current method, required for handling
	 * initialization of new objects.
	 * @param __pool The constant pool.
	 * @param __ns The number of stack entries.
	 * @param __nl The number of local variables.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/24 
	 */
	public StackMapTableBuilder(MethodFlags __f, MethodName __n,
		MethodDescriptor __t, ClassName __oc, ByteCode __bc)
		throws NullPointerException
	{
		// Check
		if (__f == null || __t == null || __oc == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Set base
		this.code = __bc;
		Pool pool = __bc.pool();
		this.pool = pool;
		
		// The stack is always empty on initial entry
		int maxstack = __bc.maxStack();
		this._stack = new StackMapTableEntry[maxstack];
		
		// Locals are initialized according to the argument types
		int maxlocals = __bc.maxLocals();
		StackMapTableEntry[] locals = new StackMapTableEntry[maxlocals];
		this._locals = locals;
		
		// If this is an instance method then the first argument is always the
		// the parameter. Instance initializers start with an uninitialized
		// this which requires initialization first.
		int at = 0;
		if (!__f.isStatic())
			locals[at++] = new StackMapTableEntry(new JavaType(__oc),
				!__n.isInstanceInitializer());
		
		// Handle all arguments now
		for (int i = 0, na = __t.argumentCount(); i < na; i++)
		{
			// Trivial set of argument
			FieldDescriptor a = __t.argument(i);
			locals[at++] = new StackMapTableEntry(new JavaType(a), true);
			
			// Add top of long/double but with unique distinct types
			if (a.equals(FieldDescriptor.LONG))
				locals[at++] = StackMapTableEntry.TOP_LONG;
			else if (a.equals(FieldDescriptor.DOUBLE))
				locals[at++] = StackMapTableEntry.TOP_DOUBLE;
		}
		
		// Set initial entry stat
		add(0);
	}
	
	/**
	 * Adds the current partial state to a constructed state and holds it in
	 * an internal map for later building.
	 *
	 * @param __pc The address to set the state for.
	 * @return The created stack map table state.
	 * @throws IllegalArgumentException If the address is negative.
	 * @since 2017/07/29
	 */
	public StackMapTableState add(int __pc)
		throws IllegalArgumentException
	{
		// {@squirreljme.error JI1r
		if (__pc < 0)
			throw new IllegalArgumentException("JI1r");
		
		StackMapTableState rv;
		this._states.put(__pc, (rv = new StackMapTableState(this._locals,
			this._stack, this._depth)));
		return rv;
	}
	
	/**
	 * Builds the stack map table representation.
	 *
	 * @return The resulting stack map table.
	 * @since 2017/07/16
	 */
	public StackMapTable build()
	{
		return new StackMapTable(this._states);
	}
}


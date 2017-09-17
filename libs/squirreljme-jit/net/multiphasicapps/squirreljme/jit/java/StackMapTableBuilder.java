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
import net.multiphasicapps.squirreljme.jit.JITException;
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
	 * @param __h The handle of the current class.
	 * @param __oc The class which contains the method to build a stack map
	 * for, this is needed for {@code this} references.
	 * @param __bc The byte code for the current method, required for handling
	 * initialization of new objects.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/24 
	 */
	public StackMapTableBuilder(MethodFlags __f, MethodHandle __h,
		ByteCode __bc)
		throws NullPointerException
	{
		// Check
		if (__f == null || __h == null || __bc == null)
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
		
		// Setup initial state
		// {@squirreljme.error JI2l The arguments that are required for the
		// given method exceeds the maximum number of permitted local
		// variables. (The method in question; The required number of local
		// variables; The maximum number of local variables)}
		boolean isinstance = !__f.isStatic();
		JavaType[] jis = __h.javaStack(isinstance);
		int jn = jis.length;
		if (jn > maxlocals)
			throw new JITException(String.format("JI2l %s %d %d", __h, jn,
				maxlocals));
		
		// Setup entries
		// If this is an instance initializer method then only the first
		// argument is not initialized
		boolean isiinit = isinstance && __h.name().isInstanceInitializer();
		for (int i = 0; i < jn; i++)
			locals[i] = new StackMapTableEntry(jis[i],
				(isiinit ? (i != 0) : true));
		
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


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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This contains the state of variables which are located in local variables
 * or placed on the stack.
 *
 * @since 2017/08/12
 */
public final class VariableState
{
	/** Treads which are available. */
	private final Map<VariableLocation, VariableTread> _treads;
	
	/**
	 * Initializes the variable state.
	 *
	 * @param __smt The stack map table.
	 * @param __ms The number of entries on the stack.
	 * @param __ml The number of entries in locals variables.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/12
	 */
	public VariableState(StackMapTable __smt, int __ms, int __ml)
		throws NullPointerException
	{
		// Check
		if (__smt == null)
			throw new NullPointerException("NARG");
		
		// Initialize treads
		Map<VariableLocation, VariableTread> treads = new LinkedHashMap<>();
		this._treads = treads;
		for (VariableLocation l : VariableLocation.values())
			treads.put(l, new VariableTread(l, l.size(__ms, __ml),
				l.isStack()));
		
		// Initialize treads
		VariableTread locals = treads.get(VariableLocation.LOCAL);
		
		// Initialize local variables from the stack map state
		StackMapTableState state = __smt.get(0);
		for (int i = 0; i < __ml; i++)
		{
			StackMapTableEntry e = state.getLocal(i);
			if (e != null)
				locals.__set(i, e);
		}
		
		// The exception register is set to some throwable
		treads.get(VariableLocation.THROWING_EXCEPTION).__set(0,
			new JavaType(new FieldDescriptor("Ljava/lang/Throwable;")));
	}
	
	/**
	 * Returns the tread for the given location.
	 *
	 * @param __l The location to get the tread for.
	 * @return The tread for the given location.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/03
	 */
	public VariableTread getTread(VariableLocation __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		return this._treads.get(__l);
	}
	
	/**
	 * Obtains the typed variable based on the given variable.
	 *
	 * @param __v The variable to obtain a typed variable for.
	 * @return The typed variable to use.
	 * @throws JITException If the variable is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/03
	 */
	public TypedVariable getTypedVariable(Variable __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return getTread(__v.location()).getTypedVariable(__v.index());
	}
	
	/**
	 * Initializes all instances of the given variable.
	 *
	 * @param __ik The variable to initialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/20
	 */
	public void initializeVariable(InitializationKey __ik)
		throws NullPointerException
	{
		// Check
		if (__ik == null)
			throw new NullPointerException("NARG");
		
		// Go through all treads and initialize them individually
		for (VariableTread t : treads())
			t.initializeVariable(__ik);
	}
	
	/**
	 * Returns the local variable tread.
	 *
	 * @return The local variable tread.
	 * @since 2017/09/02
	 */
	public VariableTread locals()
	{
		return getTread(VariableLocation.LOCAL);
	}
	
	/**
	 * Returns the number of local variables.
	 *
	 * @return The number of local variables.
	 * @since 2017/08/13
	 */
	public int maxLocals()
	{
		return locals().storageSize();
	}
	
	/**
	 * Returns the number of stack variables.
	 *
	 * @return The number of stack variables.
	 * @since 2017/08/13
	 */
	public int maxStack()
	{
		return stack().storageSize();
	}
	
	/**
	 * Returns the number of variable which are available.
	 *
	 * @return The number of variables which are recorded.
	 * @since 2017/08/13
	 */
	public int numVariables()
	{
		return maxLocals() + maxStack();
	}
	
	/**
	 * Returns the stack tread.
	 *
	 * @return The stack tread.
	 * @since 2017/09/02
	 */
	public VariableTread stack()
	{
		return getTread(VariableLocation.STACK);
	}
	
	/**
	 * Returns an array containing all of the treads.
	 *
	 * @return An array containing all of the treads.
	 * @since 2017/09/20
	 */
	public VariableTread[] treads()
	{
		Collection<VariableTread> vs = this._treads.values();
		return vs.toArray(new VariableTread[vs.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public String toString()
	{
		return this._treads.toString();
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * This represents the state of a single operation as it appears in the
 * program.
 *
 * @since 2016/03/24
 */
public final class JVMProgramAtom
	implements Comparable<JVMProgramAtom>
{
	/** Lock. */
	final Object lock;	
	
	/** The address of this operation. */
	protected final int pcaddr;
	
	/** Locals. */
	protected final JVMProgramVars locals;
	
	/** Stack. */
	protected final JVMProgramVars stack;
	
	/** Is this a derived atom? */
	protected final boolean isderived;
	
	/** The program state which owns this atom. */
	final JVMProgramState _programstate;
	
	/** The current array index. */
	volatile int _index;
	
	/** Out of domain operator link to be executed but its value is dropped. */
	private volatile JVMOperatorLink _oodlink;
	
	/**
	 * Initializes the base of the atom.
	 *
	 * @param __pc The PC address of the atom.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/24
	 */
	JVMProgramAtom(JVMProgramState __ps, int __pc)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set state
		_programstate = __ps;
		lock = _programstate.lock;
		
		// Set address
		pcaddr = __pc;
		
		// Setup state
		locals = new JVMProgramVars(this, false, null);
		stack = new JVMProgramVars(this, true, null);
		isderived = false;
	}
	
	/**
	 * This initializes an atom which is derived from another.
	 *
	 * @param __div The atom this is derived from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/27
	 */
	private JVMProgramAtom(JVMProgramAtom __div)
		throws NullPointerException
	{
		// Check
		if (__div == null)
			throw new NullPointerException("NARG");
		
		// Is derived
		_programstate = __div._programstate;
		lock = _programstate.lock;
		isderived = true;
		pcaddr = __div.pcaddr;
		
		// Lock
		synchronized (lock)
		{
			// Base JVMProgramVars
			locals = new JVMProgramVars(this, false, __div.locals);
			stack = new JVMProgramVars(this, true, __div.stack);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/24
	 */
	@Override
	public int compareTo(JVMProgramAtom __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Derived atoms are always last compared to non-derived ones
		// Derived atoms are also both considered equal
		boolean adv = isderived;
		boolean bdv = __b.isderived;
		if (adv || bdv)
		{
			if (adv && !bdv)
				return 1;
			else if (!adv && bdv)
				return -1;
			return 0;
		}
		
		// Check addresses
		int apc = pcaddr;
		int bpc = __b.pcaddr;
		if (apc < bpc)
			return -1;
		else if (apc > bpc)
			return 1;
		return 0;
	}
	
	/**
	 * Copies from the target atom to this one.
	 *
	 * @param __v Copies from the given atom to this one.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/27
	 */
	public JVMProgramAtom copyFrom(JVMProgramAtom __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Just copy the variables
			locals.copyFrom(__v.locals);
			stack.copyFrom(__v.stack);
			
			// Copy the link
			_oodlink = __v._oodlink;
		}
		
		// Self
		return this;
	}
	
	/**
	 * This creates an atom which is derived from this one and duplicates
	 * its entire state, it is not injected into the list of operations.
	 *
	 * This is used to setup the state for a following atom so that it may
	 * be set and checked to make sure the state is consistent if it is
	 * known.
	 *
	 * @return The derived atom.
	 * @since 2016/03/27
	 */
	public JVMProgramAtom derive()
	{
		return new JVMProgramAtom(this);
	}
	
	/**
	 * Returns the address associated with this atom.
	 *
	 * @return The PC address, or {@code -1} if this is derived.
	 * @since 2016/03/24
	 */
	public int getAddress()
	{
		if (isderived)
			return -1;
		return pcaddr;
	}
	
	/**
	 * Returns the out-of-domain operator link which is used when work with no
	 * result must be performed.
	 *
	 * @return The operator link for this atom.
	 * @since 2016/03/29
	 */
	public JVMOperatorLink getLink()
	{
		// Lock
		synchronized (lock)
		{
			return _oodlink;
		}
	}
	
	/**
	 * Returns the local variables state.
	 *
	 * @return The state of local variables.
	 * @since 2016/03/25
	 */
	public JVMProgramVars locals()
	{
		return locals;
	}
	
	/**
	 * Returns the atom which follows this one.
	 *
	 * @return The atom after this one or {@code null} if this is the last
	 * one.
	 * @since 2016/03/25
	 */
	public JVMProgramAtom next()
	{
		// Lock
		synchronized (lock)
		{
			// Get atoms
			List<JVMProgramAtom> ll = _programstate._atoms;
			int udx = _index + 1;
			
			// Must be within bounds
			if (udx >= ll.size())
				return null;
			return ll.get(udx);
		}
	}
	
	/**
	 * Returns the atom which this one follows.
	 *
	 * @return The atom before this one or {@code null} if this is the
	 * first one.
	 * @since 2016/03/25
	 */
	public JVMProgramAtom previous()
	{
		// Lock
		synchronized (lock)
		{
			// Get atoms
			List<JVMProgramAtom> ll = _programstate._atoms;
			int udx = _index - 1;
			
			// Must be within bounds
			if (udx < 0)
				return null;
			return ll.get(udx);
		}
	}
	
	/**
	 * Sets the out of domain link which is used to execute operations which
	 * contain no output to be placed on the stack or a local variable.
	 *
	 * @param __l The operation to be performed.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	public JVMProgramAtom setLink(JVMOperatorLink __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Set
			_oodlink = __l;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the stack variables state.
	 *
	 * @rteurn The state of stack variables.
	 * @since 2016/03/25
	 */
	public JVMProgramVars stack()
	{
		return stack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/24
	 */
	@Override
	public String toString()
	{
		// Build up
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		
		// Add address
		sb.append("pc=");
		sb.append(pcaddr);
		
		// Locals
		sb.append(", locals=");
		sb.append(locals);
		
		// Stack
		sb.append(", stack=");
		sb.append(stack);
		
		// Link, which is optional
		JVMOperatorLink ll = _oodlink;
		if (ll != null)
		{
			sb.append(", link=");
			sb.append(ll);
		}
		
		// Finish it
		sb.append('}');
		return sb.toString();
	}
}


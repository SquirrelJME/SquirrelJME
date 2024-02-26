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
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Set;

/**
 * This represents the targets of jumps that an instruction may jump to.
 *
 * @since 2019/03/30
 */
public final class InstructionJumpTargets
	extends AbstractList<InstructionJumpTarget>
{
	/** Normal jumps. */
	private final InstructionJumpTarget[] _normal;
	
	/** Exceptional jumps. */
	private final InstructionJumpTarget[] _exception;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** Hashcode. */
	private int _hash;
	
	/**
	 * Initializes empty jump targets.
	 *
	 * @since 2019/04/11
	 */
	public InstructionJumpTargets()
	{
		this._normal = new InstructionJumpTarget[0];
		this._exception = new InstructionJumpTarget[0];
	}
	
	/**
	 * Initializes the jump targets.
	 * @param __n Normal jumps.
	 * @param __e Exceptional jumps.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public InstructionJumpTargets(InstructionJumpTarget[] __n,
		InstructionJumpTarget[] __e)
		throws NullPointerException
	{
		if (__n == null || __e == null)
			throw new NullPointerException("NARG");
		
		// Load into sorted sets
		Set<InstructionJumpTarget> nrm = new SortedTreeSet<>(),
			exe = new SortedTreeSet<>();
		
		// Add normals
		for (InstructionJumpTarget i : __n)
			if (i == null)
				throw new NullPointerException("NARG");
			else
				nrm.add(i);
		
		// Add exceptional
		for (InstructionJumpTarget i : __e)
			if (i == null)
				throw new NullPointerException("NARG");
			else
				exe.add(i);
		
		// Set
		this._normal = nrm.<InstructionJumpTarget>toArray(
			new InstructionJumpTarget[nrm.size()]);
		this._exception = exe.<InstructionJumpTarget>toArray(
			new InstructionJumpTarget[exe.size()]);
	}
	
	/**
	 * Returns the number of normal functional flow.
	 * 
	 * @return The number of normal instructional flow.
	 * @since 2023/05/31
	 */
	public int countNormal()
	{
		return this._normal.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Obtains the given jump target.
	 *
	 * @param __i The index.
	 * @return The target jump target.
	 * @since 2019/03/31
	 */
	@Override
	public final InstructionJumpTarget get(int __i)
	{
		InstructionJumpTarget[] normal = this._normal;
		int numnormal = normal.length;
		
		// Treat both arrays as a single part
		if (__i >= numnormal)
			return this._exception[__i - numnormal];
		return this._normal[__i];
	}
	
	/**
	 * Checks if this has any exception specified.
	 * 
	 * @return If this has any exception.
	 * @since 2023/05/31
	 */
	public boolean hasAnyException()
	{
		return this._exception.length > 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Checks if any of the jump targets has an address which follows the
	 * given address.
	 *
	 * @param __pc The address to check.
	 * @return True if any address in the jump targets has an address which
	 * is higher than this address.
	 * @since 2019/03/30
	 */
	public final boolean hasLaterAddress(int __pc)
	{
		for (int i = 0, n = this.size(); i < n; i++)
			if (this.get(i).target() > __pc)
				return true;
		
		return false;
	}
	
	/**
	 * Checks if any of the jump targets has an address which is or follows
	 * the given address.
	 *
	 * @param __pc The address to check.
	 * @return True if any address in the jump targets has an address which
	 * is the same or higher than this address.
	 * @since 2019/04/11
	 */
	public final boolean hasSameOrLaterAddress(int __pc)
	{
		for (int i = 0, n = this.size(); i < n; i++)
			if (this.get(i).target() >= __pc)
				return true;
		
		return false;
	}
	
	/**
	 * Returns if this is empty or not.
	 *
	 * @return If this is empty or not.
	 * @since 2019/03/31
	 */
	@Override
	public final boolean isEmpty()
	{
		return this.size() == 0;
	}
	
	/**
	 * Returns if this is an exception jump.
	 *
	 * @param __i The index to get.
	 * @return If this is an exception index.
	 * @since 2019/03/31
	 */
	public final boolean isException(int __i)
	{
		return __i >= this._normal.length;
	}
	
	/**
	 * Returns the number of jump targets.
	 *
	 * @return The number of jump targets.
	 * @since 2019/03/31
	 */
	@Override
	public final int size()
	{
		return this._normal.length + this._exception.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				"[N:" + Arrays.asList(this._normal) +
				", E: " + Arrays.asList(this._exception) + "]"));
		
		return rv;
	}
}


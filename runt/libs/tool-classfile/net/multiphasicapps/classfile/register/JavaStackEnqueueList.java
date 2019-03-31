// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;

/**
 * This contains every register which can be cleared after the specified
 * operation completes. Enqueue lists are always sorted from lowest to
 * highest.
 *
 * This class is immutable.
 *
 * @since 2019/03/30
 */
public final class JavaStackEnqueueList
{
	/** The index where the stack entries start. */
	protected final int stackstart;
	
	/** Registers used. */
	private final int[] _registers;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the enqueue list.
	 *
	 * @param __ss The start of the stack.
	 * @param __rs The registers to enqueue.
	 * @since 2019/03/31
	 */
	public JavaStackEnqueueList(int __ss, int... __rs)
	{
		// Unique and sort
		Set<Integer> uniq = new SortedTreeSet<>();
		for (int i : (__rs == null ? new int[0] : __rs.clone()))
			uniq.add(i);
		
		// Set
		int n = uniq.size(),
			i = 0;
		int[] registers = new int[n];
		for (Integer v : uniq)
			registers[i++] = v;
		this._registers = registers;
		this.stackstart = (__ss > n ? n : __ss);
	}
	
	/**
	 * Initializes the enqueue list.
	 *
	 * @param __ss The start of the stack.
	 * @param __rs The registers to enqueue.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/31
	 */
	public JavaStackEnqueueList(int __ss, Iterable<Integer> __rs)
		throws NullPointerException
	{
		if (__rs == null)
			throw new NullPointerException("NARG");
		
		// Add to sorted set
		Set<Integer> uniq = new SortedTreeSet<>();
		for (Integer i : __rs)
			if (i == null)
				throw new NullPointerException("NARG");
			else
				uniq.add(i);
		
		// Set
		int n = uniq.size(),
			i = 0;
		int[] registers = new int[n];
		for (Integer v : uniq)
			registers[i++] = v;
		this._registers = registers;
		this.stackstart = (__ss > n ? n : __ss);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Gets the given entry.
	 *
	 * @param __i The index to get.
	 * @return The register at this index.
	 * @since 2019/03/30
	 */
	public final int get(int __i)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns if this is empty.
	 *
	 * @return If this is empty.
	 * @since 2019/03/30
	 */
	public final boolean isEmpty()
	{
		return this._registers.length == 0;
	}
	
	/**
	 * Returns an enqueue list which contains only locals.
	 *
	 * @return An enqueue list containing only locals.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList onlyLocals()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the registers that have been enqueued.
	 *
	 * @return The enqueued set of registers.
	 * @since 2019/03/30
	 */
	public final int[] registers()
	{
		return this._registers.clone();
	}
	
	/**
	 * Returns the number of entries here.
	 *
	 * @return The number of entries.
	 * @since 2019/03/30
	 */
	public final int size()
	{
		return this._registers.length;
	}
	
	/**
	 * Returns the top-most entry.
	 *
	 * @return The top most entry or {@code -1} if this is empty.
	 * @since 2019/03/30
	 */
	public final int top()
	{
		int[] registers = this._registers;
		int len = registers.length;
		
		if (len == 0)
			return -1;
		return registers[len - 1];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Trims the top entry from the enqueue list and returns the new list
	 * with the top missing.
	 *
	 * @return The resulting list.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList trimTop()
	{
		if (this.isEmpty())
			return this;
		
		throw new todo.TODO();
	}
}


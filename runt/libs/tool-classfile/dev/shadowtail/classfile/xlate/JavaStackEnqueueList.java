// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
	implements Iterable<Integer>
{
	/** The index where the stack entries start. */
	public final int stackstart;
	
	/** Registers used. */
	private final int[] _registers;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** Hashcode. */
	private int _hash;
	
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
		if (__o == this)
			return true;
		
		if (!(__o instanceof JavaStackEnqueueList))
			return false;
		
		JavaStackEnqueueList o = (JavaStackEnqueueList)__o;
		if (this.hashCode() != o.hashCode())
			return false;
		
		return this.stackstart == o.stackstart &&
			Arrays.equals(this._registers, o._registers);
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
		return this._registers[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
		{
			rv = ~this.stackstart;
			for (int i : this._registers)
				rv -= i;
			
			this._hash = rv;
		}
		return rv;
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
	 * {@inheritDoc}
	 * @since 2019/04/06
	 */
	@Override
	public final Iterator<Integer> iterator()
	{
		return new __Iterator__(this._registers);
	}
	
	/**
	 * Returns an enqueue list which contains only locals.
	 *
	 * @return An enqueue list containing only locals.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList onlyLocals()
	{
		// Copy just up to the stack part
		int[] from = this._registers;
		int ss = this.stackstart;
		int[] rv = new int[ss];
		for (int i = 0; i < ss; i++)
			rv[i] = from[i];
		
		return new JavaStackEnqueueList(ss, rv);
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
		StringBuilder sb = new StringBuilder("ENQ[");
		
		int stackstart = this.stackstart, dx = 0;
		boolean comma = false;
		for (int v : this._registers)
		{
			if (dx++ == stackstart)
				sb.append("| ");
			else if (comma)
				sb.append(", ");
			comma = true;
			
			sb.append(v);
		}
		if (dx == stackstart)
			sb.append('|');
		sb.append(']');
		
		return sb.toString();
	}
	
	/**
	 * Merges both of these stack enqueue lists into one.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The merged result.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public static final JavaStackEnqueueList merge(JavaStackEnqueueList __a,
		JavaStackEnqueueList __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// If one side is empty just use the other
		if (__a.isEmpty())
			return __b;
		else if (__b.isEmpty())
			return __a;
		
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
		// Do not trim empty pieces
		int[] from = this._registers;
		int n = from.length - 1;
		if (n < 0)
			return this;
		
		// Copy bits
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = from[i];
		
		return new JavaStackEnqueueList(this.stackstart, rv);
	}
	
	/**
	 * Iterator over stack slots.
	 *
	 * @since 2019/04/06
	 */
	private static final class __Iterator__
		implements Iterator<Integer>
	{
		/** Input register. */
		private final int[] _registers;
		
		/** Current index. */
		private int _at;
		
		/**
		 * Initializes the iterator.
		 *
		 * @param __r The registers used.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/06
		 */
		private __Iterator__(int[] __r)
			throws NullPointerException
		{
			if (__r == null)
				throw new NullPointerException("NARG");
			
			this._registers = __r;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/04/06
		 */
		@Override
		public final boolean hasNext()
		{
			return (this._at < this._registers.length);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/04/06
		 */
		@Override
		public final Integer next()
		{
			int at = this._at;
			int[] registers = this._registers;
			
			if (at >= registers.length)
				throw new NoSuchElementException("NSEE");
			
			this._at = at + 1;
			return registers[at];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/04/06
		 */
		@Override
		public final void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}


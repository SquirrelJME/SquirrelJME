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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents a basic block which exists to wrap a small portion of
 * the byte code to fix it within a set of requirements. Basic blocks start at
 * addresses which are a target of a jump (normal or exceptional), this means
 * that it becomes known where all non-normal execution flow instructions
 * land. This removes the need to have an overly complex execution graph.
 * Basic blocks end where entry points exist or the method terminates via
 * throwing an exception or returning.
 *
 * @see BasicBlocks
 * @since 2017/08/01
 */
public final class BasicBlock
	implements Iterable<Instruction>
{
	/** The owning byte code. */
	protected final ByteCode code;
	
	/** The starting index of the block. */
	protected final int startdx;
	
	/** The ending index of the block. */
	protected final int enddx;
	
	/** The jump target associated with this block. */
	private volatile Reference<JumpTarget> _target;
	
	/**
	 * Initializes the basic block.
	 *
	 * @param __code The code the basic block represents a block within.
	 * @param __lo The lowest index.
	 * @param __hi The highest index.
	 * @throws IllegalArgumentException If the basic block range is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/03
	 */
	public BasicBlock(ByteCode __code, int __lo, int __hi)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI1x Invalid combination of basic block ranges
		// (The low index; The high index; The number of instructions in the
		// method)}
		int count = __code.instructionCount();
		if (__lo < 0 || __lo > __hi || __lo >= count ||
			__hi < 0 || __hi < __lo)
			throw new IllegalArgumentException(String.format("JI1x %d %d %d",
				__lo, __hi, count));
		
		// Set
		this.code = __code;
		this.startdx = __lo;
		this.enddx = __hi;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/08
	 */
	public Iterator<Instruction> iterator()
	{
		return new __Iterator__(this.code, this.startdx, this.enddx);
	}
	
	/**
	 * This returns the jump target for the start of this basic block which
	 * can be used as a {@link BasicBlockKey}.
	 *
	 * @return The jump target for the start of this block.
	 * @since 2017/08/07
	 */
	public JumpTarget jumpTarget()
	{
		Reference<JumpTarget> ref = this._target;
		JumpTarget rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._target = new WeakReference<>(
				(rv = new JumpTarget(this.startdx)));
		
		return rv;
	}
	
	/**
	 * This iterates over instructions in the basic block.
	 *
	 * @since 2017/08/08
	 */
	private static final class __Iterator__
		implements Iterator<Instruction>
	{
		/** The byte code to get instruction. */
		protected final ByteCode code;
		
		/** The end point. */
		protected final int end;
		
		/** The current position. */
		private volatile int _at;
		
		/**
		 * Initializes the iterator.
		 *
		 * @param __code The code to iterate over.
		 * @param __s The start position.
		 * @param __e The end position.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/08/08
		 */
		private __Iterator__(ByteCode __code, int __s, int __e)
			throws NullPointerException
		{
			// Check
			if (__code == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.code = __code;
			this.end = __e;
			this._at = __s;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/08/08
		 */
		@Override
		public boolean hasNext()
		{
			return this._at < this.end;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/08/08
		 */
		@Override
		public Instruction next()
			throws NoSuchElementException
		{
			// End?
			int at = this._at,
				end = this.end;
			if (at >= end)
				throw new NoSuchElementException("NSEE");
			
			// Increment
			this._at = at + 1;
			
			// Get instruction at the current position
			return this.code.getByIndex(at);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/08/08
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}


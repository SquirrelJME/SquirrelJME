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
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class manages all of the basic blocks which are used within a program
 * during processing and compilation. Basic blocks may be processed multiple
 * times during compilation for various input changes.
 *
 * @see BasicBlock
 * @since 2017/08/01
 */
@Deprecated
public final class BasicBlocks
	implements Iterable<BasicBlock>
{
	/** The owning byte code. */
	protected final ByteCode code;
	
	/**
	 * Byte code ranges which appear in integer pairs, indexes are used so
	 * that the sub-references are easier to be used iteration wise.
	 */
	private final int[] _ranges;
	
	/** Basic blocks cache. */
	private volatile Reference<BasicBlock>[] _blocks;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the basic block representation.
	 *
	 * @param __code The byte code for the given method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/02
	 */
	public BasicBlocks(ByteCode __code)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.code = __code;
		
		// Determining the ranges is simple. Essentially instructions that are
		// the start of a new basic block follow non-natural flowing
		// instructions or are jump targets themselves
		int count = __code.instructionCount(),
			rangeat = 0,
			fromdx = 0;
		int[] ranges = new int[count * 2],
			jumptargets = __code.jumpTargetAddresses();
		boolean lastnatflow = false;
		for (int i = 0; i <= count; i++)
		{
			// Instructions which precede this instruction may have non-natural
			// flow which means that this instruction is now the start of a new
			// basic block
			boolean end = (i == count);
			boolean hadlast = lastnatflow;
			Instruction u;
			if (end)
				u = null;
			else
			{
				u = __code.getByIndex(i);
				lastnatflow = u.hasNaturalFlow();
			}
			
			// The first instruction could be a jump target but if it is then
			// it would always be in a basic block by itself, the greater than
			// zero check prevents this from happening
			if (i > 0 && (i == count || !hadlast ||
				Arrays.binarySearch(jumptargets, u.address()) >= 0))
			{
				ranges[rangeat++] = fromdx;
				ranges[rangeat++] = i;
				
				// The from index is now set to the current instruction which
				// starts this basic block
				fromdx = i;
			}
		}
		
		// Store
		this._ranges = Arrays.copyOf(ranges, rangeat);
		this._blocks = __createArray(rangeat / 2);
	}
	
	/**
	 * Returns the basic block by its own index.
	 *
	 * @param __i The index of the block.
	 * @return The basic block for the given index range.
	 * @throws IndexOutOfBoundsException
	 */
	public BasicBlock get(int __i)
		throws IndexOutOfBoundsException
	{
		Reference<BasicBlock>[] blocks = this._blocks;
		if (__i < 0 || __i >= blocks.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		Reference<BasicBlock> ref = blocks[__i];
		BasicBlock rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			int dx = __i * 2;
			int[] ranges = this._ranges;
			blocks[__i] = new WeakReference<>((rv = new BasicBlock(this.code,
				ranges[dx], ranges[dx + 1])));
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/08
	 */
	@Override
	public Iterator<BasicBlock> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * Returns the number of basic blocks.
	 *
	 * @return The number of basic blocks.
	 * @since 2017/08/03
	 */
	public int size()
	{
		return this._blocks.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/02
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("[");
			
			// Record all the ranges
			ByteCode code = this.code;
			int[] ranges = this._ranges;
			for (int i = 0, n = ranges.length; i < n; i += 2)
			{
				if (i > 0)
					sb.append(", ");
				
				// Use [, ) for inclusive and exclusive since the second
				// part of the range reaches past the end
				sb.append('[');
				sb.append(code.indexToAddress(ranges[i]));
				sb.append(", ");
				sb.append(code.indexToAddress(ranges[i + 1]));
				sb.append(')');
			}
			
			sb.append(']');
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Creates a new reference array.
	 *
	 * @param __i The number of elements in the array.
	 * @return The created array.
	 * @since 2017/08/03
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<BasicBlock>[] __createArray(int __i)
	{
		return (Reference<BasicBlock>[])((Object)new Reference[__i]);
	}
	
	/**
	 * The iterator over basic blocks.
	 *
	 * @since 2017/08/08
	 */
	private final class __Iterator__
		implements Iterator<BasicBlock>
	{
		/** The end index. */
		protected final int end =
			BasicBlocks.this.size();
		
		/** The current index. */
		private volatile int _at;
		
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
		public BasicBlock next()
			throws NoSuchElementException
		{
			// End?
			int at = this._at,
				end = this.end;
			if (at >= end)
				throw new NoSuchElementException("NSEE");
			
			// Increment
			this._at = at + 1;
			
			// Get the next block
			return BasicBlocks.this.get(at);
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


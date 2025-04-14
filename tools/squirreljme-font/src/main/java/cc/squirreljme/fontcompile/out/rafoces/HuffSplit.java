// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Huffman split list entry.
 *
 * @since 2024/06/03
 */
public final class HuffSplit
{
	/** The bits to use. */
	public final HuffBits bits;
	
	/** The length of the code sequence. */
	public final int sequenceLen;
	
	/**
	 * Initializes the split information.
	 *
	 * @param __bits The huffman bits.
	 * @param __seqLen The sequence length.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public HuffSplit(HuffBits __bits, int __seqLen)
		throws NullPointerException
	{
		if (__bits == null)
			throw new NullPointerException("NARG");
		
		this.bits = __bits;
		this.sequenceLen = __seqLen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		return this.sequenceLen + "[" + this.bits + "]";
	}
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.string;

/**
 * This is a sequence which provides a sub-sequence.
 *
 * @since 2018/09/29
 */
public final class SubBasicSequenceSequence
	extends BasicSequence
{
	/** The sequence to wrap. */
	protected final BasicSequence sequence;
	
	/** The start index. */
	protected final int start;
	
	/** The length. */
	protected final int length;
	
	/**
	 * Initializes this sequence.
	 *
	 * @param __q The sequence to wrap.
	 * @param __s The start point.
	 * @param __e The end point.
	 * @throws IndexOutOfBoundsException If the start is negative, the end
	 * exceeds the sequence length, or the start is greater than the end.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/29
	 */
	public SubBasicSequenceSequence(BasicSequence __q, int __s, int __e)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__q == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0e Out of bounds sequence access.}
		if (__s < 0 || __s > __e || __e > __q.length())
			throw new IndexOutOfBoundsException("ZZ0e");
		
		this.sequence = __q;
		this.start = __s;
		this.length = __e - __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/29
	 */
	@Override
	public final char charAt(int __i)
		throws StringIndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ0f Sub-basic sequence index is out of
		// bounds. (The index)}
		if (__i < 0 || __i > this.length)
			throw new StringIndexOutOfBoundsException("ZZ0f " + __i);
		
		return this.sequence.charAt(this.start + __i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/29
	 */
	@Override
	public final int length()
	{
		return this.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/29
	 */
	@Override
	public final SubBasicSequenceSequence subSequence(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ0g Out of bounds sequence access.}
		int start = this.start,
			newend = start + __e;
		if (__s < 0 || __s > __e || __e > newend)
			throw new IndexOutOfBoundsException("ZZ0g");
		
		return new SubBasicSequenceSequence(this.sequence,
			start + __s, newend);
	}
}


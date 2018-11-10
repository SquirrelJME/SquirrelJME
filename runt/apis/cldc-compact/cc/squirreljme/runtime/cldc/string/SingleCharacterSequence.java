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
 * This is a sequence which contains only a single character.
 *
 * @since 2018/10/12
 */
public final class SingleCharacterSequence
	implements BasicSequence
{
	/** The value to store. */
	protected final char value;
	
	/**
	 * Initializes the single character sequence.
	 *
	 * @param __v The value to use.
	 * @since 2018/10/12
	 */
	public SingleCharacterSequence(char __v)
	{
		this.value = __v;
	}	
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/12
	 */
	@Override
	public final char charAt(int __i)
		throws StringIndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ0d Only a single character is valid.}
		if (__i != 0)
			throw new StringIndexOutOfBoundsException("ZZ0d");
		
		return this.value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/12
	 */
	@Override
	public final int length()
	{
		return 1;
	}
}


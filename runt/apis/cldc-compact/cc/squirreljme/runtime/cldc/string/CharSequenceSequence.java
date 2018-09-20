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
 * A sequence which uses a character sequence.
 *
 * @since 2018/09/20
 */
public final class CharSequenceSequence
	implements BasicSequence
{
	/** The sequence to wrap. */
	protected final CharSequence sequence;
	
	/**
	 * Initializes the sequence.
	 *
	 * @param __cs The sequence to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	public CharSequenceSequence(CharSequence __cs)
		throws NullPointerException
	{
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		this.sequence = __cs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public final char charAt(int __i)
		throws StringIndexOutOfBoundsException
	{
		try
		{
			return this.sequence.charAt(__i);
		}
		catch (IndexOutOfBoundsException e)
		{
			if (e instanceof StringIndexOutOfBoundsException)
				throw (StringIndexOutOfBoundsException)e;
			
			// {@squirreljme.error ZZ17 Out of bounds access while accessing
			// character sequence.}
			StringIndexOutOfBoundsException t =
				new StringIndexOutOfBoundsException("ZZ17");
			t.initCause(e);
			throw t;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public final int length()
	{
		return this.sequence.length();
	}
}


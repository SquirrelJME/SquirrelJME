// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.magic;

/**
 * This is a special class which is used by the interpreter so that it can wrap
 * {@link String}s which exist on the host so that they carry a representation
 * in the target virtual machine without requiring the data be copied to an
 * array and duplicated (since host strings are immutable).
 *
 * @since 2016/04/05
 */
public final class IVMCharSequence
{
	/** The internally wrapped sequence. */
	protected final Object internal =
		null;
	
	/**
	 * Initializes the magical character sequence.
	 *
	 * @since 2016/04/05
	 */
	private IVMCharSequence()
	{
	}
	
	/**
	 * Returns the character at the given position.
	 *
	 * @param __i The position to read from.
	 * @return The character at the given position.
	 * @since 2016/04/05
	 */
	public char charAt(int __i)
	{	
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the string length.
	 *
	 * @return The string length.
	 * @since 2016/04/05
	 */
	public int length()
	{	
		throw new ForbiddenMagicError();
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

/**
 * This is the base class for sections and program headers.
 *
 * @since 2016/08/16
 */
abstract class __ELFBaseEntry__
{
	/** The lock used. */
	protected final Object lock;
	
	/** The ELF output used. */
	final ELFOutput _output;
	
	/**
	 * Initializes the base entry.
	 *
	 * @param __eo The output used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/16
	 */
	__ELFBaseEntry__(ELFOutput __eo)
		throws NullPointerException
	{
		if (__eo == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._output = __eo;
		this.lock = __eo._lock;
	}
}


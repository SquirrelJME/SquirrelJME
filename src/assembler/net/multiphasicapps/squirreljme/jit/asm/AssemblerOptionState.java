// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.asm;

/**
 * This is used instead of a boolean since there may be options that were
 * requested which are not valid options (instead of returning {@code null}.
 *
 * @since 2016/07/02
 */
public enum AssemblerOptionState
{
	/** The option is invalid and should not be used. */
	INVALID(false, false),
	
	/** Option is disabled. */
	DISABLED(true, false),
	
	/** Option is enabled. */
	ENABLED(true, true),
	
	/** End. */
	;
	
	/** Is this a valid option? */
	protected final boolean isvalid;
	
	/** Is this enabled? */
	protected final boolean isenabled;
	
	/**
	 * Initializes the option state.
	 *
	 * @param __iv Is this a valid option?
	 * @param __ie Is this option an enabled one?
	 * @since 2016/07/02
	 */
	private AssemblerOptionState(boolean __iv, boolean __ie)
	{
		this.isvalid = __iv;
		this.isenabled = __ie;
	}
}


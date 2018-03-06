// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test;

/**
 * Parameters for a test.
 *
 * @since 2018/03/05
 */
public final class Parameters
{
	/** Return value for the test. */
	protected final Object returnvalue;
	
	/** Arguments for the test. */
	private final Object[] _arguments;
	
	/**
	 * Initializes test parameters.
	 *
	 * @param __rv The return value.
	 * @param __args The arguments.
	 * @since 2018/03/05
	 */
	public Parameters(Object __rv, Object... __args)
	{
		this.returnvalue = __rv;
		this._arguments = (__args != null ? __args.clone() : new Object[0]);
	}
	
	/**
	 * Initializes test parameters.
	 *
	 * @param __rv The return value.
	 * @param __args The arguments.
	 * @since 2018/03/05
	 */
	public static final Parameters parms(Object __rv, Object... __args)
	{
		return new Parameters(__rv, __args);
	}
}


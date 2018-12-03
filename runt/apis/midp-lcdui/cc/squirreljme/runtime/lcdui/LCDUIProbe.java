// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

/**
 * This is a class which provides a probe into any LCDUI object's internals
 * as needed. This is made to simplify the implementation and allow very
 * specific things to be done without needing to consider about access
 * checks and such. This class is very SquirrelJME specific and is not
 * recommended to be used in user code.
 *
 * @since 2018/12/02
 */
public abstract class LCDUIProbe
{
	/** The single probe instance. */
	private static LCDUIProbe _PROBE;
	
	/**
	 * Sets the probe instance to this class.
	 *
	 * @since 2018/12/02
	 */
	protected LCDUIProbe()
	{
		// Set probe if it has not been set
		if (LCDUIProbe._PROBE == null)
			LCDUIProbe._PROBE = this;
	}
	
	/**
	 * Returns the probe.
	 *
	 * @return The probe.
	 * @since 2018/12/02
	 */
	public static final LCDUIProbe probe()
	{
		return _PROBE;
	}
}


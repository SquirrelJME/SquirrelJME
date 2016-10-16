// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launcher;

/**
 * This is used to represent the configuration that a program may use for its
 * main library.
 *
 * @since 2016/10/16
 */
public enum MicroEditionConfiguration
{
	/** CLDC 1.0. */
	CLDC_1_0("CLDC-1.0"),
	
	/** CLDC 1.1. */
	CLDC_1_1("CLDC-1.1"),
	
	/** CLDC 1.8 (Compact). */
	CLDC_1_8_COMPACT("CLDC-1.8-Compact"),
	
	/** CLDC 1.8 (Full). */
	CLDC_1_8("CLDC-1.8"),
	
	/** End. */
	;
	
	/** The string representation. */
	protected final String string;
	
	/**
	 * Initializes the configuration detail.
	 *
	 * @param __s The string used to represent it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/16
	 */
	private MicroEditionConfiguration(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/16
	 */
	@Override
	public String toString()
	{
		return string;
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classwriter;

/**
 * This represents the version number that a class file may be.
 *
 * @since 2016/06/20
 */
public enum OutputVersion
{
	/** CLDC 1.0 (JSR 30). */
	CLDC_1((45 << 16) + 3),
	
	/** CLDC 1.1 (JSR 139). */
	CLDC_1_1(47 << 16),
	
	/** CLDC 8 (aka Java 7). */
	CLDC_8(51 << 16),
	
	/** End. */
	;
	
	/** The version identifier. */
	protected final int version;
	
	/**
	 * Initializes the version information.
	 *
	 * @param __v The class version to use.
	 * @since 2016/06/20
	 */
	private OutputVersion(int __v)
	{
		this.version = __v;
	}
	
	/**
	 * Returns the class version number.
	 *
	 * @return The class version number.
	 * @since 2016/06/20
	 */
	public final int version()
	{
		return this.version;
	}
}


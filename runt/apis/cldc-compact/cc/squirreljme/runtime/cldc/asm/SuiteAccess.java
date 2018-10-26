// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * Access to suites and other suites which are available for usage.
 *
 * @since 2018/10/26
 */
public class SuiteAccess
{
	/**
	 * Returns the suites which are available for usage.
	 *
	 * @return The suites which are available for usage.
	 * @since 2018/10/26
	 */
	public static final native String[] availableSuites();
}


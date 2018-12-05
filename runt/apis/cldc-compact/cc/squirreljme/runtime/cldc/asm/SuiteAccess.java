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

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

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
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native String[] availableSuites();
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * This is used to package API calls which are either {@link Object[]} if they
 * use in-VM boxed types, or {@link int[]} if they consist of raw pointers
 * and such.
 *
 * @since 2019/05/06
 */
public final class UniversalPackage
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/06
	 */
	private UniversalPackage()
	{
	}
}


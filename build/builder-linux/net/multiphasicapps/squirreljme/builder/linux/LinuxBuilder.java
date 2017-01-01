// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux;

import net.multiphasicapps.squirreljme.builder.TargetBuilder;

/**
 * This is the base buidler for all Linux based systems.
 *
 * @since 2016/08/15
 */
public abstract class LinuxBuilder
	extends TargetBuilder
{
	/**
	 * Initializes the base builder.
	 *
	 * @param __sug Suggested targets.
	 * @since 2016/08/15
	 */
	public LinuxBuilder(String... __sug)
	{
		super(__sug);
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import java.io.IOException;

/**
 * Wraps the output and makes it very compact.
 *
 * @since 2023/06/19
 */
public class CompactCTokenOutput
	extends __FormattedCTokenOutput__
{
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public CompactCTokenOutput(CTokenOutput __wrap)
		throws NullPointerException
	{
		super(__wrap);
	}
}

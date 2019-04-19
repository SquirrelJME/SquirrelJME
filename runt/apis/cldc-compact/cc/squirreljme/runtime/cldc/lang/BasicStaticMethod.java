// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.asm.StaticMethod;

/**
 * This represents a basic static method.
 *
 * @since 2019/04/19
 */
public final class BasicStaticMethod
	implements StaticMethod
{
	/** The method index this refers to. */
	private int _index;
	
	/**
	 * Internally initialized.
	 *
	 * @since 2019/04/19
	 */
	private BasicStaticMethod()
	{
	}
}


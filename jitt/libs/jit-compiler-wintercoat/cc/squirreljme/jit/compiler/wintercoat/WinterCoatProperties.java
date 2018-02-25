// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler.wintercoat;

import cc.squirreljme.jit.compiler.TargetProperties;
import cc.squirreljme.jit.objectfile.DataProperties;
import cc.squirreljme.jit.objectfile.Endian;
import cc.squirreljme.jit.objectfile.IntegerType;

/**
 * This is used to describe how WinterCoat compiles code.
 *
 * @since 2018/02/24
 */
public final class WinterCoatProperties
	extends TargetProperties
{
	/**
	 * Initializes the properties.
	 *
	 * @since 2018/02/24
	 */
	public WinterCoatProperties()
	{
		super(new DataProperties(Endian.BIG, IntegerType.LONG));
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.sqc;

import cc.squirreljme.jvm.aot.summercoat.SummerCoatHandler;
import java.io.OutputStream;

/**
 * Serializes SQCs to a storable format on the disk.
 * 
 * This is a handler and can directly pipe into a disk space format.
 *
 * @since 2022/09/02
 */
public final class SqcSerializer
	implements SummerCoatHandler
{
	/**
	 * Initializes the serializer to write to the given output.
	 * 
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/02
	 */
	public SqcSerializer(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
	}
}

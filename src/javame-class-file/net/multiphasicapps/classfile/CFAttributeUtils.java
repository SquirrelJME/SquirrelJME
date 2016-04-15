// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This utility class helps reading attributes which exist within a class.
 *
 * @since 2016/04/03
 */
public final class CFAttributeUtils
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/04/03
	 */
	private CFAttributeUtils()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Reads the name of an attribute.
	 *
	 * @param __pool The constant pool class.
	 * @param __das The data source.
	 * @return The current attribute to be read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	public static String readName(CFConstantPool __pool, DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__pool == null || __das == null)
			throw new NullPointerException("NARG");
		
		// Read it in
		return __pool.<CFUTF8>getAs(
			__das.readUnsignedShort(), CFUTF8.class).toString();
	}
}


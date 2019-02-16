// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.scrf.classfile.ClassFileProcessor;

/**
 * This represents a SummerCoat class file.
 *
 * @since 2019/02/16
 */
public final class SummerClass
{
	/**
	 * Loads a SCRF class from the given normal Java class file.
	 *
	 * @param __cl The input class to convert.
	 * @return The resulting SummerCoat class.
	 * @throws NullPointerException On null arguments.
	 * @throws SummerFormatException If the input class is not correct or
	 * conversion was not possible.
	 * @since 2019/02/16
	 */
	public static final SummerClass ofClassFile(ClassFile __cl)
		throws NullPointerException, SummerFormatException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Conversion could fail with a bad class format
		try
		{
			return new ClassFileProcessor(__cl).process();
		}
		
		// {@squirreljme.error AV02 Could not load standard Java class file
		// as a SummerCoat class due to a format error.}
		catch (InvalidClassFormatException e)
		{
			throw new SummerFormatException("AV02", e);
		}
	}
}


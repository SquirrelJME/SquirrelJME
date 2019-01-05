// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.ClassFile;

/**
 * This is a processor which translates standard Java class files with their
 * structure and byte code to the SummerCoat Register Format.
 *
 * @since 2019/01/05
 */
public final class ClassProcessor
{
	/**
	 * Initializes the class processor.
	 *
	 * @param __cf The class file to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public ClassProcessor(ClassFile __cf)
		throws NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}


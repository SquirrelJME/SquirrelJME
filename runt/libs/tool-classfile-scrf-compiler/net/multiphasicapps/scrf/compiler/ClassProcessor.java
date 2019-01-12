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
import net.multiphasicapps.scrf.RegisterClass;

/**
 * This is a processor which translates standard Java class files with their
 * structure and byte code to the SummerCoat Register Format.
 *
 * @since 2019/01/05
 */
public final class ClassProcessor
{
	/** The input class to process. */
	protected final ClassFile input;
	
	/**
	 * Initializes the class processor.
	 *
	 * @param __cf The class file to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	private ClassProcessor(ClassFile __cf)
		throws NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		this.input = __cf;
	}
	
	/**
	 * Processes the input class.
	 *
	 * @return The resulting register class.
	 * @throws ClassProcessException If the class could not be processed.
	 * @since 2019/01/11
	 */
	public final RegisterClass process()
		throws ClassProcessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Processes the specified class file and compiles it to the register class
	 * format.
	 *
	 * @param __cf The class file to convert.
	 * @return The register class.
	 * @throws ClassProcessException If the class could not be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/11
	 */
	public static final RegisterClass process(ClassFile __cf)
		throws ClassProcessException, NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		return new ClassProcessor(__cf).process();
	}
}


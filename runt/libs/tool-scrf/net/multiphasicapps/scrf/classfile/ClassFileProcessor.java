// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.classfile;

import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.scrf.ILCode;
import net.multiphasicapps.scrf.SummerClass;
import net.multiphasicapps.scrf.SummerFormatException;

/**
 * This is used to actually process the class file to the SummerCoat class
 * format.
 *
 * @since 2019/02/16
 */
public final class ClassFileProcessor
{
	/** The classfile to process. */
	protected final ClassFile input;
	
	/**
	 * Initializes the processor.
	 *
	 * @param __cl The class to convert.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public ClassFileProcessor(ClassFile __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.input = __cl;
	}
	
	/**
	 * Performs the processing.
	 *
	 * @return The resulting SummerCoat class.
	 * @throws SummerFormatException If the class could not be converted.
	 * @since 2019/02/16
	 */
	public final SummerClass process()
		throws SummerFormatException
	{
		// Process each method
		for (Method m : input.methods())
		{
			// Intermediate code for this method
			ILCode ilc = null;
			
			// Process byte code in the method
			MethodFlags mf = m.flags();
			if (!mf.isNative() && !mf.isAbstract())
				ilc = new MethodProcessor(this, m).process();
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}


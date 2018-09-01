// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassFile;

/**
 * This is a representation of a class file as it is seen by the virtual
 * machine, it is intended to remain simple and only refer to what is needed
 * for the machine to run.
 *
 * @since 2018/07/21
 */
public final class SpringClass
{
	/** The name of this class. */
	protected final ClassName name;
	
	/** The class file data. */
	protected final ClassFile file;
	
	/**
	 * Initializes the spring class.
	 *
	 * @param __tn The name of this class.
	 * @param __cf The class file for this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/21
	 */
	private SpringClass(ClassName __tn, ClassFile __cf)
		throws NullPointerException
	{
		if (__tn == null || __cf == null)
			throw new NullPointerException("NARG");
		
		this.name = __tn;
		this.file = __cf;
	}
	
	/**
	 * Parses the input stream for class information.
	 *
	 * @param __in The class to parse.
	 * @return A loaded class.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringClassFormatException If the class is not formatted
	 * properly.
	 * @since 2018/07/21
	 */
	public static SpringClass parse(ClassFile __in)
		throws NullPointerException, SpringClassFormatException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("parse(%s)%n", __in.thisName());
		
		return new SpringClass(__in.thisName(), __in);
	}
}


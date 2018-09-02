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
	/** The class file data. */
	protected final ClassFile file;
	
	/** The super class. */
	protected final SpringClass superclass;
	
	/** Interface classes. */
	private final SpringClass[] _interfaceclasses;
	
	/**
	 * Initializes the spring class.
	 *
	 * @param __super The super class of this class.
	 * @param __interfaces The the interfaces this class implements.
	 * @param __cf The class file for this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/21
	 */
	SpringClass(SpringClass __super, SpringClass[] __interfaces,
		ClassFile __cf)
		throws NullPointerException
	{
		if (__interfaces == null || __cf == null)
			throw new NullPointerException("NARG");
		
		this.file = __cf;
		this.superclass = __super;
		
		// Check
		this._interfaceclasses = (__interfaces = __interfaces.clone());
		for (SpringClass x : __interfaces)
			if (x == null)
				throw new NullPointerException("NARG");
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler;

import cc.squirreljme.jit.objectfile.SymbolName;

/**
 * This represents the name and reference to the structure within the class
 * information.
 *
 * @since 2018/02/26
 */
public final class ClassStructureSymbolName
	extends SymbolName
{
	/** The name of the class. */
	protected final String classname;
	
	/** The structure to access. */
	protected final ClassStructure struct;
	
	/**
	 * Initializes the class structure symbol name.
	 *
	 * @param __name The name of the class.
	 * @param __struct The structure field to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/26
	 */
	public ClassStructureSymbolName(String __name, ClassStructure __struct)
		throws NullPointerException
	{
		if (__name == null || __struct == null)
			throw new NullPointerException("NARG");
		
		this.classname = __name;
		this.struct = __struct;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/26
	 */
	@Override
	public final String internalToString()
	{
		return "class@" + this.classname + "@" + this.struct;
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.struct;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import net.multiphasicapps.classfile.ClassFlag;
import net.multiphasicapps.classfile.ClassFlags;

/**
 * This class represents a set of defined class flags.
 *
 * @since 2018/03/10
 */
public final class DefinedClassFlags
{
	/**
	 * Defines the set of flags to use for a given class.
	 *
	 * @param __f The flags to use for the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/10
	 */
	public DefinedClassFlags(DefinedClassFlag... __f)
		throws NullPointerException
	{
		this(Arrays.<DefinedClassFlag>asList(__f));
	}
	
	/**
	 * Defines the set of flags to use for a given class.
	 *
	 * @param __f The flags to use for the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/10
	 */
	public DefinedClassFlags(Iterable<DefinedClassFlag> __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the class flags which would be used if this were compiled.
	 *
	 * @return The flags to use for compilation.
	 * @since 2018/03/10
	 */
	public final ClassFlags toClassFlags()
	{
		throw new todo.TODO();
	}
}


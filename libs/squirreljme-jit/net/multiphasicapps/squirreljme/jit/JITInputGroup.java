// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.Collection;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.rc.Resource;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This represents a group of resources and classes together as a single
 * unit.
 *
 * @since 2017/09/27
 */
public final class JITInputGroup
{
	/**
	 * This initializes an input group from the given collections.
	 *
	 * @param __n The name of the group.
	 * @param __trc The resources in the group.
	 * @param __tcl The classes in the group.
	 * @throws JITException If there are duplicate resources or classes.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	JITInputGroup(String __n, Collection<Resource> __trc,
		Collection<ClassFile> __tcl)
		throws JITException, NullPointerException
	{
		if (__n == null || __trc == null || __tcl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}


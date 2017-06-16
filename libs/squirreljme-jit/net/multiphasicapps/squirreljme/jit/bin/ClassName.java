// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the name of a class which exists within the virtual machine.
 * It is always normalized to the form which is used in fields, that is the
 * string used is always a field descriptor so that native types an arrays can
 * be represented in a uniform fashion.
 *
 * This class is immutable.
 *
 * @since 2017/06/15
 */
public final class ClassName
	implements Comparable<ClassName>
{
	/** The package this class is in. */
	private volatile Reference<PackageName> _package;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/15
	 */
	@Override
	public int compareTo(ClassName __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the package that this class resides within.
	 *
	 * If this class name represents a primitive type then the special package
	 * is returned.
	 *
	 * @return The package that this class is inside.
	 * @since 2017/06/15
	 */
	public PackageName packageName()
	{
		throw new todo.TODO();
	}
}


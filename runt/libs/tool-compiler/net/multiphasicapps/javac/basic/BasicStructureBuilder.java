// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import net.multiphasicapps.classfile.BinaryName;

/**
 * This class is used to build the basic structure of a class file along with
 * any of its classes.
 *
 * @since 2018/03/12
 */
public final class BasicStructureBuilder
{
	/** The package this class is in. */
	private volatile BinaryName _package;
	
	/**
	 * Builds the structure.
	 *
	 * @return The resulting structure.
	 * @throws BasicStructureException If the structure is not valid.
	 * @since 2018/03/13
	 */
	public final BasicStructure build()
		throws BasicStructureException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the package that this class is in.
	 *
	 * @param __bn The package the class is in.
	 * @since 2018/03/13
	 */
	public final void setPackage(BinaryName __bn)
	{
		this._package = __bn;
	}
}


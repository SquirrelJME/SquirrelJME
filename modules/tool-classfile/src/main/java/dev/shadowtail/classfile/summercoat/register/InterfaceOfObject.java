// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.summercoat.register;

/**
 * Represents an interface of a given object, for the JVM implementation these
 * represent a joined _Interface_ and _Object's Class_ as there needs to be
 * a point of reference to these interfaces.
 *
 * @since 2020/11/24
 */
public final class InterfaceOfObject
	extends Register
{
	/**
	 * Initializes the basic register.
	 *
	 * @param __register The register to get.
	 * @since 2020/11/24
	 */
	public InterfaceOfObject(int __register)
	{
		super(__register);
	}
}

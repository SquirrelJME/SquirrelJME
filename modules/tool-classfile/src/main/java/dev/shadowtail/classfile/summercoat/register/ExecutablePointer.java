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
 * Represents a pointer that can be invoked for a method call.
 *
 * @since 2020/11/24
 */
public final class ExecutablePointer
	extends Register
{
	/**
	 * Initializes the basic register.
	 *
	 * @param __register The register to get.
	 * @since 2020/11/24
	 */
	public ExecutablePointer(int __register)
	{
		super(__register);
	}
}

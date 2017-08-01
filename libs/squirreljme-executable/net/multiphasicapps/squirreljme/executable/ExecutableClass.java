// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.executable;

/**
 * This represents a class which has been loaded into memory. The class here
 * could be internal to SquirrelJME's binary and always exists in compiled
 * form. If not, then it could be the result of a JIT compilation as needed.
 * Since it is system and target specific this must be handled by the kernel
 * implementation because there cannot be a standard common way to have a base
 * class managed the same when systems can be completely different. Note that
 * this representation of a class can be shared among many processes.
 *
 * @since 2016/12/14
 */
public interface ExecutableClass
{
	/**
	 * Return the binary name of the class.
	 *
	 * @return The class binary name.
	 * @since 2017/01/16
	 */
	public abstract String name();
}


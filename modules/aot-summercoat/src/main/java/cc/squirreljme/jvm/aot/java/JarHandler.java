// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.java;

import net.multiphasicapps.classfile.ClassName;

/**
 * Handler which handles the processing of Jars.
 *
 * @since 2022/08/04
 */
public interface JarHandler
{
	/**
	 * Starts handling and processing of the given class with the given name.
	 * 
	 * @param __name The name of the class.
	 * @return The handler for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/04
	 */
	ClassHandler newClass(ClassName __name)
		throws NullPointerException;
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.util.ServiceLoader;

/**
 * This is the base class for the backend interface what is available for
 * compilers and otherwise.
 * 
 * Accessed through {@link ServiceLoader}.
 *
 * @since 2020/11/22
 */
public interface Backend
{
	/**
	 * Returns the name of the backend.
	 * 
	 * @return The name of the backend.
	 * @since 2020/11/22
	 */
	String name();
}

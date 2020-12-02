// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.io.IOException;
import java.io.InputStream;

/**
 * Base interface which describes a glob of linked binary.
 *
 * @since 2020/11/22
 */
public interface LinkGlob
{
	/**
	 * Indicates that compilation is complete and the final binary should
	 * be output.
	 * 
	 * @throws IOException On read/write errors.
	 * @since 2020/11/22
	 */
	void finish()
		throws IOException;
		
	/**
	 * Joins this into the linking structure.
	 * 
	 * @param __name The name of the object to link.
	 * @param __isRc Is this a resource and not an executable?
	 * @param __data The data to link in.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/22
	 */
	void join(String __name, boolean __isRc, InputStream __data)
		throws IOException, NullPointerException;
}

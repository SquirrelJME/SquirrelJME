// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base interface which describes a glob of linked binary.
 *
 * @since 2020/11/22
 */
public interface LinkGlob
	extends Closeable
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
	 * Indicates that the compilation step is soon to start.
	 * 
	 * @throws IOException On read/write errors.
	 * @since 2023/05/28
	 */
	void initialize()
		throws IOException;
}

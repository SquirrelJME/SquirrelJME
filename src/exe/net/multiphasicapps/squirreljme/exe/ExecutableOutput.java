// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITConfig;

/**
 * This interface describes a means for which an executable that contains
 * native code, class definitions, and resources is written. The output class
 * handles linkage, the layout of the resulting binary, and other details.
 *
 * It is up to the implementation of the output how caching of the resulting
 * binary is to be handled, it may store the data in memory or use a temporary
 * file on the disk.
 *
 * @since 2016/09/28
 */
public interface ExecutableOutput
{
	/**
	 * Returns the configuration currently being used on this output.
	 *
	 * @return The output configuration.
	 * @since 2016/09/28
	 */
	public abstract JITConfig config();
	
	/**
	 * Writes the resultant binary to the output stream.
	 *
	 * This method should cause linking to occur if needed.
	 *
	 * This method must be able to be called multiple times, if there are no
	 * changes then it should output the same exact binary.
	 *
	 * @param __os The stream to write binary data to.
	 * @throws IOException On read/write errors.
	 * @since 2016/09/28
	 */
	public abstract void writeOutput(OutputStream __os)
		throws IOException;
}


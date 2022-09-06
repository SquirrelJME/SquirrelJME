// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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
	 * Returns the compilation settings used for the glob.
	 * 
	 * @return The compilation settings.
	 * @since 2022/09/05
	 */
	CompileSettings compileSettings();
	
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
	 * Joins this resource into the linking structure.
	 * 
	 * @param __name The name of the resource to link.
	 * @param __data The data stream of the resource to link in.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/22
	 */
	void joinResource(String __name, InputStream __data)
		throws IOException, NullPointerException;
	
	/**
	 * Counts compilation statistics.
	 * 
	 * @return The statistics counter.
	 * @since 2022/09/06
	 */
	CompilationStatistics statistics();
}

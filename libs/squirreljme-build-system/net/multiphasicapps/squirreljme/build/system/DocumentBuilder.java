// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;

/**
 * This is used to produce the documentation which documents SquirrelJME.
 *
 * @since 2017/09/04
 */
public class DocumentBuilder
{
	/** The output path for documents. */
	protected final Path output;
	
	/**
	 * Initializes the document builder.
	 *
	 * @param __p The output path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	public DocumentBuilder(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __p;
	}
	
	/**
	 * Runs the generator.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2017/09/04 
	 */
	public void run()
		throws IOException
	{
		throw new todo.TODO();
	}
}


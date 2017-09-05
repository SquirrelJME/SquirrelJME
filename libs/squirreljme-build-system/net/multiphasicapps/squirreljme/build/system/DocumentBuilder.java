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

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.multiphasicapps.javac.token.Tokenizer;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
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
	
	/** The manager which is used for projects. */
	protected final ProjectManager projects;
	
	/**
	 * Initializes the document builder.
	 *
	 * @param __pm The manager for projects.
	 * @param __p The output path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	public DocumentBuilder(ProjectManager __pm, Path __p)
		throws NullPointerException
	{
		// Check
		if (__pm == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projects = __pm;
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
		for (Project p : this.projects)
			try (FileDirectory fd = p.source().openFileDirectory())
			{
				for (String fn : fd)
				{
					if (!fn.endsWith(".java"))
						continue;
					
					try (InputStream is = fd.open(fn))
					{
						System.err.println(new Tokenizer(is, true).run());
					}
				}
			}
		
		throw new todo.TODO();
	}
}


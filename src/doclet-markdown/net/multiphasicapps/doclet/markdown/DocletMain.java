// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.doclet.markdown;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * This is the main entry point for the markdown code generators.
 *
 * @since 2016/09/12
 */
public class DocletMain
{
	/**
	 * {@squirreljme.property net.multiphasicapps.doclet.markdown.outdir=(dir)
	 * The output directory where generated documentation files go.}
	 */
	public static final String OUTPUT_DIRECTORY_PROPERTY =
		"net.multiphasicapps.doclet.markdown.outdir";
	
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.doclet.markdown.projectdir=(dir)
	 * This is the directory which contains project directories used to
	 * determine cross-linking between projects.}
	 */
	public static final String PROJECT_DIRECTORY_PROPERTY =
		"net.multiphasicapps.doclet.markdown.projectdir";
	
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.doclet.markdown.depends=(projects)
	 * This is a colon separated list of projects which the current project
	 * being documented depends on. If a reference to a class that does not
	 * exist in the source tree is detected then there will be a cross-project
	 * link to another project.}
	 */
	public static final String DEPENDS_PROPERTY =
		"net.multiphasicapps.doclet.markdown.depends";
	
	/**
	 * Starts processing the root document to generate output markdown
	 * documentation.
	 *
	 * @param __rd The root document.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/12
	 */
	public static boolean start(RootDoc __rd)
		throws NullPointerException
	{
		// Check
		if (__rd == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- %s%n",
			System.getProperty(OUTPUT_DIRECTORY_PROPERTY));
		System.err.printf("DEBUG -- %s%n",
			System.getProperty(PROJECT_DIRECTORY_PROPERTY));
		System.err.printf("DEBUG -- %s%n",
			System.getProperty(DEPENDS_PROPERTY));
		
		// Debug print classes
		for (ClassDoc cd : __rd.classes())
			System.err.printf("DEBUG -- Class %s%n", cd);
		
		// Processed
		return true;
	}
}


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
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import net.multiphasicapps.markdownwriter.MarkdownWriter;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the main entry point for the markdown code generators.
 *
 * @since 2016/09/12
 */
public class DocletMain
	implements Runnable
{
	/** The output directory where generated documentation files go. */
	public static final String OUTPUT_DIRECTORY_OPTION =
		"-d";
	
	/**
	 * This is the directory which contains project directories used to
	 * determine cross-linking between projects.
	 */
	public static final String PROJECTS_DIRECTORY_OPTION =
		"-squirreljme-projectsdir";
	
	/**
	 * This is a colon separated list of projects which the current project
	 * being documented depends on. If a reference to a class that does not
	 * exist in the source tree is detected then there will be a cross-project
	 * link to another project.
	 */
	public static final String DEPENDS_OPTION =
		"-squirreljme-depends";
	
	/** The root document. */
	protected final RootDoc root;
	
	/** The class table. */
	protected final Map<String, MarkdownClass> classes =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the main doclet.
	 *
	 * @param __rd The root document.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public DocletMain(RootDoc __rd)
		throws NullPointerException
	{
		// Check
		if (__rd == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.root = __rd;
	}
	
	/** 
	 * Returns the markdown class for the given class documentation.
	 *
	 * @param __cd The class documentation.
	 * @return The markdown class for it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public MarkdownClass markdownClass(ClassDoc __cd)
		throws NullPointerException
	{
		// Check
		if (__cd == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.markdownClass(__cd.qualifiedName());
	}
	
	/**
	 * Returns the markdown class for the given class name.
	 *
	 * @param __n The qualified class name.
	 * @return The markdown class for it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public MarkdownClass markdownClass(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// See if the class is already in the class map
		Map<String, MarkdownClass> classes = this.classes;
		MarkdownClass rv = classes.get(__n);
		if (rv != null)
			return rv;
		
		// {@squirreljme.error CF02 No class using the specified name has been
		// documented. (The class name)}
		ClassDoc cd = this.root.classNamed(__n);
		if (cd == null)
			throw new RuntimeException(String.format("CF02 %s", __n));
		
		// Load documentation for it and return it
		// it will be placed in the class map when the constructor is called.
		rv = new MarkdownClass(this, cd);
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void run()
	{
		// Build class information to be used for later output
		// Although it is somewhat duplicated, this is used so that everything
		// matches that of SquirrelJME's expected documentation output. Since
		// markdown has no ref type links, everything must be sorted in
		// alphabetical order.
		// These classes are marked implicit, otherwise the generation pass
		// will end up outputting documents for unspecified classes.
		RootDoc root = this.root;
		for (ClassDoc cd : root.classes())
			markdownClass(cd)._implicit = true;
		
		// Write all classes
		for (MarkdownClass mc : this.classes.values())
			if (mc._implicit)
				mc.writeOutput();
	}
	
	/**
	 * The constructor of {@link MarkdownClass} calls this method so that
	 * classes may eventually and recursively mention themselves when the
	 * layout used for documentation is specified.
	 *
	 * @param __n The qualified name.
	 * @parma __mc The markdown class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	void __registerClass(String __n, MarkdownClass __mc)
		throws NullPointerException
	{
		// Check
		if (__n == null || __mc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CF01 A class has been duplicated multiple
		// times.}
		Map<String, MarkdownClass> classes = this.classes;
		if (null != classes.put(__n, __mc))
			throw new RuntimeException("CF01");
	}
	
	/**
	 * Returns the langauge version used.
	 *
	 * @return The supported version in the doclet.
	 * @since 2016/09/13
	 */
	public static LanguageVersion languageVersion()
	{
		return LanguageVersion.JAVA_1_5;
	}
	
	/**
	 * Returns the number of options that a command takes,
	 *
	 * @param __s The command.
	 * @return The number of options used.
	 * @since 2016/09/13
	 */
	public static int optionLength(String __s)
	{
		// Depends
		switch (__s)
		{
				// These take two
			case OUTPUT_DIRECTORY_OPTION:
			case PROJECTS_DIRECTORY_OPTION:
			case DEPENDS_OPTION:
				return 2;
			
				// Unknown
			default:
				return 0;
		}
	}
	
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
		
		// Use instance since it is much saner
		DocletMain dm = new DocletMain(__rd);
		dm.run();
		
		// Processed
		return true;
	}
}


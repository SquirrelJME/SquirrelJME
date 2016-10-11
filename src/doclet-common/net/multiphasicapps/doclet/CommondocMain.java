// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (CommondocClass) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (CommondocClass) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.doclet.CommondocMain;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassLoaderNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This is the base class for common doclets.
 *
 * @since 2016/10/11
 */
public abstract class CommondocMain
	implements Runnable
{
	/**
	 * This is a colon separated list of projects which the current project
	 * being documented depends on. If a reference to a class that does not
	 * exist in the source tree is detected then there will be a cross-project
	 * link to another project.
	 */
	public static final String DEPENDS_OPTION =
		"-squirreljme-depends";
	
	/** The output directory where generated documentation files go. */
	public static final String OUTPUT_DIRECTORY_OPTION =
		"-d";
	
	/** The current project being JavaDoced. */
	public static final String PROJECT_OPTION =
		"-squirreljme-project";
	
	/**
	 * This is the directory which contains project directories used to
	 * determine cross-linking between projects.
	 */
	public static final String PROJECTS_DIRECTORY_OPTION =
		"-squirreljme-projectsdir";
	
	/** The root document. */
	protected final RootDoc root;
	
	/** The class table. */
	protected final Map<String, CommondocClass> classes =
		new SortedTreeMap<>();
	
	/** Class to project mappings. */
	protected final Map<CommondocClass, String> classtoproject =
		new HashMap<>();
	
	/** The name of this project. */
	protected final String project;
	
	/** The output created document base directory. */
	protected final Path outdir;
	
	/** The directory where projects are if detection is desired. */
	protected final Path projectsdir;
	
	/** Dependencies of this project. */
	protected final Set<String> depends;
	
	/**
	 * Initializes the main doclet.
	 *
	 * @param __rd The root document.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public CommondocMain(RootDoc __rd)
		throws NullPointerException
	{
		// Check
		if (__rd == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.root = __rd;
		
		// Some properties
		String project = null;
		Set<String> depends = new HashSet<>();
		
		// Some directories
		Path projectsdir = null;
		Path outdir = Paths.get(System.getProperty("user.dir"));
		
		// Handle options
		for (String[] option : __rd.options())
			switch (option[0])
			{
					// Dependencies of the current package
				case DEPENDS_OPTION:
					__decodeProjects(depends, option[1]);
					break;
				
					// The project being documented
				case PROJECT_OPTION:
					project = option[1];
					break;
					
					// Projects directory root
				case PROJECTS_DIRECTORY_OPTION:
					projectsdir = Paths.get(option[1]);
					break;
				
					// Output directory 
				case OUTPUT_DIRECTORY_OPTION:
					outdir = Paths.get(option[1]);
					break;
				
					// Unknown, ignore
				default:
					break;
			}
		
		// Set
		this.project = project;
		this.outdir = outdir;
		this.projectsdir = projectsdir;
		this.depends = UnmodifiableSet.<String>of(depends);
	}
	
	/**
	 * Creates a wrapped class document.
	 *
	 * @param __cd The class document to wrap.
	 * @return The wrapped class document.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/11
	 */
	protected abstract CommondocClass createWrappedClass(ClassDoc __cd)
		throws NullPointerException;
	
	/**
	 * Generates documentation as required.
	 *
	 * @since 2016/10/11
	 */
	protected abstract void generateDocumentation();
	
	/**
	 * Determines the path where to output based on the input path.
	 *
	 * @param __p The path to output at.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	public Path outputPath(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Resolve
		return this.outdir.resolve(__p);
	}
	
	/**
	 * Returns the project which contains the specified class.
	 *
	 * @param __cl The class to get the project for.
	 * @return The project the given class is under.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	public String projectOfClass(CommondocClass __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Get
		Map<CommondocClass, String> classtoproject = this.classtoproject;
		
		// If already cached, returned the cached value
		String rv = classtoproject.get(__cl);
		if (rv != null)
			return rv;
		
		// Need project directory to mass scan directories
		Path pd = this.projectsdir;
		if (pd == null)
			return Objects.toString(this.project, "unknown");
		
		// The class to find
		Path want = __cl.containingClassFile();
		
		// Go through everything
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(pd))
		{
			for (Path pp : ds)
			{
				// Ignore non-files
				if (!Files.isDirectory(pp))
					continue;
				
				// Loop since some 
				Path couldbe = pp.resolve(want);
				
				// See if it exists
				Path maybe = pp.resolve(couldbe);
				if (Files.exists(maybe) && !Files.isDirectory(maybe))
				{
					// Setup
					rv = pp.getFileName().toString();
				
					// Cache
					classtoproject.put(__cl, rv);
				
					// Use it
					return rv;
				}
			}
		}
		
		// {@squirreljme.error CF04 Could not list directory contents.}
		catch (IOException e)
		{
			throw new RuntimeException("CF04", e);
		}
		
		// Unknown
		return "unknown";
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
			wrapClass(cd)._implicit = true;
		
		// Generate documentation
		generateDocumentation();
	}
	
	/** 
	 * Returns the wrapped class for the given class documentation.
	 *
	 * @param __cd The class documentation.
	 * @return The wrapped class for it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public CommondocClass wrapClass(ClassDoc __cd)
		throws NullPointerException
	{
		// Check
		if (__cd == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.wrapClass(__cd.qualifiedName());
	}
	
	/**
	 * Returns the wrapped class for the given class name.
	 *
	 * @param __n The qualified class name.
	 * @return The wrapped class for it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public CommondocClass wrapClass(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// See if the class is already in the class map
		Map<String, CommondocClass> classes = this.classes;
		CommondocClass rv = classes.get(__n);
		if (rv != null)
			return rv;
		
		// {@squirreljme.error CF02 No class using the specified name has been
		// documented. (The class name)}
		ClassDoc cd = this.root.classNamed(__n);
		if (cd == null)
			throw new RuntimeException(String.format("CF02 %s", __n));
		
		// Load documentation for it and return it
		// it will be placed in the class map when the constructor is called.
		rv = createWrappedClass(cd);
		return rv;
	}
	
	/**
	 * The constructor of {@link CommondocClass} calls this method so that
	 * classes may eventually and recursively mention themselves when the
	 * layout used for documentation is specified.
	 *
	 * @param __n The qualified name.
	 * @parma __mc The documented class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	void __registerClass(String __n, CommondocClass __mc)
		throws NullPointerException
	{
		// Check
		if (__n == null || __mc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CF01 A class has been duplicated multiple
		// times.}
		Map<String, CommondocClass> classes = this.classes;
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
			case DEPENDS_OPTION:
			case OUTPUT_DIRECTORY_OPTION:
			case PROJECT_OPTION:
			case PROJECTS_DIRECTORY_OPTION:
				return 2;
			
				// Unknown
			default:
				return 0;
		}
	}
	
	/**
	 * Decodes projects separated by colon and adds them to the specified set.
	 *
	 * @param __deps The set to add dependencies to.
	 * @param __in The input string to split.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	private static void __decodeProjects(Set<String> __deps, String __in)
		throws NullPointerException
	{
		// Check
		if (__deps == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Split
		int n = __in.length();
		for (int i = 0, last = 0; i <= n; i++)
		{
			// Force last to be colon
			char c = (i == n ? ':' : __in.charAt(i));
			
			// If a colon, split
			if (c == ':')
			{
				// Add
				__deps.add(__in.substring(last, i));
				
				// Start after
				last = i + 1;
			}
		}
	}
}


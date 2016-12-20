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
import net.multiphasicapps.doclet.CommondocClass;
import net.multiphasicapps.doclet.CommondocMain;
import net.multiphasicapps.markdownwriter.MarkdownWriter;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassLoaderNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This is the main entry point for the markdown code generators.
 *
 * @since 2016/09/12
 */
public class MarkdocMain
	extends CommondocMain
{
	/**
	 * Initializes the markdown document writer.
	 *
	 * @param __rd The root document.
	 * @since 2016/10/11
	 */
	public MarkdocMain(RootDoc __rd)
	{
		super(__rd);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	protected CommondocClass createWrappedClass(ClassDoc __cd)
		throws NullPointerException
	{
		// Check
		if (__cd == null)
			throw new NullPointerException("NARG");
		
		// Wrap
		return new MarkdocClass(this, __cd);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	protected void generateDocumentation()
	{
		// The current project
		String project = this.project;
		Path outdir = this.outdir;
		
		// Need to create directories to place the class table
		try
		{
			Files.createDirectories(outputPath(Paths.get("foo")).getParent());
			
			// Add to projects file
			__addToFile(outdir.getParent().resolve(".projects"), project);
		}
		
		// {@squirreljme.error CF07 Failed to create output directories.}
		catch (IOException e)
		{
			throw new RuntimeException("CF07", e);
		}
		
		// Write project index and such also
		Path outpath = outputPath(Paths.get("classes.mkd"));
		try (MarkdownWriter mdw = new MarkdownWriter(new OutputStreamWriter(
			Channels.newOutputStream(FileChannel.open(outpath,
			StandardOpenOption.WRITE, StandardOpenOption.CREATE)), "utf-8")))
		{
			// Project Title
			mdw.header(true, 1, project);
			
			// Start list
			mdw.listStart();
			boolean start = false;
			
			// Write all classes
			Path classesoutdir = outdir.getParent().resolve(".classes");
			for (CommondocClass cmc : this.classes.values())
				if (cmc.isImplicit())
				{
					// Cast
					MarkdocClass mc = (MarkdocClass)cmc;
					
					// Write class markdown
					mc.writeOutput();
					
					// Go to the next item on the list
					if (start)
						mdw.listNext();
					start = true;
					
					// Link to it
					String qn = mc.qualifiedName();
					String mdp = pathToString(mc.markdownPath());
					mdw.uri(mdp, qn);
					
					// Add to the global class list
					__addToFile(classesoutdir, qn + " " + project + "/" + mdp);
				}
			
			// End list
			mdw.listEnd();
			
			// Flush
			mdw.flush();
		}
		
		// Failed to write, delete the produced file
		catch (RuntimeException|Error|IOException e)
		{
			// Delete output markdown
			try
			{
				Files.delete(outpath);
			}
			
			// Ignore
			catch (IOException f)
			{
			}
			
			// Rethrow exception
			if (e instanceof RuntimeException)
				throw (RuntimeException)e;
			else if (e instanceof Error)
				throw (Error)e;
			
			// {@squirreljme.error CF06 Failed to write the class table.}
			else
				throw new RuntimeException("CF06", e);
		}
	}
	
	/**
	 * Returns a relative URI from one class that may exist in one project to
	 * another class which may be in another project.
	 *
	 * @param __from The source class.
	 * @param __to The target class.
	 * @return The relative URI from the source class to the destination.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	public String uriToClassMarkdown(MarkdocClass __from, MarkdocClass __to)
		throws NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Get projects for both classes
		String ja = projectOfClass(__from),
			jb = projectOfClass(__to);
		
		// Get file paths for both
		Path pa = __from.markdownPath(),
			pb = __to.markdownPath();
		
		// Same project?
		if (ja.equals(jb))
		{
			// Get both lists and their lengths, minus one (since only the
			// package is cared about)
			List<IdentifierSymbol> la = __from.binaryName().asList(),
				lb = __to.binaryName().asList();
			int na = la.size() - 1, nb = lb.size() - 1;
			
			// Determine the number of elements that are in common
			int mm = Math.min(na, nb);
			int common ;
			for (common = 0; common < mm; common++)
				if (!la.get(common).equals(lb.get(common)))
					break;
			
			// Setup
			StringBuilder sb = new StringBuilder();
			
			// For every fragment left in the first, dot dot
			for (int i = common; i < na; i++)
			{
				if (sb.length() > 0)
					sb.append("/");
				sb.append("..");
			}
			
			// For every fragment in the second, add directories
			for (int i = common; i < nb; i++)
			{
				if (sb.length() > 0)
					sb.append("/");
				sb.append(MarkdocClass.__lowerString(lb.get(i).toString()));
			}
			
			// And the markdown filename
			if (sb.length() > 0)
				sb.append('/');
			sb.append(__to.markdownPath().getFileName().toString());
			
			// Build
			return sb.toString();
		}
		
		// Other ones, completely .. out and move in
		else
		{
			StringBuilder sb = new StringBuilder();
			List<IdentifierSymbol> idl = __from.binaryName().asList();
			
			// Dot dot it all, but ignore the class name
			boolean skipped = false;
			for (IdentifierSymbol i : idl)
			{
				// Ignore the first element
				if (!skipped)
				{
					skipped = true;
					continue;
				}
				
				// Add downpath
				if (sb.length() > 0)
					sb.append('/');
				sb.append("..");
			}
			
			// Add project
			sb.append("/../");
			sb.append(jb);
			
			// Append the second form
			for (Path p : pb)
			{
				sb.append('/');
				sb.append(p.toString());
			}
			
			// Build
			return sb.toString();
		}
	}
	
	/**
	 * Returns the langauge version used.
	 *
	 * @return The supported version in the doclet.
	 * @since 2016/09/13
	 */
	public static LanguageVersion languageVersion()
	{
		return CommondocMain.languageVersion();
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
		return CommondocMain.optionLength(__s);
	}
	
	/**
	 * Converts a path to a string using a neutral algorithm that is compatible
	 * with URIs used by Markdown.
	 *
	 * @param __p The path to use.
	 * @return The path converted to a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/02
	 */
	public static String pathToString(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Convert
		StringBuilder sb = new StringBuilder();
		for (Path p : __p)
		{
			if (sb.length() > 0)
				sb.append("/");
			sb.append(p.toString());
		}
		
		// Return it
		return sb.toString();
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
		MarkdocMain dm = new MarkdocMain(__rd);
		dm.run();
		
		// Processed
		return true;
	}
	
	/**
	 * Adds a single file to the specified file and if required adds the given
	 * string to the output file.
	 *
	 * @param __p The file to add to.
	 * @param __str The string to add.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/02
	 */
	private static void __addToFile(Path __p, String __str)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null || __str == null)
			throw new NullPointerException("NARG");
		
		// Read in lines
		Set<String> lines = new SortedTreeSet<>();
		if (Files.exists(__p))
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				Channels.newInputStream(FileChannel.open(__p,
				StandardOpenOption.READ)), "utf-8")))
			{
				String s;
				while (null != (s = br.readLine()))
					lines.add(s);
			}
		
		// Add our string
		lines.add(__str);
		
		// Write lines
		try (PrintStream ps = new PrintStream(Channels.newOutputStream(
			FileChannel.open(__p, StandardOpenOption.WRITE,
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)),
			true, "utf-8"))
		{
			for (String s : lines)
				ps.println(s);
		}
	}
}


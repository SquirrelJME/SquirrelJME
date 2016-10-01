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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.markdownwriter.MarkdownWriter;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This loads the class doclet documentation and obtains information so that
 * it may be neatly output.
 *
 * @since 2016/09/13
 */
public class MarkdownClass
{
	/** The main doclet. */
	protected final DocletMain main;
	
	/** The wrapped class doclet. */
	protected final ClassDoc doc;
	
	/** The base class name path, uses directory components. */
	protected final Path basenamepath;
	
	/** The base path for the markdown file. */
	protected final Path basemarkdownpath;
	
	/** The base path with Java extension. */
	protected final Path basenamepathjava;
	
	/** The qualified name of this class. */
	protected final String qualifiedname;
	
	/** The unqualified class name. */
	protected final String unqualifiedname;
	
	/** The class name used. */
	protected final BinaryNameSymbol namesymbol;
	
	/** The containing class (will be null if not an inner class). */
	protected final MarkdownClass containedin;
	
	/** The super class. */
	protected final MarkdownClass superclass;
	
	/** Implemented interfaces. */
	protected final Map<String, MarkdownClass> interfaces =
		new SortedTreeMap<>();
	
	/** Classes that extend this class. */
	protected final Map<String, MarkdownClass> superclassof =
		new SortedTreeMap<>();
	
	/** Classes which implement this class. */
	protected final Map<String, MarkdownClass> interfacesof =
		new SortedTreeMap<>();
	
	/** Is this class explicit? */
	volatile boolean _implicit;
	
	/**
	 * Initializes the markdown wrapped class.
	 *
	 * @param __dm The main doclet.
	 * @param __cd The class to reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public MarkdownClass(DocletMain __dm, ClassDoc __cd)
		throws NullPointerException
	{
		// Check
		if (__dm == null || __cd == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.main = __dm;
		this.doc = __cd;
		
		// Setup qualified name
		String qualifiedname = __cd.qualifiedName();
		this.qualifiedname = qualifiedname;
		
		// Determine unqualified name, which is just from the last dot
		int ld = qualifiedname.lastIndexOf('.');
		this.unqualifiedname = (ld < 0 ? qualifiedname :
			qualifiedname.substring(ld + 1));
		
		// Register class
		__dm.__registerClass(__cd.qualifiedName(), this);
		
		// Get contained class
		ClassDoc cin = __cd.containingClass();
		MarkdownClass incl = (cin == null ? null : __dm.markdownClass(cin));
		this.containedin = incl;
		
		// The symbol for this name is just this class
		BinaryNameSymbol bns;
		if (incl == null)
			this.namesymbol = (bns = BinaryNameSymbol.of(
				qualifiedname.replace('.', '/')));
		
		// Otherwise, use the parent class with a $ this class
		else
		{
			// The simple name contains the dot in it, which must be removed
			String sn = __cd.name();
			int ldx = sn.lastIndexOf('.');
			sn = sn.substring(ldx + 1);
			
			// Now use it
			this.namesymbol = (bns = BinaryNameSymbol.of(
				incl.namesymbol + "$" + sn));
		}
		
		// Determine the base name path for this class
		{
			// Fill with fragments
			List<String> bnp = new ArrayList<>();
			int n = qualifiedname.length();
			int base = 0;
			for (IdentifierSymbol i : bns)
				bnp.add(i.toString());
		
			// Setup
			Path p;
			this.basenamepath = (p = Paths.get(bnp.remove(0),
				bnp.<String>toArray(new String[bnp.size()])));
			
			// Setup name for markdown file location
			this.basemarkdownpath = __lowerPath(
				p.resolveSibling(p.getFileName() + ".mkd"));
			
			// The path where the file should be, hopefully
			this.basenamepathjava = p.resolveSibling(
				p.getFileName() + ".java");
		}
		
		// Get super class
		ClassDoc rawsc = __cd.superclass();
		MarkdownClass superclass = (rawsc == null ? null :
			__dm.markdownClass(rawsc));
		this.superclass = superclass;
		
		// Add to superclass of list
		if (superclass != null)
			superclass.superclassof.put(qualifiedname, this);
	}
	
	/**
	 * Returns the class binary name.
	 *
	 * @return The class binary name.
	 * @since 2016/10/01
	 */
	public BinaryNameSymbol binaryName()
	{
		return this.namesymbol;
	}
	
	/**
	 * Returns the path which should contain the file.
	 *
	 * @return The containing file.
	 * @since 2016/10/01
	 */
	public Path containingClassFile()
	{
		// If in another class, use that
		MarkdownClass containedin = this.containedin;
		if (containedin != null)
			return containedin.containingClassFile();
		
		// Otherwise use the normal Java path
		return this.basenamepathjava;
	}
	
	/**
	 * The path to the markdown file.
	 *
	 * @return The markdown path.
	 * @since 2016/10/01
	 */
	public Path markdownPath()
	{
		return this.basemarkdownpath;
	}
	
	/**
	 * Returns the qualified name of this class.
	 *
	 * @return The qualified name of this class.
	 * @since 2016/10/01
	 */
	public String qualifiedName()
	{
		return this.qualifiedname;
	}
	
	/**
	 * Returns the unqualified name of this class.
	 *
	 * @return The unqualified name.
	 * @since 2016/10/01
	 */
	public String unqualifiedName()
	{
		return this.unqualifiedname;
	}
	
	/**
	 * Writes the class documentation details to the output markdown file.
	 *
	 * @since 2016/09/13
	 */
	public void writeOutput()
	{
		// Get main
		DocletMain main = this.main;
		
		// Setup output
		try (MarkdownWriter md = new MarkdownWriter(new __AppendToStdOut__()))
		{
			// Top level header
			String qualifiedname = this.qualifiedname;
			md.headerSameLevel(qualifiedname);
			
			// Write class tree last
			md.headerSameLevel("Inheritance");
			
			// Print super class, if there is one
			MarkdownClass superclass = this.superclass;
			if (superclass != null)
			{
				md.print("Superclass: ");
				md.uri(main.uriToClassMarkdown(this, superclass),
					unqualifiedName());
				md.println();
			}
			
			// Classes which extend this class
			for (MarkdownClass superclassof : this.superclassof.values())
				md.printf("Superclass of: %s%n", superclassof.qualifiedname);
		}
		
		// {@squirreljme.error CF03 Could not write the output markdown file.}
		catch (IOException e)
		{
			throw new RuntimeException("CF03", e);
		}
	}
	
	/**
	 * Lowercases the entire path set.
	 *
	 * @param __p The path to lowercase.
	 * @return The lowercase form of the path.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	static Path __lowerPath(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Add lowercase forms
		List<String> bnp = new ArrayList<>();
		for (Path p : __p)
			bnp.add(__lowerString(p.toString()));
		
		// Rebuild
		return Paths.get(bnp.remove(0),
			bnp.<String>toArray(new String[bnp.size()]));
	}
	
	/**
	 * Lowercases the specified string and replaces out of range characters
	 * with escaped symbols.
	 *
	 * @param __s The string to lowercase.
	 * @return The string lowercased in pure ASCII.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	static String __lowerString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Build
		StringBuilder sb = new StringBuilder();
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Funny character?
			if (!(c == '.' || c == '_' || (c >= '0' && c <= '9') ||
				(c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')))
			{
				sb.append('_');
				sb.append(String.format("%04x", c & 0xFFFF));
			}
			
			// Lower?
			else if (c >= 'A' && c <= 'Z')
				sb.append((char)('a' + (c - 'A')));
				
			// same
			else
				sb.append(c);
		}
		
		// Return
		return sb.toString();
	}
}


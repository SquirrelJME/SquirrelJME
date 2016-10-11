// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.doclet;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.doclet.CommondocClass;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the base class for common details shared with multiple doclets.
 *
 * @since 2016/10/11
 */
public abstract class CommondocClass
{
	/** The main doclet. */
	protected final CommondocMain main;
	
	/** The wrapped class doclet. */
	protected final ClassDoc doc;
	
	/** The base class name path, uses directory components. */
	protected final Path basenamepath;
	
	/** The base path with Java extension. */
	protected final Path basenamepathjava;
	
	/** The qualified name of this class. */
	protected final String qualifiedname;
	
	/** The unqualified class name. */
	protected final String unqualifiedname;
	
	/** The class name used. */
	protected final BinaryNameSymbol namesymbol;
	
	/** The containing class (will be null if not an inner class). */
	protected final CommondocClass containedin;
	
	/** The super class. */
	protected final CommondocClass superclass;
	
	/** Implemented interfaces. */
	protected final Map<String, CommondocClass> interfaces =
		new SortedTreeMap<>();
	
	/** Classes that extend this class. */
	protected final Map<String, CommondocClass> superclassof =
		new SortedTreeMap<>();
	
	/** Classes which implement this class. */
	protected final Map<String, CommondocClass> interfacesof =
		new SortedTreeMap<>();
	
	/** Is this class explicit? */
	volatile boolean _implicit;
	
	/**
	 * Initializes the wrapped class.
	 *
	 * @param __dm The main doclet.
	 * @param __cd The class to reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public CommondocClass(CommondocMain __dm, ClassDoc __cd)
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
		CommondocClass incl = (cin == null ? null : __dm.wrapClass(cin));
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
			
			// The path where the file should be, hopefully
			this.basenamepathjava = p.resolveSibling(
				p.getFileName() + ".java");
		}
		
		// Get super class
		ClassDoc rawsc = __cd.superclass();
		CommondocClass superclass = (rawsc == null ? null :
			__dm.wrapClass(rawsc));
		this.superclass = superclass;
		
		// Add to superclass of list
		if (superclass != null)
			superclass.superclassof.put(qualifiedname, this);
		
		// Handle interfaces
		Map<String, CommondocClass> interfaces = this.interfaces;
		for (ClassDoc in : __cd.interfaces())
		{
			// Locate class
			CommondocClass inc = __dm.wrapClass(in);
			
			// Implemented by this class
			interfaces.put(inc.qualifiedname, inc);
			
			// That class implemented by this one
			inc.interfacesof.put(qualifiedname, this);
		}
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
		CommondocClass containedin = this.containedin;
		if (containedin != null)
			return containedin.containingClassFile();
		
		// Otherwise use the normal Java path
		return this.basenamepathjava;
	}
	
	/**
	 * Is this an implicitely defined class?
	 *
	 * @return {@code true} if it is implicit.
	 * @since 2016/10/11
	 */
	public boolean isImplicit()
	{
		return this._implicit;
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
}


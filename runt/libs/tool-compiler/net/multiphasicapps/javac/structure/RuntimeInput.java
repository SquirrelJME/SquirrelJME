// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.NoSuchInputException;
import net.multiphasicapps.javac.StringFileName;
import net.multiphasicapps.javac.syntax.ClassSyntax;
import net.multiphasicapps.javac.syntax.CompilationUnitSyntax;
import net.multiphasicapps.javac.syntax.ModifiersSyntax;
import net.multiphasicapps.javac.syntax.SyntaxException;
import net.multiphasicapps.javac.token.BufferedTokenSource;

/**
 * This class contians the input for the .
 *
 * @since 2018/05/03
 */
public final class RuntimeInput
{
	/** Output structure information. */
	protected final Structures structures =
		new Structures();
	
	/** The class path. */
	private final CompilerPathSet[] _classpath;
	
	/** The source path. */
	private final CompilerPathSet[] _sourcepath;
	
	/** Files which have been processed. */
	private final Set<String> _didfiles =
		new HashSet<>();
	
	/**
	 * Initializes the runtime input.
	 *
	 * @param __class The class path.
	 * @param __src The source path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public RuntimeInput(CompilerPathSet[] __class, CompilerPathSet[] __src)
		throws NullPointerException
	{
		this(Arrays.<CompilerPathSet>asList((__class == null ?
			new CompilerPathSet[0] : __class)),
			Arrays.<CompilerPathSet>asList((__src == null ?
			new CompilerPathSet[0] : __src)));
	}
	
	/**
	 * Initializes the runtime input.
	 *
	 * @param __class The class path.
	 * @param __src The source path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public RuntimeInput(Iterable<CompilerPathSet> __class,
		Iterable<CompilerPathSet> __src)
		throws NullPointerException
	{
		if (__class == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Check classpath
		List<CompilerPathSet> classes = new ArrayList<>();
		for (CompilerPathSet p : __class)
			if (p == null)
				throw new NullPointerException("NARG");
			else
				classes.add(p);
		
		// Check sources
		List<CompilerPathSet> sources = new ArrayList<>();
		for (CompilerPathSet p : __src)
			if (p == null)
				throw new NullPointerException("NARG");
			else
				sources.add(p);
		
		this._classpath = classes.<CompilerPathSet>toArray(
			new CompilerPathSet[classes.size()]);
		this._sourcepath = sources.<CompilerPathSet>toArray(
			new CompilerPathSet[sources.size()]);
	}
	
	/**
	 * Prcoesses a single class file.
	 *
	 * @param __fn The class file name to process.
	 * @throws StructureException If the source structure is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public final void processClassFile(String __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes a single source file.
	 *
	 * @param __fn The input file to process.
	 * @throws StructureException If the source structure is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public final void processSourceFile(String __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// Only process class files once
		Set<String> didfiles = this._didfiles;
		if (didfiles.contains(__fn))
			return;
		didfiles.add(__fn);
		
		// Used for location awareness
		StringFileName sfn = new StringFileName(__fn);
		
		// {@squirreljme.error AQ54 Cannot process source file which does not
		// end in {@code .java}.}
		if (!__fn.toLowerCase().endsWith(".java"))
			throw new StructureException(sfn, "AQ54");
		
		// Get basename and package name, used to quickly determine some things
		String basename = __fn.substring(0, __fn.length() - 5);
		int lastslash = basename.lastIndexOf('/');
		String rawinpackage = (lastslash < 0 ? null :
				basename.substring(0, lastslash)),
			baseclassname = (lastslash < 0 ? basename :
				basename.substring(lastslash + 1));
		
		// The package the class is in
		BinaryName inpackage = (rawinpackage == null ? null :
			new BinaryName(rawinpackage));
		
		// This will essentially check that the class is named with valid
		// characters
		BinaryName classname;
		try
		{
			// The package information has no class name
			if (baseclassname.equals("package-info"))
				classname = null;
			else
				classname = new BinaryName(basename);
		}
		catch (InvalidClassFormatException e)
		{
			// {@squirreljme.error AQ55 Cannot parse the source file because
			// it does not have a valid name.}
			throw new StructureException(sfn,
				String.format("AQ55 %s", basename));
		}
		
		// Determine the identifier of the class
		ClassIdentifier classident = (classname == null ? null :
			new ClassIdentifier(baseclassname));
		
		// Search for the source file
		CompilerInput ci = null;
		for (CompilerPathSet ps : this._sourcepath)
			try
			{
				if (null != (ci = ps.input(__fn)))
					break;
			}
			catch (NoSuchInputException e)
			{
			}
		
		// {@squirreljme.error AQ52 The source file does not exist.}
		if (ci == null)
			throw new StructureException(sfn, "AQ52");
		
		// Parse the syntax for the compilation unit and hope it works
		CompilationUnitSyntax cus;
		try (InputStream in = ci.open();
			BufferedTokenSource bts = new BufferedTokenSource(__fn, in))
		{
			cus = CompilationUnitSyntax.parse(bts);
		}
		
		// {@squirreljme.error AQ53 Could not parse the syntax for the source
		// file because it failed to read or has invalid syntax.}
		catch (IOException|SyntaxException e)
		{
			throw new StructureException(sfn, "AQ53", e);
		}
		
		// {@squirreljme.error AQ56 Source code file specified a package which
		// does not match the package represented in the source code itself.
		// (The package the source is in; The package the source code actually
		// specified)}
		BinaryName cuspackage = cus.inPackage();
		if (!Objects.equals(inpackage, cuspackage))
			throw new StructureException(sfn, String.format("AQ56 %s %s",
				inpackage, cuspackage));
		
		// Determine if the class was placed in the correct file
		ClassSyntax[] classes = cus.classes();
		if (classname != null)
		{
			// {@squirreljme.error AQ5b Source file declares no classes.}
			if (classes.length <= 0)
				throw new StructureException(sfn, "AQ5b");
			
			// Find the public class
			ClassSyntax pubclass = null;
			for (ClassSyntax cs : classes)
			{
				ModifiersSyntax mods = cs.modifiers();
				if (mods.isPublic())
				{
					// {@squirreljme.error AQ59 Class file contains multiple
					// public classes. (The first found public class; The
					// second found public class)}
					if (pubclass != null)
						throw new StructureException(sfn,
							String.format("AQ59 %s %s", pubclass.name(),
							cs.name()));
					
					pubclass = cs;
				}
			}
			
			// If only a single class is declared and no public class exists
			// then the only class which exists there must have a matching
			// name
			if (pubclass == null && classes.length == 1)
				pubclass = classes[0];
			
			// {@squirreljme.error AQ5a The name of the public class in the
			// file does not match the expected name of the source file.
			// (The public class name)}
			if (pubclass != null && !classident.equals(pubclass.name()))
				throw new StructureException(sfn,
					String.format("AQ5a %s", pubclass.name()));
		}
		
		// Is a package-info file
		else
		{
			// {@squirreljme.error AQ57 Source package-info files cannot
			// specify any classes.}
			if (classes.length != 0)
				throw new StructureException(sfn, "AQ57");
		}
		
		// So now that the source file has been parsed the resulting syntax
		// needs to be parsed itself to load structure data from it
		new CompilationUnitParser(cus, this.structures).run();
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes source code files and loads their required structure
	 * information performing basic compilation of them.
	 *
	 * @param __fn The source file name to process.
	 * @throws StructureException If the parsed structures are not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public final void processSourceFiles(String... __fn)
		throws StructureException, NullPointerException
	{
		this.processSourceFiles(Arrays.<String>asList((__fn == null ?
			new String[0] : __fn)));
	}
	
	/**
	 * Processes source code files and loads their required structure
	 * information performing basic compilation of them.
	 *
	 * @param __fn The source file name to process.
	 * @throws StructureException If the parsed structures are not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public final void processSourceFiles(Iterable<String> __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// Process each file
		for (String filename : __fn)
		{
			if (filename == null)
				throw new NullPointerException("NARG");	
			
			this.processSourceFile(filename);
		}
	}
	
	/**
	 * Returns the structures where classes have been read into.
	 *
	 * @return The structures used.
	 * @since 2018/05/05
	 */
	public final Structures structures()
	{
		return this.structures;
	}
}


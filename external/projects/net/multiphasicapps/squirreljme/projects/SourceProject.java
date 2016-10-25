// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.javac.base.Compiler;
import net.multiphasicapps.javac.base.CompilerInput;

/**
 * This is a project which provides source code that may be compiled by a
 * compiler into a binary project.
 *
 * @since 2016/10/20
 */
public final class SourceProject
	extends ProjectInfo
{
	/** The global compilation lock, permit only a single thread to compile. */
	private static final Object _COMPILE_LOCK =
		new Object();
	
	/**
	 * This is a fallback compiler which may be specified when it is not known.
	 * This sets an explicit compiler to use.
	 */
	static volatile Compiler _SPECIFIED_FALLBACK_COMPILER;
	
	/** The manifet used for the source project. */
	protected final SourceProjectManifest manifest;
	
	/** The root directory for the project. */
	protected final Path root;
	
	/** The project namespace. */
	protected final String namespace;
	
	/** The name of this project. */
	private volatile Reference<ProjectName> _name;
	
	/** Currently being compiled? */
	private volatile boolean _incompile;
	
	/**
	 * Initializes the source project.
	 *
	 * @param __man The manifest contianing source project information.
	 * @param __r The root directory where sources are located.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	SourceProject(SourceProjectManifest __man, Path __r)
		throws NullPointerException
	{
		// Check
		if (__man == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manifest = __man;
		this.root = __r;
		
		// {@squirreljme.error CI07 Could not determine the parent directory
		// of the given path. (The path to the project)}
		Path par = __r.normalize().getParent();
		if (par == null)
			throw new InvalidProjectException(String.format("CI07 %s", __r));
		this.namespace = par.getFileName().toString();
		
		// Check validity of the project name
		projectName();
	}
	
	/**
	 * Compiles the project if it is out of date and returns the binary
	 * project.
	 *
	 * @param __c The compiler to use, if {@code null} then the fallback
	 * compiler is used.
	 * @throws CompilationFailedException If compilation failed.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/20
	 */
	public BinaryProject compile(Compiler __c)
		throws CompilationFailedException, IOException
	{
		// Only allow a single thread to compile a project at a time
		Object compilelock = SourceProject._COMPILE_LOCK;
		synchronized (compilelock)
		{
			// {@squirreljme.error CI0b Source code to be compiled
			// eventually dependend on itself for compilation. (The name
			// of this project)}
			if (this._incompile)
				throw new CompilationFailedException(
					String.format("CI0b %s", projectName()));
		
			// Perform compilation
			try
			{
				// Set as compiling
				this._incompile = true;
			
				throw new Error("TODO");
			}
		
			// Clear the in compile flag and notify any waiting threads
			finally
			{
				// Clear
				this._incompile = false;
			}
		}
	}
	
	/**
	 * Returns the input that is used for the compiler.
	 *
	 * @return The input for the compiler.
	 * @since 2016/10/20
	 */
	public CompilerInput compilerInput()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Generates a binary manifest from this source project.
	 *
	 * This is used by the compiler. This may also be used to determine what
	 * kind of project a source project compiles into before compiling it.
	 *
	 * @return The binary manifest.
	 * @since 2016/10/20
	 */
	public BinaryProjectManifest generateBinaryManifest()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws InvalidProjectException If the project name is not valid.
	 * @since 2016/10/21
	 */
	@Override
	public ProjectName projectName()
		throws InvalidProjectException
	{
		// Get
		Reference<ProjectName> ref = this._name;
		ProjectName rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._name = new WeakReference<>((rv =
				new ProjectName(this.namespace, this.manifest.projectName())));
		
		// Return it
		return rv;
	}
	
	/**
	 * Sets the fallback compiler to use if no default could be used.
	 *
	 * @param __cc The compiler to use as a fallback.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	public static void setFallbackCompiler(Compiler __cc)
		throws NullPointerException
	{
		// Check
		if (__cc == null)
			throw new NullPointerException("NARG");
		
		// Set
		SourceProject._SPECIFIED_FALLBACK_COMPILER = __cc;
	}
	
	/**
	 * Calculates the name that a file would appear as inside of a ZIP file.
	 *
	 * @param __root The root path.
	 * @param __p The file to add.
	 * @return The ZIP compatible name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	static String __zipName(Path __root, Path __p)
		throws NullPointerException
	{
		// Check
		if (__root == null || __p == null)
			throw new NullPointerException();
		
		// Calculate relative name
		Path rel = __root.toAbsolutePath().relativize(__p.toAbsolutePath());
		
		// Build name
		StringBuilder sb = new StringBuilder();
		for (Path comp : rel)
		{
			// Prefix slash
			if (sb.length() > 0)
				sb.append('/');
			
			// Add component
			sb.append(comp);
		}
		
		// Return it
		return sb.toString();
	}
}


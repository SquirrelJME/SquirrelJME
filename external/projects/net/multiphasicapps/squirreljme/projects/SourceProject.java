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
	/**
	 * This is a fallback compiler which may be specified when it is not known.
	 * This sets an explicit compiler to use.
	 */
	static volatile Compiler _SPECIFIED_FALLBACK_COMPILER;
	
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
		throw new Error("TODO");
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


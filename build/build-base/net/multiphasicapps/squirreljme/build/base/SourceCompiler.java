// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.base;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * This interfaced is used to describe a compiler which is used to compile
 * Java source code into Java class files.
 *
 * Where the compilation log is initially output is undefined and unspecified
 * and it may be to standard error, a file on the disk, or not output at all.
 *
 * The default output for compilation is to ignore all files.
 *
 * @since 2016/12/19
 */
public interface SourceCompiler
{
	/**
	 * Adds a directory where class files may be looked up which are needed
	 * for compilation.
	 *
	 * @param __fd The directory where classes may be found.
	 * @throws IOException If it could not be added.
	 * @throws NullPointerException On null arguments.
	 * @throws 2016/12/24
	 */
	public abstract void addClassDirectory(FileDirectory __fd)
		throws IOException, NullPointerException;
	
	/**
	 * Adds source code which is to be compiled by the compiler.
	 *
	 * @param __fn The file name to compile.
	 * @throws IOException If it could not be added.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/24
	 */
	public abstract void addSource(String __fn)
		throws IOException, NullPointerException;
	
	/**
	 * Performs compilation.
	 *
	 * @return {@code true} if compilation has succeeded.
	 * @since 2016/12/24
	 */
	public abstract boolean compile();
	
	/**
	 * Sets the options which are used during compilation, the options
	 * follow the standard Java compiler options.
	 *
	 * @param __args Compilation options, if this is {@code null} then it
	 * should have the same effect as passing an empty array.
	 * @since 2016/12/24
	 */
	public abstract void setCompileOptions(String... __args);
	
	/**
	 * Sets the output where the result of compilation is to be placed.
	 *
	 * @param __o The output where compiled classes will be placed.
	 * @throws IOException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/24
	 */
	public abstract void setOutput(CompilerOutput __p)
		throws IOException, NullPointerException;
	
	/**
	 * Sets the writer which is used to output the compilation log.
	 *
	 * @param __w The output writer to print text to.
	 * @throws IOException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/24
	 */
	public abstract void setOutputLog(Writer __w)
		throws IOException, NullPointerException;
	
	/**
	 * Sets the directory where source code is to be looked up.
	 *
	 * @param __fd The directory where sources are to be looked up.
	 * @throws IOException If it could not be added.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/24
	 */
	public abstract void setSourceDirectory(FileDirectory __fd)
		throws IOException, NullPointerException;
}


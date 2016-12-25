// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.base.SourceCompiler;

/**
 * This provides access to a single instance of the standard Java compiler
 * that is included with the run-time since Java 6.
 *
 * @since 2016/12/21
 */
public class StandardCompiler
	implements SourceCompiler
{
	/** The internal Java compiler. */
	protected final JavaCompiler javac;
	
	/**
	 * Initializes the standard compiler.
	 *
	 * @since 2016/12/25
	 */
	public StandardCompiler()
	{
		// {@squirreljme.error BM03 No system Java compiler exists.}
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		if (javac == null)
			throw new RuntimeException("BM03");
		this.javac = javac;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void addClassDirectory(FileDirectory __fd)
		throws IOException, NullPointerException
	{
		// Check
		if (__fd == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void addSource(String __fn)
		throws IOException, NullPointerException
	{
		// Check
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void addSourceDirectory(FileDirectory __fd)
		throws IOException, NullPointerException
	{
		// Check
		if (__fd == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public boolean compile()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void setCompileOptions(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void setOutputDirectory(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void setOutputLog(Writer __w)
		throws IOException, NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}


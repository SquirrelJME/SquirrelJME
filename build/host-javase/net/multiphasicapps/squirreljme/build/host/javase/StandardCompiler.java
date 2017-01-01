// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import net.multiphasicapps.squirreljme.build.base.CompilerOutput;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.base.NullCompilerOutput;
import net.multiphasicapps.squirreljme.build.base.NullFileDirectory;
import net.multiphasicapps.squirreljme.build.base.SourceCompiler;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This provides access to a single instance of the standard Java compiler
 * that is included with the run-time since Java 6.
 *
 * @since 2016/12/21
 */
public class StandardCompiler
	implements SourceCompiler
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The internal Java compiler. */
	protected final JavaCompiler javac;
	
	/** Write output. */
	protected final Writer output =
		new __Out__();
	
	/** Source directories. */
	private volatile FileDirectory _sources =
		new NullFileDirectory();
	
	/** Class directories. */
	private final List<FileDirectory> _classes =
		new ArrayList<>();
	
	/** Source files to compile. */
	private final Set<String> _compile =
		new SortedTreeSet<>();
	
	/** Compilation options. */
	private volatile String[] _options =
		new String[0];
	
	/** The current write target, where logs go. */
	private volatile Writer _console =
		new OutputStreamWriter(System.err);
	
	/** The current output. */
	private volatile CompilerOutput _output =
		new NullCompilerOutput();
	
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
		
		// Lock
		synchronized (this.lock)
		{
			this._classes.add(__fd);
		}
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
		
		// Lock
		synchronized (this.lock)
		{
			this._compile.add(__fn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public boolean compile()
	{
		// Lock
		synchronized (this.lock)
		{
			// Setup file manager
			__FileManager__ fm = new __FileManager__(this._output,
				this._classes, this._sources);
			
			// Determine compilation units
			Set<JavaFileObject> units = new LinkedHashSet<>();
			for (JavaFileObject f : fm.getJavaFileObjectsFromStrings(
				UnmodifiableSet.<String>of(this._compile)))
				units.add(f);
			
			// Setup target task
			JavaCompiler javac = this.javac;
			JavaCompiler.CompilationTask task = javac.getTask(
				this.output, fm, null, UnmodifiableList.<String>of(
				Arrays.<String>asList(this._options)), null, units);
			
			// Run it
			return task.call();
		}
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
		else
		{
			__args = __args.clone();
		
			// Never have null
			for (int i = 0, n = __args.length; i < n; i++)
				if (__args[i] == null)
					__args[i] = "";
		}
		
		// Lock
		synchronized (this.lock)
		{
			// Set
			this._options = __args;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void setOutput(CompilerOutput __o)
		throws IOException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._output = __o;
		}
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
		
		// Flush the old writer before it is set again
		Writer w = this._console;
		if (w instanceof Flushable)
			try
			{
				w.flush();
			}
			catch (IOException e)
			{
				// Ignore
			}
		
		// Set new one
		this._console = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws 2016/12/24
	 */
	@Override
	public void setSourceDirectory(FileDirectory __fd)
		throws IOException, NullPointerException
	{
		// Check
		if (__fd == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._sources = __fd;
		}
	}
	
	/**
	 * Wraps the output.
	 *
	 * @since 2016/12/25
	 */
	private class __Out__
		extends Writer
		implements Flushable
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/12/25
		 */
		@Override
		public void close()
		{
			// Does nothing
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/25
		 */
		@Override
		public void flush()
			throws IOException
		{
			StandardCompiler.this._console.flush();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/25
		 */
		@Override
		public void write(char[] __c, int __o, int __l)
			throws IOException
		{
			StandardCompiler.this._console.write(__c, __o, __l);
		}
	}
}


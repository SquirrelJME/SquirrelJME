// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to process namespaces for JIT compilation and resource
 * inclusion. The purpose of this class is so that the builder for SquirrelJME
 * and the JVM can recompile namespaces using very similar means (the code
 * would essentially be duplicated).
 *
 * @since 2016/07/07
 */
public class JITNamespaceProcessor
{
	/** Cache copy size. */
	private static final int _CACHE_SIZE =
		4096;
	
	/** The configuration to use. */
	protected final JITOutputConfig.Immutable config;
	
	/** The output of the JIT. */
	protected final JITOutput output;
	
	/** Contents for namespaces. */
	protected final JITNamespaceContent contents;
	
	/** The list of processed namespaces. */
	private final List<String> _processed =
		new LinkedList<>();
	
	/**
	 * Initializes the namespace processor.
	 *
	 * @param __conf The JIT configuration to use.
	 * @throws JITException If no output could be created with the given
	 * configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	public JITNamespaceProcessor(JITOutputConfig.Immutable __conf,
		JITNamespaceContent __cont)
		throws JITException, NullPointerException
	{
		// Check
		if (__conf == null || __cont == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.contents = __cont;
		
		// {@squirreljme.error ED0h No output could be created for the
		// given configuration. (The configuration)}
		JITOutput output = JITOutputFactory.createOutput(__conf);
		this.output = output;
		if (output == null)
			throw new JITException(String.format("ED0h %s", __conf));
	}
	
	/**
	 * This returns the name of the output executable which should be
	 * compatible with the target system.
	 *
	 * @return An executable which is compatible with the target system.
	 * @since 2016/07/18
	 */
	public final String executableName()
	{
		return this.output.executableName();
	}
	
	/**
	 * This links together any input namespaces which were processed and
	 * produces a single binary which contains all of them linked together.
	 *
	 * Calling this is not required unless a binary which natively runs
	 * SquirrelJME is desired.
	 *
	 * @param __os The stream where the binary is to be written.
	 * @throws IOException On read/write errors.
	 * @throws JITException If the binary could not be linked.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public final void linkBinary(OutputStream __os)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ED0j No cache creator was associated with
		// the configuration, thus binaries cannot be linked because they
		// rely on cached data sets.}
		JITCacheCreator jcc = this.config.cacheCreator();
		if (jcc == null)
			throw new JITException("ED0j");
		
		// Get processed namespace list
		String[] procset;
		List<String> processed = this._processed;
		synchronized (processed)
		{
			procset = processed.<String>toArray(new String[processed.size()]);
		}
		
		// Load input streams for all of the namespaces
		int n = procset.length;
		InputStream[] ins = new InputStream[n];
		int i = 0;
		for (String s : procset)
		{
			// {@squirreljme.error ED0k A null namespace was processed, this
			// should not occur.}
			if (s == null)
				throw new NullPointerException("ED0k");
			
			// {@squirreljme.error ED0l No cache input stream was opened for
			// the specified namespace. (The namespace)}
			InputStream is;
			ins[i++] = (is = jcc.openCache(s));
			if (is == null)
				throw new JITException(String.format("ED0l %s", s));
		}
		
		// Perform linking
		this.output.linkBinary(__os, procset, ins);
	}
	
	/**
	 * Performs namespace processing.
	 *
	 * @param __ns The namespace to process.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	public final void processNamespace(String __ns)
		throws IOException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
			
		// Setup output for a given namespace
		JITOutput output = this.output;
		
		// Go through the directory for the given namespace
		// Also create the cached output if it was requested
		try (JITNamespaceWriter nsw = output.beginNamespace(__ns);
			JITNamespaceContent.Directory dir =
				this.contents.directoryOf(__ns))
		{
			// Go through directory entries
			byte[] buf = null;
			for (JITNamespaceContent.Entry ent : dir)
			{
				// Determine if this is a class to recompile or not
				String name = ent.name();
				boolean isclass = name.endsWith(".class");
				
				// Open the input regardless
				try (InputStream is = ent.open())
				{
					// Recompiling class file with JIT?
					if (isclass)
						__doClass(output, nsw, is);
				
					// Copying resource data
					else
					{
						buf = __buffer(buf);
						__doResource(buf, nsw, name, is);
					}
				}
			}
		}
		
		// Add to processed list
		List<String> processed = this._processed;
		synchronized (processed)
		{
			processed.add(__ns);
		}
	}
	
	/**
	 * Performs class recompilation.
	 *
	 * @param __output The output.
	 * @param __nsw The namespace writer.
	 * @param __is The input data stream.
	 * @since 2016/07/07
	 */
	private void __doClass(JITOutput __output, JITNamespaceWriter __nsw,
		InputStream __is)
	{
		// Setup new JIT
		JIT jit = new JIT(__output, __nsw, __is);
		
		// Run the JIT
		jit.run();
	}
	
	/**
	 * Performs resource copying.
	 *
	 * @param __buf The temporary buffer.
	 * @param __nsw The namespace writer.
	 * @param __name The entry name.
	 * @param __is The input data stream.
	 * @throws IOException On copy errors.
	 * @since 2016/07/07
	 */
	private void __doResource(byte[] __buf, JITNamespaceWriter __nsw,
		String __name, InputStream __is)
		throws IOException
	{
		// Open output
		try (OutputStream ros = __nsw.beginResource(__name))
		{
			// Read loop
			for (;;)
			{
				// Read
				int rc = __is.read(__buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Copy
				ros.write(__buf, 0, rc);
			}
		}
	}
	
	/**
	 * If the specified buffer is not allocated then it is attempted to be
	 * allocated and returned.
	 *
	 * There may be cases where the default buffer size is too large for a
	 * given system, as such smaller buffer sizes will be attempted.
	 *
	 * @param __buf If {@code null} a buffer is allocated and returned.
	 * @return Either {@code __buf} or a new buffer.
	 * @throws JITException If there is not enough memory available to allocate
	 * a buffer of any given size.
	 * @since 2016/07/07
	 */
	private static byte[] __buffer(byte[] __buf)
		throws JITException
	{
		// Already exists, use it
		if (__buf != null)
			return __buf;
		
		for (int sz = _CACHE_SIZE; sz >= 1; sz >>= 1)
			try
			{
				// Attempt allocation using the given size
				return new byte[sz];
			}
			catch (OutOfMemoryError e)
			{
				continue;
			}
		
		// {@squirreljme.error ED0i Could not allocate enough memory for a
		// temporary working buffer.}
		throw new JITException("ED0i");
	}
}


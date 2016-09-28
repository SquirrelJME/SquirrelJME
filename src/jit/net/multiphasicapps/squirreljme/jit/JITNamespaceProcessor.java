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

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.ClassDecoder;
import net.multiphasicapps.squirreljme.classformat.ClassDecoderOption;
import net.multiphasicapps.squirreljme.classformat.ClassFormatException;
import net.multiphasicapps.squirreljme.jit.base.JITConfig;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITNamespaceBrowser;
import net.multiphasicapps.squirreljme.jit.base.JITNamespaceOutput;
import net.multiphasicapps.squirreljme.jit.base.JITNamespaceOutputShared;
import net.multiphasicapps.squirreljme.jit.base.JITNamespaceOutputSingle;

/**
 * This class is used to process namespaces for JIT compilation and resource
 * inclusion. The purpose of this class is so that the builder for SquirrelJME
 * and the JVM can recompile namespaces using very similar means (the code
 * would essentially be duplicated).
 *
 * @since 2016/07/07
 */
public class JITNamespaceProcessor
	implements AutoCloseable
{
	/** Cache copy size. */
	private static final int _CACHE_SIZE =
		4096;
	
	/** The configuration to use. */
	protected final JITConfig config;
	
	/** The output of the JIT. */
	protected final JITOutput output;
	
	/** Browser for namespaces. */
	protected final JITNamespaceBrowser browser;
	
	/** Progress indicator. */
	protected final JITNamespaceProcessorProgress progress;
	
	/** The class which is used to generate where output code is placed. */
	protected final JITNamespaceOutput nsoutput;
	
	/** Shared output stream, if such a thing exists. */
	protected final OutputStream sharedoutput;
	
	/** The list of processed namespaces. */
	private final List<String> _processed =
		new LinkedList<>();
	
	/**
	 * Initializes the namespace processor.
	 *
	 * @param __conf The JIT configuration to use.
	 * @param __b The namespace browser which is used for namespace input
	 * and output.
	 * @param __nso This is class which is capable of creating a single shared
	 * output or multiple singlular outputs when generating namespaces.
	 * @throws JITException If no output could be created with the given
	 * configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	public JITNamespaceProcessor(JITConfig __conf, JITNamespaceBrowser __b,
		JITNamespaceOutput __nso)
		throws JITException, NullPointerException
	{
		this(__conf, __b, __nso, null);
	}
	
	/**
	 * Initializes the namespace processor using the given output stream, this
	 * wraps another constructor.
	 *
	 * @param __conf As forwarded.
	 * @param __b As forwarded.
	 * @param __nso As forwarded.
	 * @throws JITException As forwarded.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	public JITNamespaceProcessor(JITConfig __conf, JITNamespaceBrowser __b,
		OutputStream __nso)
		throws JITException, NullPointerException
	{
		this(__conf, __b, __wrapNSO(__nso), null);
	}
	
	/**
	 * Initializes the namespace processor using the given output stream, this
	 * wraps another constructor.
	 *
	 * @param __conf As forwarded.
	 * @param __b As forwarded.
	 * @param __nso As forwarded.
	 * @param __prog Optional progress indicator.
	 * @throws JITException As forwarded.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __prog}.
	 * @since 2016/07/07
	 */
	public JITNamespaceProcessor(JITConfig __conf, JITNamespaceBrowser __b,
		OutputStream __nso, JITNamespaceProcessorProgress __prog)
		throws JITException, NullPointerException
	{
		this(__conf, __b, __wrapNSO(__nso), __prog);
	}
	
	/**
	 * Initializes the namespace processor.
	 *
	 * @param __conf The JIT configuration to use.
	 * @param __b The namespace browser which is used for namespace input
	 * and output.
	 * @param __prog Progress indicator for the processor.
	 * @throws JITException If no output could be created with the given
	 * configuration.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __prog}.
	 * @since 2016/07/23
	 */
	public JITNamespaceProcessor(JITConfig __conf, JITNamespaceBrowser __b,
		JITNamespaceOutput __nso, JITNamespaceProcessorProgress __prog)
		throws JITException, NullPointerException
	{
		// Check
		if (__conf == null || __b == null || __nso == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.browser = __b;
		this.progress = __prog;
		this.nsoutput = __nso;
		
		// If a shared output was requested then create it
		OutputStream sharedoutput;
		if (__nso instanceof JITNamespaceOutputShared)
			try
			{
				sharedoutput = ((JITNamespaceOutputShared)__nso).
					outputShared();
			}
			
			// {@squirreljme.error ED06 Could not create the shared output.}
			catch (IOException e)
			{
				throw new JITException("ED06", e);
			}
		
		// No shared output used
		else
			sharedoutput = null;
		
		// Set
		this.sharedoutput = sharedoutput;
		
		// {@squirreljme.error ED0v No output factory was specified in the
		// output. (The configuration)}
		JITOutputFactory fact = __conf.<JITOutputFactory>getAsClass(
			JITConfig.FACTORY_PROPERTY, JITOutputFactory.class);
		if (fact == null)
			throw new JITException(String.format("ED0v %s", __conf));
		
		// {@squirreljme.error ED0h No output could be created for the
		// given configuration. (The configuration)}
		JITOutput output = fact.create(__conf);
		if (output == null)
			throw new JITException(String.format("ED0h %s", __conf));
		this.output = output;
		
		// If shared output is being used then tell the output that it is being
		// done so that it can setup an output binary if required
		if (sharedoutput != null)
			output.sharedOutput(sharedoutput);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/28
	 */
	@Override
	public void close()
		throws JITException
	{
		// Close it
		this.output.close();
		
		// Also close the shared output
		OutputStream so = this.sharedoutput;
		if (so != null)
			try
			{
				so.close();
			}
			
			// {@squirreljme.error ED07 Could not close the shared output.}
			catch (IOException e)
			{
				throw new JITException("ED07", e);
			}
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
		
		// Progress
		JITNamespaceProcessorProgress progress = this.progress;
		if (progress != null)
			progress.progressNamespace(__ns);
		
		// Go through the directory for the given namespace
		// Also create the cached output if a singular output form is used
		JITNamespaceOutput nso = this.nsoutput;
		JITNamespaceBrowser browser = this.browser;
		try (OutputStream os = ((nso instanceof JITNamespaceOutputSingle) ?
				((JITNamespaceOutputSingle)nso).outputSingle(__ns) : null);
			JITNamespaceWriter nsw = output.beginNamespace(__ns, os);
			JITNamespaceBrowser.Directory dir =
				browser.directoryOf(__ns))
		{
			// Go through directory entries
			byte[] buf = null;
			JITNamespaceBrowser.Entry ent;
			while (null != (ent = dir.nextEntry()))
				try (InputStream is = ent)
				{
					// Determine if this is a class to recompile or not
					String name = ent.name();
					boolean isclass = name.endsWith(".class");
				
					// Recompiling class file with JIT?
					if (isclass)
					{
						// Progress
						if (progress != null)
							progress.progressClass(name);
					
						// Handle it
						__doClass(output, nsw, is);
					}
			
					// Copying resource data
					else
					{
						buf = __buffer(buf);
						__doResource(buf, nsw, name, is);
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
		// Perform the JIT by running and parsing the class file.
		try (__ClassWriter__ cw = new __ClassWriter__(__nsw))
		{
			// Setup data stream
			DataInputStream dis = new DataInputStream(__is);
			
			// Start decoding the class
			ClassDecoder cd = new ClassDecoder(dis, cw);
			cd.decode();
		}
		
		// {@squirreljme.error ED02 Failed to read the class file.}
		catch (IOException|ClassFormatException e)
		{
			throw new JITException("ED02", e);
		}
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
		// Progress
		JITNamespaceProcessorProgress progress = this.progress;
		if (progress != null)
			progress.progressResource(__name);
		
		// Open output
		try (JITResourceWriter ros = __nsw.beginResource(__name))
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
	
	/**
	 * This creates a wrapped shared namespace output which returns the input
	 * {@link OutputStream}.
	 *
	 * @param __os The stream to wrap.
	 * @return The shared output wrapping the given stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/28
	 */
	private static JITNamespaceOutputShared __wrapNSO(final OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new JITNamespaceOutputShared()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/09/28
				 */
				@Override
				public OutputStream outputShared()
				{
					return __os;
				}
			};
	}
}


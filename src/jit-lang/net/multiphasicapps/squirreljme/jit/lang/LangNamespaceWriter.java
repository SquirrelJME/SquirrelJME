// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is the base namespace writer for language outputs. Since namespaces
 * only have a single output stream, all language based writers write their
 * desired contents to a TAR archive. The output archive could then be
 * extracted and its code compiled accordingly.
 *
 * @since 2016/07/09
 */
public abstract class LangNamespaceWriter
	implements JITNamespaceWriter
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The target configuration. */
	protected final JITOutputConfig.Immutable config;
	
	/** The namespace being written. */
	protected final String namespace;
	
	/** The output stream for writing to the ZIP. */
	protected final ZipStreamWriter zipwriter;
	
	/**
	 * Initializes the base namespace writer.
	 *
	 * @param __ns The target namespace name.
	 * @param __config The configuration being used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/09
	 */
	public LangNamespaceWriter(String __ns, JITOutputConfig.Immutable __config)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __config == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __ns;
		this.config = __config;
		
		// Get the cache creator since all language outputs generate through
		// this interface.
		JITCacheCreator jcc = __config.cacheCreator();
		
		// {@squirreljme.error AZ01 The language based namespace writer only
		// operates when a cache is being written.}
		if (jcc == null)
			throw new JITException(String.format("AZ01 %s", jcc));
		
		// Create the namespace cache which creates a ZIP file.
		try
		{
			// Stream a ZIP
			ZipStreamWriter zipwriter = new ZipStreamWriter(
				jcc.createCache(__ns));
			
			// Set
			this.zipwriter = zipwriter;
		}
		
		// {@squirreljme.error AZ02 Failed to create a namespace cache.}
		catch (IOException e)
		{
			throw new JITException("AZ02", e);
		}
	}
	
	/**
	 * Creates a class writer which is used to generate source code which
	 * matches the layout and logic of the input class.
	 *
	 * @param __rname The file name of the class.
	 * @param __cn The class name.
	 * @param __ps The output print stream to write source code.
	 * @return A class writer for the given language.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	protected abstract LangClassWriter createClassWriter(String __rname,
		ClassNameSymbol __cn, PrintStream __ps)
		throws NullPointerException;
	
	/**
	 * Creates a resource output stream which is used to write namespace
	 * resources which are available for a given language.
	 *
	 * @param __rname The file name of the resource.
	 * @param __ps The target stream.
	 * @return The resource output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	protected abstract ResourceOutputStream createResourceOutputStream(
		String __rname, PrintStream __ps)
		throws NullPointerException;
	
	/**
	 * Before classes are created in the output, they must be named using an
	 * unspecified form which is usually compatible with the target language.
	 *
	 * @param __name The name of the class to translate.
	 * @return A string which is compatible with ZIP file names and can be used
	 * in the given language.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public abstract String nameClass(ClassNameSymbol __name)
		throws NullPointerException;
	
	/**
	 * Before resources can be created, the name of the file must be created
	 * first.
	 *
	 * @param __name The resource to name.
	 * @return A string which is compatible with ZIP file names and can be used
	 * in the given language.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public abstract String nameResource(String __name)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public final JITClassWriter beginClass(ClassNameSymbol __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// The resource must be named first
			String rname = nameClass(__cn);
			
			// Create output
			try
			{
				return createClassWriter(rname, __cn,
					new PrintStream(new __DebugPrintStream__(
					this.zipwriter.nextEntry(extensionClass(rname),
					ZipCompressionType.DEFAULT_COMPRESSION)), true, "utf-8"));
			}
			
			// {@squirreljme.error AZ05 Failed to create class output. (The
			// name of the class)}
			catch (IOException e)
			{
				throw new JITException(String.format("AZ05 %s", __cn), e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public final OutputStream beginResource(String __name)
		throws JITException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// The resource must be named first
			String rname = nameResource(__name);
			
			// Create output
			try
			{
				return createResourceOutputStream(rname,
					new PrintStream(new __DebugPrintStream__(
					this.zipwriter.nextEntry(extensionResource(rname),
					ZipCompressionType.DEFAULT_COMPRESSION)), true, "utf-8"));
			}
			
			// {@squirreljme.error AZ04 Failed to create resource output. (The
			// name of the resource)}
			catch (IOException e)
			{
				throw new JITException(String.format("AZ04 %s", __name), e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public void close()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Close the ZIP file
			try
			{
				// Finish writing the ZIP.
				this.zipwriter.close();
			}
		
			// {@squirreljme.error AZ03 Failed to close the ZIP.}
			catch (IOException e)
			{
				throw new JITException("AZ03", e);
			}
		}
	}
	
	/**
	 * Possibly appends or prepends an extension before to a class before
	 * placing it into a ZIP so that it is named correctly.
	 *
	 * @param __n The name to add an extension to.
	 * @return The name as it should appear in the ZIP.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public String extensionClass(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Do not modify
		return __n;
	}
	
	/**
	 * Possibly appends or prepends an extension to a resource before placing
	 * it into the ZIP so that it is named accordingly.
	 *
	 * @param __n The name to add an extension to.
	 * @return The name as it should appear in the ZIP.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public String extensionResource(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Do not modify
		return __n;
	}
	
	/**
	 * Includes a file to the output ZIP using the given name.
	 *
	 * @param __n The name of the file.
	 * @param __is The file data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/20
	 */
	protected final void includeFile(String __n, InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Create
		synchronized (this.lock)
		{
			try (OutputStream os = this.zipwriter.nextEntry(__n,
				ZipCompressionType.DEFAULT_COMPRESSION))
			{
				// Copy all of the data
				byte[] buf = new byte[64];
				for (;;)
				{
					// Read
					int rc = __is.read(buf);
				
					// EOF?
					if (rc < 0)
						break;
				
					// Write
					os.write(buf, 0, rc);
				}
			}
		}
	}
	
	/**
	 * Includes a file to the output ZIP using the given name.
	 *
	 * @param __n The name of the file.
	 * @param __is The file data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/20
	 */
	protected final void includeFile(String __n, ByteArrayOutputStream __baos)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null || __baos == null)
			throw new NullPointerException("NARG");
		
		// Create
		synchronized (this.lock)
		{
			try (OutputStream os = this.zipwriter.nextEntry(__n,
				ZipCompressionType.DEFAULT_COMPRESSION))
			{
				__baos.writeTo(os);
			}
		}
	}
}


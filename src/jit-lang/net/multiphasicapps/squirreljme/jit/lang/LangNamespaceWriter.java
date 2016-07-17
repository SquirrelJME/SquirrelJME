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

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
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
		
		throw new Error("TODO");
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
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public final void close()
		throws JITException
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


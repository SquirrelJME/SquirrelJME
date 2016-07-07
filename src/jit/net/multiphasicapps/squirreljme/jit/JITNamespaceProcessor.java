// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

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
	/** The configuration to use. */
	protected final JITOutputConfig.Immutable config;
	
	/** The output of the JIT. */
	protected final JITOutput output;
	
	/** Contents for namespaces. */
	protected final JITNamespaceContent contents;
	
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
		
		// Using a cache creator for output?
		JITCacheCreator jcc = this.config.cacheCreator();
		
		// Go through the directory for the given namespace
		// Also create the cached output if it was requested
		try (JITNamespaceContent.Directory dir =
			this.contents.directoryOf(__ns);
			OutputStream os = (jcc != null ? jcc.createCache(__ns) : null))
		{
			throw new Error("TODO");
		}
	}
}


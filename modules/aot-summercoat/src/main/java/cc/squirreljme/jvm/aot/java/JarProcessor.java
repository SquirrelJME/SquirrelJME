// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.java;

import cc.squirreljme.runtime.cldc.archive.ArchiveStreamReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * Processes a single Java Jar.
 *
 * @since 2022/08/04
 */
public class JarProcessor
	implements Closeable
{
	/** The input archive to read files from. */
	protected final ArchiveStreamReader input;
	
	/** The handler for Jars. */
	protected final JarHandler handler;
	
	/**
	 * Initialize the Jar processor, which reads every class and resource
	 * accordingly.
	 * 
	 * @param __input The stream to read Jar data from, treated as a Zip.
	 * @param __handler The handler for Jar data.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	public JarProcessor(InputStream __input, JarHandler __handler)
		throws NullPointerException
	{
		this(new ZipStreamReader(__input), __handler);
	}
	
	/**
	 * Initialize the Jar processor, which reads every class and resource
	 * accordingly.
	 * 
	 * @param __input The stream to read Jar data from.
	 * @param __handler The handler for Jar data.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	public JarProcessor(ArchiveStreamReader __input, JarHandler __handler)
		throws NullPointerException
	{
		if (__input == null || __handler == null)
			throw new NullPointerException("NARG");
		
		this.input = __input;
		this.handler = __handler;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public void close()
		throws IOException
	{
		this.input.close();
	}
}

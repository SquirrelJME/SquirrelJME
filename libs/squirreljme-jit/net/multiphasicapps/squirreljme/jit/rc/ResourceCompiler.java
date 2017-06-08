// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.rc;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.ClusterIdentifier;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.LinkTable;

/**
 * This class is used to compile resources contained within JARs and for their
 * placement into output executables.
 *
 * @since 2017/06/02
 */
public final class ResourceCompiler
	implements Runnable
{
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/** The input data for the link table. */
	protected final InputStream in;
	
	/** The output stream to write to. */
	protected final OutputStream out;
	
	/** The name of this resource. */
	protected final String name;
	
	/**
	 * Initializes the resource compiler.
	 *
	 * @param __jc The JIT configuration.
	 * @param __is The stream containing the resource data.
	 * @param __n The name of the resource.
	 * @param __ci The cluster the resource is in.
	 * @param __lt The link table which is given the compiled resource data.
	 * @return The resource compiler task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/02
	 */
	public ResourceCompiler(JITConfig __jc, InputStream __is, String __n,
		ClusterIdentifier __ci, LinkTable __lt)
		throws NullPointerException
	{
		// Check
		if (__jc == null || __is == null || __n == null || __ci == null ||
			__lt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __jc;
		this.in = __is;
		this.name = __n;
		
		// Create output
		this.out = __lt.createResource(__ci, __n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/02
	 */
	@Override
	public void run()
	{
		InputStream in = this.in;
		try (OutputStream out = this.out)
		{
			byte[] buf = new byte[512];
			for (;;)
			{
				int rc = in.read(buf);
				if (rc < 0)
					break;
				out.write(buf, 0, rc);
			}
		}
		
		// {@squirreljme.error JI02 Failed to write the resource data. (The
		// name of this resource)}
		catch (IOException e)
		{
			throw new JITException(String.format("JT02 %s", this.name), e);
		}
	}
}


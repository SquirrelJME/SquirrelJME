// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * This represents information about a single resource in the output binary
 * which translates from a resource within the JAR.
 *
 * @since 2017/06/17
 */
public class Resource
	extends __SubState__
{
	/** The reference to the owning cluster. */
	protected final Reference<Cluster> cluster;
	
	/** The name of this resource. */
	protected final String name;
	
	/** The fragment this resource is associated with. */
	private volatile Reference<Fragment> _fragment;
	
	/** Has the resource already been parsed? */
	private volatile boolean _parsed;
	
	/**
	 * Initializes the resource.
	 *
	 * @param __ls The owning linker state.
	 * @param __n The name of this resource.
	 * @param __cr The reference to the owning cluster.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/18
	 */
	Resource(Reference<LinkerState> __ls, String __n,
		Reference<Cluster> __cr)
		throws NullPointerException
	{
		super(__ls);
		
		// Check
		if (__n == null || __cr == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.cluster = __cr;
	}
	
	/**
	 * Parses the specified input stream as the data for the given resource and
	 * stores it within the binary section.
	 *
	 * @param __is The stream containing the binary data.
	 * @throws IllegalStateException If the resource has already been parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/20
	 */
	final void __parse(InputStream __is)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI10 The resource has already been parsed.}
		if (this._parsed)
			throw new IllegalStateException("JI10");
		this._parsed = true;
		
		// Need to declare a new section
		LinkerState ls = this.__linkerState();
		Reference<LinkerState> rls = ls.__reference();
		FragmentBuilder sb = new FragmentBuilder(rls);
		
		// Copy all bytes to the output
		byte[] buf = new byte[512];
		for (;;)
		{
			int rc = __is.read(buf);
			
			if (rc < 0)
				break;
			
			sb.append(buf, 0, rc);
		}
		
		// Set fragment
		this._fragment = sb.__build(ls.__sectionCounter().nextResource(
			ls, this.cluster.get(), this.name));
	}
}


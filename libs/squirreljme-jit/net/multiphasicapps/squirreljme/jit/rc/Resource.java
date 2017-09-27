// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.rc;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This represents a single resource which may exist within a class.
 *
 * @since 2017/09/27
 */
public final class Resource
{
	/** The name of this resource. */
	protected final String name;
	
	/** The data which makes up this resource. */
	protected final byte[] data;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the resource.
	 *
	 * @param __n The name of the resource.
	 * @param __d The data which makes up the resource, this is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	Resource(String __n, byte[] __d)
		throws NullPointerException
	{
		if (__n == null || __d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.data = __d;
	}
	
	/**
	 * Returns the name of this resource.
	 *
	 * @return The name of the resource.
	 * @since 2017/09/27
	 */
	public String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the size of this resource.
	 *
	 * @return The resource size.
	 * @since 2017/09/27
	 */
	public int size()
	{
		return this.data.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Resource %s (%d bytes)", this.name, this.data.length)));
		
		return rv;
	}
	
	/**
	 * Reads the resource from the given input stream and initializes it.
	 *
	 * @param __n The name of the resource.
	 * @param __in The input stream containing the resource data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public static Resource read(String __n, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__n == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Copy bytes
		ByteDeque q = new ByteDeque();
		byte[] buf = new byte[512];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				break;
			
			q.addLast(buf, 0, rc);
		}
		
		// Create
		return new Resource(__n, q.toByteArray());
	}
}


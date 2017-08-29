// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a linking point which represents a single resource that exists
 * within the virtual machine.
 *
 * @since 2017/08/29
 */
public final class ResourceLinkingPoint
	implements LinkingPoint
{
	/** The JAR the resource is in. */
	protected final String jar;
	
	/** The name of the resource. */
	protected final String rc;
	
	/** The string representation of this point. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the resource linking point.
	 *
	 * @param __jar The JAR the resource is in.
	 * @param __rc The name of the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/29
	 */
	public ResourceLinkingPoint(String __jar, String __rc)
		throws NullPointerException
	{
		// Check
		if (__jar == null || __rc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jar = __jar;
		this.rc = __rc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ResourceLinkingPoint))
			return false;
		
		ResourceLinkingPoint o = (ResourceLinkingPoint)__o;
		return this.jar.equals(o.jar) &&
			this.rc.equals(o.rc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public final int hashCode()
	{
		return this.jar.hashCode() ^ this.rc.hashCode();
	}
	
	/**
	 * Returns the JAR this resource exists within.
	 *
	 * @return The JAR containing this resource.
	 * @since 2017/08/29
	 */
	public final String jar()
	{
		return this.jar;
	}
	
	/**
	 * Returns the name of this resource.
	 *
	 * @return The name of this resource.
	 * @since 2017/08/29
	 */
	public final String resource()
	{
		return this.rc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s:/%s",
				this.jar, this.rc)));
		
		return rv;
	}
}


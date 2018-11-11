// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.profiler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the location of a frame.
 *
 * @since 2018/11/11
 */
public final class FrameLocation
	implements Comparable<FrameLocation>
{
	/** The class. */
	protected final String inclass;
	
	/** The method name. */
	protected final String methodname;
	
	/** The method type. */
	protected final String methodtype;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the location.
	 *
	 * @param __cl The class.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public FrameLocation(String __cl, String __mn, String __mt)
		throws NullPointerException
	{
		if (__cl == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		this.inclass = __cl;
		this.methodname = __mn;
		this.methodtype = __mt;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final int compareTo(FrameLocation __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof FrameLocation))
			return false;
		
		FrameLocation o = (FrameLocation)__o;
		return this.inclass.equals(o.inclass) &&
			this.methodname.equals(o.methodname) &&
			this.methodtype.equals(o.methodtype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final int hashCode()
	{
		return this.inclass.hashCode() ^
			this.methodname.hashCode() ^
			this.methodtype.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.inclass + "::" +
				this.methodname + ":" + this.methodtype));
		
		return rv;
	}
}


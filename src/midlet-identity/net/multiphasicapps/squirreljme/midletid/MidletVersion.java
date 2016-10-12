// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midletid;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a midlet version.
 *
 * @since 2016/10/12
 */
public final class MidletVersion
	implements Comparable<MidletVersion>
{
	/** The major version. */
	protected final int major;
	
	/** The minor version. */
	protected final int minor;
	
	/** The release version. */
	protected final int release;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the version.
	 *
	 * @param __v The value to parse.
	 * @throws IllegalArgumentException If there are too many or too little
	 * version fields, they contain illegal charactes, or have an out of range
	 * value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public MidletVersion(String __v)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Initializes the version.
	 *
	 * @param __maj The major version.
	 * @throws IllegalArgumentException If any value is out of range.
	 * @since 2016/10/12
	 */
	public MidletVersion(int __maj)
	{
		this(__maj, 0, 0);
	}
	
	/**
	 * Initializes the version.
	 *
	 * @param __maj The major version.
	 * @param __min The minor version.
	 * @throws IllegalArgumentException If any value is out of range.
	 * @since 2016/10/12
	 */
	public MidletVersion(int __maj, int __min)
	{
		this(__maj, __min, 0);
	}
	
	/**
	 * Initializes the version.
	 *
	 * @param __maj The major version.
	 * @param __min The minor version.
	 * @param __rel The release version.
	 * @throws IllegalArgumentException If any value is out of range.
	 * @since 2016/10/12
	 */
	public MidletVersion(int __maj, int __min, int __rel)
	{
		// {@squirreljme.error AD03 Input version number is out of range, only
		// 0 through 99 are valid. (The major version; The minor version; The
		// release version)}
		if (__maj < 0 || __maj > 99 || __min < 0 || __min > 99 ||
			__rel < 0 || __rel > 99)
			throw new IllegalArgumentException(String.format("AD03 %d %d %d",
				__maj, __min, __rel));
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int compareTo(MidletVersion __o)
	{
		int amaj = this.major, amin = this.minor, arel = this.release;
		int bmaj = __o.major, bmin = __o.minor, brel = __o.release;
		
		// Major first
		if (amaj < bmaj)
			return -1;
		else if (amaj > bmaj)
			return 1;
		
		// Then minor
		if (amin < bmin)
			return -1;
		else if (amin > bmin)
			return 1;
		
		// Then release
		if (arel < brel)
			return -1;
		else if (arel > brel)
			return 1;
		
		// The same
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MidletVersion))
			return false;
		
		// Cast
		MidletVersion o = (MidletVersion)__o;
		return this.major == o.major &&
			this.minor == o.minor &&
			this.release == o.release;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int hashCode()
	{
		return (this.major * 10000) +
			(this.minor * 100) +
			this.release;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.major + "." +
				this.minor + "." + this.release));
		
		// Return it
		return rv;
	}
}


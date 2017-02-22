// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is used to handle version ranges that may be used for dependencies.
 *
 * Version ranges are inclusive.
 *
 * @since 2017/02/22
 */
public final class MidletVersionRange
{
	/** The starting range, inclusive. */
	protected final MidletVersion from;
	
	/** Tne ending range, inclusive. */
	protected final MidletVersion to;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the range inclusively between the two given versions.
	 *
	 * @param __from The source version.
	 * @param __to The destination version.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public MidletVersionRange(MidletVersion __from, MidletVersion __to)
		throws NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Make sure from is always first
		if (__from.compareTo(__to) <= 0)
		{
			this.from = __from;
			this.to = __to;
		}
		
		// Swapped
		else
		{
			this.from = __to;
			this.to = __from;
		}
	}
	
	/**
	 * Parses the version range that is specified in the dependency of JAR
	 * files.
	 *
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the range is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public MidletVersionRange(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MidletVersionRange))
			return false;
		
		// Compare
		MidletVersionRange o = (MidletVersionRange)__o;
		return this.from.equals(o.from) && this.to.equals(o.to);
	}
	
	/**
	 * Returns the start of the range
	 *
	 * @return The range start.
	 * @since 2017/02/22
	 */
	public MidletVersion from()
	{
		return this.from;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public int hashCode()
	{
		return this.to.hashCode() ^ (~this.from.hashCode());
	}
	
	/**
	 * Checks whether the specified version is in range.
	 *
	 * @param __v The version to check.
	 * @return {@code true} if it is in the range.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public boolean inRange(MidletVersion __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the end of the range.
	 *
	 * @return The range end.
	 * @since 2017/02/22
	 */
	public MidletVersion to()
	{
		return this.to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}


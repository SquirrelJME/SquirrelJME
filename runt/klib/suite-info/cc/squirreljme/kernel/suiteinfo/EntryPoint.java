// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.suiteinfo;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents an entry point of a JAR.
 *
 * @since 2017/08/20
 */
public final class EntryPoint
	implements Comparable<EntryPoint>
{
	/** The name of the entry point. */
	protected final String name;
	
	/** The entry point class. */
	protected final String entry;
	
	/** Is this a midlet? */
	protected final boolean ismidlet;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the entry point.
	 *
	 * @param __name The name of the entry point.
	 * @param __entry The class used for entry.
	 * @param __mid Is this a midlet launch?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/20
	 */
	public EntryPoint(String __name, String __entry, boolean __mid)
		throws NullPointerException
	{
		// Check
		if (__name == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.entry = __entry;
		this.ismidlet = __mid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/20
	 */
	@Override
	public int compareTo(EntryPoint __o)
	{
		// Do non-midlets first
		boolean ma = this.ismidlet,
			mb = __o.ismidlet;
		if (ma != mb)
			if (!ma)
				return -1;
			else
				return 1;
		
		// Then the rest
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		return this.entry.compareTo(__o.entry);
	}
	
	/**
	 * Returns the entry point class of the entry point.
	 *
	 * @return The entry point class.
	 * @since 2017/08/20
	 */
	public String entryPoint()
	{
		return this.entry;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof EntryPoint))
			return false;
		
		EntryPoint o = (EntryPoint)__o;
		return this.name.equals(o.name) &&
			this.entry.equals(o.entry) &&
			this.ismidlet == o.ismidlet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/20
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.entry.hashCode() ^
			(this.ismidlet ? 1 : 0);
	}
	
	/**
	 * Is this a MIDlet?
	 *
	 * @return If this is a MIDlet or not.
	 * @since 2017/08/20
	 */
	public boolean isMidlet()
	{
		return this.ismidlet;
	}
	
	/**
	 * Returns the name of the entry point.
	 *
	 * @return The entry point name.
	 * @since 2017/08/20
	 */
	public String name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s (%s: %s)", this.name, (this.ismidlet ? "MIDlet" :
				"Classic"), this.entry)));
		
		return rv;
	}
}


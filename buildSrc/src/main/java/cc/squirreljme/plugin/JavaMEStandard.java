// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

/**
 * Describes a Java ME Standard.
 *
 * @since 2020/02/15
 */
public final class JavaMEStandard
{
	/** The name. */
	protected final String name;
	
	/** The vendor. */
	protected final String vendor;
	
	/** The version. */
	protected final String version;
	
	/**
	 * Defines split standard.
	 *
	 * @param __name The name.
	 * @param __vend The vendor.
	 * @param __vers The version.
	 * @throws NullPointerException If no name was specified.
	 * @since 2020/02/15
	 */
	public JavaMEStandard(String __name, String __vend, String __vers)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("No name specified");
		
		this.name = __name;
		this.vendor = (__vend == null ? "" : __vend);
		this.version = (__vers == null ? "" : __vers);
	}
	
	/**
	 * Defines full standard.
	 *
	 * @param __full The full string.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	public JavaMEStandard(String __full)
		throws IllegalArgumentException, NullPointerException
	{
		if (__full == null)
			throw new NullPointerException("No standard specified.");
		
		int fc = __full.indexOf(';'),
			lc = __full.lastIndexOf(';');
		if (fc < 0 || lc < 0 || fc == lc)
			throw new IllegalArgumentException("Invalid standard string.");
		
		this.name = __full.substring(0, fc).trim();
		this.vendor = __full.substring(fc + 1, lc).trim();
		this.version = __full.substring(lc + 1).trim();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JavaMEStandard))
			return false;
		
		JavaMEStandard o = (JavaMEStandard)__o;
		return this.name.equals(o.name) &&
			this.vendor.equals(o.vendor) &&
			this.version.equals(o.version);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^
			this.vendor.hashCode() ^
			this.version.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final String toString()
	{
		return String.format("%s;%s;%s",
			this.name, this.vendor, this.version);
	}
}


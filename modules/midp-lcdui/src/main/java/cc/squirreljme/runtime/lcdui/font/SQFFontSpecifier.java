// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

/**
 * Specifier for SQF Fonts.
 *
 * @since 2018/12/01
 */
public final class SQFFontSpecifier
	implements Comparable<SQFFontSpecifier>
{
	/** The name of the font. */
	protected final String name;
	
	/** The pixel size of the font. */
	protected final int pixelsize;
	
	/** The hashcode cache. */
	private final int _hashcode;
	
	/**
	 * Initializes the font specifier.
	 *
	 * @param __name The font name.
	 * @param __pxs The pixel size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/01
	 */
	public SQFFontSpecifier(String __name, int __pxs)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.pixelsize = __pxs;
		this._hashcode = __name.hashCode() ^ __pxs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public final int compareTo(SQFFontSpecifier __o)
	{
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		return this.pixelsize - __o.pixelsize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof SQFFontSpecifier))
			return false;
		
		// Different hashcodes will never compare ever
		SQFFontSpecifier o = (SQFFontSpecifier)__o;
		if (this._hashcode != o._hashcode)
			return false;
		
		return this.pixelsize == o.pixelsize &&
			this.name.equals(o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public final int hashCode()
	{
		return this._hashcode;
	}
	
	/**
	 * Returns the file name of the font.
	 *
	 * @return The filename.
	 * @since 2018/12/01
	 */
	public final String toFileName()
	{
		return String.format("%s-%d.sqf", this.name, this.pixelsize);
	}
}


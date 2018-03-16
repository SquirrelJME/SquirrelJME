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
 * This represents the name for a font family.
 *
 * @since 2017/10/20
 */
@Deprecated
public final class FontFamilyName
	implements Comparable<FontFamilyName>
{
	/** The default font. */
	public static final FontFamilyName DEFAULT =
		new FontFamilyName("Default");
	
	/** Dialog font. */
	public static final FontFamilyName DIALOG =
		new FontFamilyName("Dialog");
	
	/** Dialog input font. */
	public static final FontFamilyName DIALOG_INPUT =
		new FontFamilyName("DialogInput");
	
	/** Monospaced font. */
	public static final FontFamilyName MONOSPACED =
		new FontFamilyName("Monospaced");
	
	/** Serif font. */
	public static final FontFamilyName SERIF =
		new FontFamilyName("Serif");
	
	/** Sans Serif font. */
	public static final FontFamilyName SANS_SERIF =
		new FontFamilyName("SansSerif");
	
	/** The name of the font, lowercased. */
	protected final String name;
	
	/**
	 * Initializes the family name.
	 *
	 * @param __n The name to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public FontFamilyName(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Make it lowercase
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __n.length(); i < n; i++)
		{
			char c = __n.charAt(i);
			if (c >= 'A' && c <= 'Z')
				c = (char)((c - 'A') + 'a');
			sb.append(c);
		}
		this.name = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int compareTo(FontFamilyName __o)
	{
		return this.name.compareTo(__o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof FontFamilyName))
			return false;
		
		return this.name.equals(((FontFamilyName)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}


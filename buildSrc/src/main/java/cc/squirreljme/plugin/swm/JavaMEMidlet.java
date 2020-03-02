// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

/**
 * Defines a MIDlet that can be launched.
 *
 * @since 2020/02/15
 */
public final class JavaMEMidlet
{
	/** The title. */
	public final String title;
	
	/** The icon. */
	public final String icon;
	
	/** The main class. */
	public final String mainClass;
	
	/**
	 * Initializes the MIDlet information.
	 *
	 * @param __title The title of the MIDlet.
	 * @param __icon The icon of the MIDlet.
	 * @param __main The main class.
	 * @throws NullPointerException If no title or main class was specified.
	 * @since 2020/02/15
	 */
	public JavaMEMidlet(String __title, String __icon, String __main)
		throws NullPointerException
	{
		if (__title == null || __main == null)
			throw new NullPointerException("No title or main specified.");
		
		this.title = __title;
		this.icon = (__icon == null ? "" : __icon);
		this.mainClass = __main;
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
		
		if (!(__o instanceof JavaMEMidlet))
			return false;
		
		JavaMEMidlet o = (JavaMEMidlet)__o;
		return this.title.equals(o.title) &&
			this.icon.equals(o.icon) &&
			this.mainClass.equals(o.mainClass);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final int hashCode()
	{
		return this.title.hashCode() ^
			this.icon.hashCode() ^
			this.mainClass.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final String toString()
	{
		return String.format("%s, %s, %s",
			this.title, this.icon, this.mainClass);
	}
}


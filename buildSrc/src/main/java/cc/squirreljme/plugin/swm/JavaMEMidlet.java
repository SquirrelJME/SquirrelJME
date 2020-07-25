// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

import java.util.List;

/**
 * Defines a MIDlet that can be launched.
 *
 * @since 2020/02/15
 */
public final class JavaMEMidlet
{
	/** Which MIDlet is to be run? */
	public static final String MIDLET_PROPERTY =
		"squirreljme.midlet";
	
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
	 * @param __icon The icon of the MIDlet, may be {@code null}.
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
	
	/**
	 * Search through the list of MIDlets and also 
	 * 
	 * @param __mids The MIDlets to search within.
	 * @return The MIDlet to be used, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/25
	 */
	public static JavaMEMidlet find(List<JavaMEMidlet> __mids)
		throws NullPointerException
	{
		if (__mids == null)
			throw new NullPointerException("NARG");
		
		// Decode the values it could possibly be
		String string = System.getProperty(JavaMEMidlet.MIDLET_PROPERTY);
		Integer number = null;
		if (string != null)
			try
			{
				number = Integer.parseInt(string, 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// If this is a number...
		if (number != null)
		{
			// Force the selection of no MIDlet (run main class)
			// Even so if there are no MIDlets available
			if (number == -1 || __mids.isEmpty())
				return null;
			
			// If out of range only use the first MIDlet
			if (number < 0 || number >= __mids.size())
				return __mids.get(0);
			
			// Return the exact MIDlet
			return __mids.get(number);
		}
		
		// String based search
		if (string != null)
		{
			// Search for a matching case-insensitive title
			for (JavaMEMidlet midlet : __mids)
				if (string.equalsIgnoreCase(midlet.title))
					return midlet;
			
			// Otherwise construct a new MIDlet entry point
			return new JavaMEMidlet(string, null, string);
		}
		
		// Use the first MIDlet if available, or otherwise stop
		if (!__mids.isEmpty())
			return __mids.get(0);
		return null;
	}
}


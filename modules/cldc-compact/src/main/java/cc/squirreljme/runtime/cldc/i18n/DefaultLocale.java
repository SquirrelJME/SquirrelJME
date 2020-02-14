// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.i18n;

/**
 * This class provides access to the default locale.
 *
 * @since 2018/09/20
 */
public final class DefaultLocale
{
	/** The locale to use for conversion in cases where one is not used. */
	public static final Locale NO_LOCALE =
		new LocaleEnUs();
	
	/** The default locale. */
	private static final Locale _DEFAULT_LOCALE =
		DefaultLocale.__defaultLocale();
	
	/**
	 * Not used.
	 *
	 * @since 2018/09/20
	 */
	private DefaultLocale()
	{
	}
	
	/**
	 * Returns the default locale, if one was not initialized yet then "en-US"
	 * will be used temporarily until one is.
	 *
	 * @return The default locale, this value should not be cached.
	 * @since 2018/09/20
	 */
	public static final Locale defaultLocale()
	{
		Locale rv = DefaultLocale._DEFAULT_LOCALE;
		if (rv == null)
			return DefaultLocale.NO_LOCALE;
		return rv;
	}
	
	/**
	 * Determines the default locale.
	 *
	 * @return The default locale.
	 * @since 2018/09/20
	 */
	private static final Locale __defaultLocale()
	{
		// Use local from system property
		String prop = null;
		try
		{
			prop = System.getProperty("microedition.locale");
		}
		catch (SecurityException e)
		{
		}
		
		// If there is none, default to US
		if (prop == null)
			return new LocaleEnUs();
		
		// Determine the locale to use
		Locale use;
		switch (prop.toLowerCase())
		{
				// Fallback to en-US
			case "en-us":
			default:
				return new LocaleEnUs();
		}
	}
}


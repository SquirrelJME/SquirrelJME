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

import cc.squirreljme.jvm.BuiltInLocale;
import cc.squirreljme.jvm.ConfigRomKey;
import cc.squirreljme.jvm.SystemCall;
import cc.squirreljme.jvm.boot.ConfigEntry;
import java.util.NoSuchElementException;

/**
 * This class provides access to the default locale.
 *
 * @since 2018/09/20
 */
public final class DefaultLocale
{
	/** The cached no locale. */
	private static Locale _noLocale =
		null;
	
	/** The default locale. */
	private static Locale _default =
		null;
	
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
	public static Locale defaultLocale()
	{
		Locale rv = DefaultLocale._default;
		if (rv != null)
			return rv;
		
		// The system could have configured the locale for us (hopefully)
		ConfigEntry locale;
		try
		{
			locale = SystemCall.config(ConfigRomKey.BUILT_IN_LOCALE);
			switch (locale.getInteger())
			{
				case BuiltInLocale.ENGLISH_US:
				default:
					rv = new LocaleEnUs();
					break;
			}
		}
		
		// If none was configured, assume the "no" locale which is enUS
		catch (NoSuchElementException e)
		{
			rv = DefaultLocale.noLocale();
		}
		
		// Cache and use it
		DefaultLocale._default = rv;
		return rv;
	}
	
	/**
	 * Returns the the "no" locale.
	 *
	 * @return The no locale.
	 * @since 2020/05/12
	 */
	public static Locale noLocale()
	{
		Locale rv = DefaultLocale._noLocale;
		
		if (rv == null)
			DefaultLocale._noLocale = (rv = new LocaleEnUs());
		
		return rv;
	}
	
	/**
	 * Returns the built-in encoding for the given string.
	 *
	 * @param __str The string to get the built-in encoding of.
	 * @return The built-in encoding from the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/13
	 */
	public static int toBuiltIn(String __str)
		throws NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		switch (__str)
		{
			case "en-US":		return BuiltInLocale.ENGLISH_US;
			default:			return BuiltInLocale.UNKNOWN;
		}
	}
}


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
		ConfigEntry locale = SystemCall.config(ConfigRomKey.BUILT_IN_LOCALE);
		switch (locale.getInteger())
		{
			case BuiltInLocale.ENGLISH_US:
			default:
				rv = new LocaleEnUs();
				break;
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
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.i18n;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.BuiltInLocaleType;

/**
 * This class provides access to the default locale.
 *
 * @since 2018/09/20
 */
public final class DefaultLocale
{
	/** The locale to use for conversion in cases where one is not used. */
	@SuppressWarnings("RedundantFieldInitialization")
	private static Locale _noLocale =
		null;
	
	/** The default locale. */
	@SuppressWarnings("RedundantFieldInitialization")
	private static Locale _defaultLocale =
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
	 * Returns the built-in locale instance.
	 *
	 * @param __id The {@link BuiltInLocaleType}.
	 * @return The built-in locale.
	 * @since 2020/06/11
	 */
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static Locale builtInLocale(int __id)
	{
		switch (__id)
		{
			case BuiltInLocaleType.UNSPECIFIED:
			case BuiltInLocaleType.ENGLISH_US:
				return new LocaleEnUs();
			
				/* {@squirreljme.error ZZ3v Unknown built-in locale ID. (ID)} */
			default:
				throw new IllegalArgumentException("ZZ3v " + __id);
		}
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
		Locale rv = DefaultLocale._defaultLocale;
		if (rv == null)
			DefaultLocale._defaultLocale = (rv = DefaultLocale.builtInLocale(
				RuntimeShelf.locale()));
		return rv;
	}
	
	/**
	 * Returns the default no-locale which is used for default operations.
	 *
	 * @return The no-locale.
	 * @since 2020/06/11
	 */
	public static Locale noLocale()
	{
		Locale rv = DefaultLocale._noLocale;
		if (rv == null)
			DefaultLocale._noLocale = (rv = new LocaleEnUs());
		return rv;
	}
	
	/**
	 * Returns the string representation of the given locale.
	 *
	 * @param __builtIn The {@link BuiltInLocaleType}.
	 * @return The string representation of this locale.
	 * @throws IllegalArgumentException If the built-in encoding is unknown.
	 * @since 2020/06/11
	 */
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static String toString(int __builtIn)
		throws IllegalArgumentException
	{
		switch (__builtIn)
		{
			case BuiltInLocaleType.ENGLISH_US:	return "en-US";
			
				/* {@squirreljme.error ZZ4a Unknown built-in encoding.
				(The built-in encoding ID)} */
			default:
				throw new IllegalArgumentException("ZZ4a " + __builtIn);
		}
	}
}

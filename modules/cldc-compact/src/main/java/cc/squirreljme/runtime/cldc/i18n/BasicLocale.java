// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.i18n;

/**
 * Basic locale support.
 *
 * @since 2018/09/20
 */
public abstract class BasicLocale
	implements Locale
{
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public char toLowerCase(char __c)
	{
		if ((__c >= 'A' && __c <= 'Z') || (__c >= 0xC0 && __c <= 0xDE))
			return (char)(__c + 0x20);
		return __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/29
	 */
	@Override
	public char toUpperCase(char __c)
	{
		if ((__c >= 'a' && __c <= 'z') || (__c >= 0xE0 && __c <= 0xFE))
			return (char)(__c - 0x20);
		return __c;
	}
}


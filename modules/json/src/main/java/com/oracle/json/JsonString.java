// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * An immutable string value.
 *
 * @since 2014/07/25
 */
@Api
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface JsonString
	extends JsonValue
{
	/**
	 * Returns {@code true} only if the other object is a {@link JsonString}
	 * and the strings values match with this one.
	 *
	 * @param __o The object to compare against.
	 * @return {@code true} if this equals another {@link JsonString}.
	 * @since 2014/07/26
	 */
	@Override
	boolean equals(Object __o);
	
	/**
	 * Returns a character sequence which represents the internal string value.
	 *
	 * @return A character sequence for this specified string.
	 * @since 2014/07/26
	 */
	@Api
	CharSequence getChars();
	
	/**
	 * Returns the string which is the value of this string.
	 *
	 * @return A string.
	 * @since 2014/07/26
	 */
	@Api
	String getString();
	
	/**
	 * Invokes {@code getString().hashCode()}.
	 *
	 * @return The hash code.
	 * @since 2014/07/26
	 */
	@Override
	int hashCode();
}


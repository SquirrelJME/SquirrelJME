// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Represents a locale which is built-in.
 *
 * @since 2020/06/11
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface BuiltInLocaleType
{
	/** Unspecified. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNSPECIFIED =
		0;
	
	/** English, US. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ENGLISH_US =
		1;
}

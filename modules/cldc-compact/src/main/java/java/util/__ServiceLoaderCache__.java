// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;

/**
 * Cache for the service loader.
 *
 * @param <S> The class type.
 * @since 2018/12/06
 */
@KeepAbsolutelyEverything
final class __ServiceLoaderCache__<S>
{
	/** The cache of services. */
	volatile Object[] _cache;
}

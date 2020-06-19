// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Cache for the service loader.
 *
 * @param <S> The class type.
 * @since 2018/12/06
 */
final class __ServiceLoaderCache__<S>
{
	/** The cache of services. */
	volatile Object[] _cache;
}

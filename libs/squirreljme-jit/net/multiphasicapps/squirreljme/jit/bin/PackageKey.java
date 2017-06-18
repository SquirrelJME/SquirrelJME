// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

/**
 * This represents an individual package identifier which is used to associate
 * a class to a given package.
 *
 * There also is a special package which is associated with classes which do not
 * belong to any package.
 *
 * This class is immutable and thread-safe.
 *
 * @since 2017/06/15
 */
public final class PackageKey
{
	/** The special package identifier which only special classes may be in. */
	public static final PackageKey SPECIAL =
		new PackageKey();
}


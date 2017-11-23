// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet;

/**
 * This represents a dependency which may be within the manifest, it is shared
 * with profiles and configurations since they count as dependencies.
 *
 * @since 2017/11/23
 */
public interface ManifestedDependency
{
	/**
	 * Is this an optional dependency?
	 *
	 * @return {@code true} if this is an optional dependency.
	 * @since 2017/11/23
	 */
	public abstract boolean isOptional();
}


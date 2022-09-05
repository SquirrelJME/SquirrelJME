// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.target;

/**
 * Represents that a target variant can exist.
 *
 * @param <V> The type of variant.
 * @since 2022/09/04
 */
public interface HasTargetVariant<V extends TargetVariant>
{
	/**
	 * Returns the available variants.
	 * 
	 * @return The available variants.
	 * @since 2022/09/05
	 */
	V[] variants();
}

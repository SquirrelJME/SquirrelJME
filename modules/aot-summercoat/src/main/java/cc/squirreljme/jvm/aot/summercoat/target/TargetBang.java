// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.target;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class represents the format of a target bang which is similar to
 * GCC's triplets except it is not ambiguous with dashes and such.
 * 
 * The format is {@code arch-family!arch-variant!os!os-variant}, all fields
 * must be specified and additionally if a variant is unknown then {@code none}
 * is used. Note that for architecture variants
 *
 * @see TargetArchitecture
 * @see TargetOperatingSystem
 * @since 2022/09/04
 */
public final class TargetBang
	implements Comparable<TargetBang>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/04
	 */
	@Override
	public int compareTo(TargetBang __b)
	{
		throw Debugging.todo();
	}
}

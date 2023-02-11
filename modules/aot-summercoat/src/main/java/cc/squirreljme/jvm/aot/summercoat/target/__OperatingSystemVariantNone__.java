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
 * Represents the "none" operating system variant.
 *
 * @since 2022/09/05
 */
final class __OperatingSystemVariantNone__
	implements TargetOperatingSystemVariant
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public String name()
	{
		return "NONE";
	}
}

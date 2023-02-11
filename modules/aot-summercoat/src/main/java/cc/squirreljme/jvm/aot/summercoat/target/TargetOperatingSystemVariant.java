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
 * Represents a variant of an operating system.
 *
 * @since 2022/09/04
 */
public interface TargetOperatingSystemVariant
	extends Banglet, TargetVariant
{
	/** The None Variant. */
	TargetOperatingSystemVariant NONE =
		new __OperatingSystemVariantNone__();
}

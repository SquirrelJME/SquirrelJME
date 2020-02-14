// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents anything which has member flags.
 *
 * @since 2018/09/09
 */
public interface HasMemberFlags
	extends HasAccessibleFlags
{
	/**
	 * Returns member flags.
	 *
	 * @return The member flags.
	 * @since 2018/09/09
	 */
	@Override
	public abstract MemberFlags flags();
}


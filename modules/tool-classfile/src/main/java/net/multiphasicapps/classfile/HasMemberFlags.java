// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	MemberFlags flags();
}


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
 * This represents the base for members within classes.
 *
 * @since 2017/09/30
 */
public abstract class Member
	implements HasMemberFlags
{
	/**
	 * Initializes the base member.
	 *
	 * @since 2017/09/30
	 */
	Member()
	{
	}
	
	/**
	 * Returns the name and type of the member.
	 *
	 * @return The member name and type.
	 * @since 2018/09/09
	 */
	public abstract MemberNameAndType nameAndType();
}


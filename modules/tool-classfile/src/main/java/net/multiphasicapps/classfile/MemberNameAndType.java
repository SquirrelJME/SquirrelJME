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
 * This represents something which is a member and has a member name and type.
 *
 * @since 2018/09/09
 */
public interface MemberNameAndType
{
	/**
	 * Returns the name of the member.
	 *
	 * @return The member name.
	 * @since 2018/09/09
	 */
	public abstract MemberName name();
	
	/**
	 * Returns the type of the member.
	 *
	 * @return The member type.
	 * @since 2018/09/09
	 */
	public abstract MemberDescriptor type();
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import net.multiphasicapps.classfile.Identifier;

/**
 * This interface represents anything which can be a member of a class.
 *
 * @since 2018/04/27
 */
public interface MemberSyntax
{
	/**
	 * Returns the modifiers for the member.
	 *
	 * @return The member modifiers.
	 * @since 2018/04/29
	 */
	public abstract ModifiersSyntax modifiers();
	
	/**
	 * Returns the name of the member.
	 *
	 * @return The name of the member.
	 * @since 2018/04/29
	 */
	public abstract Identifier name();
}


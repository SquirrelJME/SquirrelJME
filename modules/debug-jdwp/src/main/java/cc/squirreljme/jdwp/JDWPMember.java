// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents a member field or method of a class.
 *
 * @since 2021/03/14
 */
public interface JDWPMember
	extends JDWPId
{
	/**
	 * Returns the member flag bits.
	 * 
	 * @return The member flag bits.
	 * @since 2021/03/13
	 */
	int debuggerMemberFlags();
	
	/**
	 * Returns the member name.
	 * 
	 * @return The member name.
	 * @since 2021/03/13
	 */
	String debuggerMemberName();
	
	/**
	 * Returns the member type signature.
	 * 
	 * @return The member type signature.
	 * @since 2021/03/13
	 */
	String debuggerMemberType();
}

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
 * Represents a debugged method reference.
 *
 * @since 2021/03/13
 */
public interface JDWPMethod
	extends JDWPMember
{
	/**
	 * Returns the line table for the method.
	 * 
	 * @return The line table or {@code null} if there is none.
	 * @since 2021/03/14
	 */
	int[] debuggerLineTable();
	
	/**
	 * Returns the number of valid locations within the method.
	 * 
	 * @return The number of valid locations.
	 * @since 2021/03/17
	 */
	long debuggerLocationCount();
}

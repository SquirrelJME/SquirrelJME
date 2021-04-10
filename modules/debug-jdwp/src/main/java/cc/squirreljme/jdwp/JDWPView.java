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
 * This is a view of something on JDWP.
 *
 * @since 2021/04/10
 */
public interface JDWPView
{
	/**
	 * Is this a valid object for viewing?
	 * 
	 * @param __what What is being checked?
	 * @return If it is valid or not.
	 * @since 2021/04/10
	 */
	boolean isValid(Object __what);
}

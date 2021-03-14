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
 * Represents a class type.
 *
 * @since 2021/03/13
 */
public interface JDWPClass
	extends JDWPId
{
	/**
	 * Returns the class type of this class.
	 * 
	 * @return The class type.
	 * @since 2021/03/13
	 */
	JDWPClassType debuggerClassType();
}

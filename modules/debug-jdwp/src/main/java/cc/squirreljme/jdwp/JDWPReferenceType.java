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
 * Represents a reference type.
 *
 * @deprecated Use viewers instead.
 * @since 2021/03/14
 */
@Deprecated
public interface JDWPReferenceType
	extends JDWPObjectLike
{
	/**
	 * Returns the class of this object.
	 * 
	 * @return The class type.
	 * @since 2021/03/14
	 */
	JDWPClass debuggerClass();
}

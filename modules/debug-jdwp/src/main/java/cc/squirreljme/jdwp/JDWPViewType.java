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
 * Represents a type.
 *
 * @since 2021/04/10
 */
public interface JDWPViewType
	extends JDWPView
{
	/**
	 * Returns the component type.
	 * 
	 * @param __what Get the component type of what?
	 * @return The component type or {@code null} if this is not an array.
	 * @since 2021/04/11
	 */
	Object componentType(Object __what);
	
	/**
	 * Returns the signature of the given type.
	 * 
	 * @param __what Get the signature of which type?
	 * @return The signature of the given type.
	 * @since 2021/04/11
	 */
	String signature(Object __what);
}

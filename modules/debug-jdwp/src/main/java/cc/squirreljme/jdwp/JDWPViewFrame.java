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
 * This is a view around thread frames.
 *
 * @since 2021/04/11
 */
public interface JDWPViewFrame
	extends JDWPViewValidObject
{
	/**
	 * Returns the class the frame is in.
	 * 
	 * @param __which Which frame are we looking at?
	 * @return The class the frame is in.
	 * @since 2021/04/11
	 */
	Object atClass(Object __which);
	
	/**
	 * Returns the code index the frame is at, typically will be a PC
	 * address.
	 * 
	 * @param __which Which frame are we looking at?
	 * @return The code index the frame is at.
	 * @since 2021/04/11
	 */
	int atCodeIndex(Object __which);
	
	/**
	 * Returns the method index within the given class the frame is at.
	 * 
	 * @param __which Which frame are we looking at?
	 * @return The method index.
	 * @since 2021/04/11
	 */
	int atMethodIndex(Object __which);
}

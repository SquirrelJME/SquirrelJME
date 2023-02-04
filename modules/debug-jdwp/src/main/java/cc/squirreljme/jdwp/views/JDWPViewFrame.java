// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.views;

import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This is a view around thread frames.
 *
 * @since 2021/04/11
 */
@Exported
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
	@Exported
	Object atClass(Object __which);
	
	/**
	 * Returns the code index the frame is at, typically will be a PC
	 * address.
	 * 
	 * @param __which Which frame are we looking at?
	 * @return The code index the frame is at.
	 * @since 2021/04/11
	 */
	@Exported
	long atCodeIndex(Object __which);
	
	/**
	 * Returns the current line index.
	 * 
	 * @param __which Which frame are we looking at?
	 * @return The line index the frame is at or a negative value if not valid.
	 * @since 2021/04/28
	 */
	@Exported
	long atLineIndex(Object __which);
	
	/**
	 * Returns the method index within the given class the frame is at.
	 * 
	 * @param __which Which frame are we looking at?
	 * @return The method index.
	 * @since 2021/04/11
	 */
	@Exported
	int atMethodIndex(Object __which);
	
	/**
	 * Reads the value of a register within a frame.
	 *
	 * @param __which Which frame to read from?
	 * @param __index The index of the register to read from the frame.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/14
	 */
	@Exported
	boolean readValue(Object __which, int __index, JDWPValue __out);
	
	/**
	 * Returns the number of values stored in the frame.
	 * 
	 * @param __which Which frame to get from?
	 * @return The number of values to store in the frame.
	 * @since 2021/04/14
	 */
	@Exported
	int numValues(Object __which);
}

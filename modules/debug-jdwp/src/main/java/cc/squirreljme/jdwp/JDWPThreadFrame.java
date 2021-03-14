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
 * Represents a single thread frame.
 *
 * @since 2021/03/13
 */
public interface JDWPThreadFrame
	extends JDWPId
{
	/**
	 * Returns the class this frame is at.
	 * 
	 * @return The class this frame is at.
	 * @since 2021/03/13
	 */
	JDWPClass debuggerAtClass();
	
	/**
	 * The index this frame is at, the value is implementation defined.
	 * 
	 * @return The implementation defined index, if the adress is not valid
	 * then this must return {@code -1}.
	 * @since 2021/03/13
	 */
	long debuggerAtIndex();
	
	/**
	 * Returns the method this frame is at.
	 * 
	 * @return The method the frame is at.
	 * @since 2021/03/13
	 */
	JDWPMethod debuggerAtMethod();
}

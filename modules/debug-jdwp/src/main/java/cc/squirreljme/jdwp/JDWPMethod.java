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
	extends JDWPId
{
	/**
	 * Returns the method flag bits.
	 * 
	 * @return The method flag bits.
	 * @since 2021/03/13
	 */
	int debuggerMethodFlags();
	
	/**
	 * Returns the method name.
	 * 
	 * @return The method name.
	 * @since 2021/03/13
	 */
	String debuggerMethodName();
	
	/**
	 * Returns the method type signature.
	 * 
	 * @return The method type signature.
	 * @since 2021/03/13
	 */
	String debuggerMethodType();
}

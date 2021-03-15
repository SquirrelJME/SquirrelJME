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
 * Used on any object that can be collected.
 *
 * @since 2021/03/15
 */
public interface JDWPCollectable
	extends JDWPId
{
	/**
	 * Has this been collected?
	 * 
	 * @return If this has been collected?
	 * @since 2021/03/15
	 */
	boolean debuggerIsCollected();
}

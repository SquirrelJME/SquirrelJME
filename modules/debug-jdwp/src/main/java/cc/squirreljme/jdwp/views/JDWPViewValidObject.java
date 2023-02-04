// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.views;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * A viewer over something which has a valid object as its subject.
 *
 * @since 2021/04/11
 */
@Exported
public interface JDWPViewValidObject
	extends JDWPView
{
	/**
	 * Is this a valid object for viewing?
	 * 
	 * @param __which What is being checked?
	 * @return If it is valid or not.
	 * @since 2021/04/10
	 */
	@Exported
	boolean isValid(Object __which);
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.views;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Represents a type that has an instance view.
 *
 * @since 2022/09/24
 */
public interface JDWPViewHasInstance
	extends JDWPViewValidObject
{
	/**
	 * Returns the in virtual machine object instance of the given
	 * representation.
	 * 
	 * @param __which Which virtual representation to get its actual in VM
	 * instance of.
	 * @return The in-VM instance of the given representation.
	 * @since 2022/09/24
	 */
	Object instance(Object __which);
}

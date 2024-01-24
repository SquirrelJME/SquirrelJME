// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.views;

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

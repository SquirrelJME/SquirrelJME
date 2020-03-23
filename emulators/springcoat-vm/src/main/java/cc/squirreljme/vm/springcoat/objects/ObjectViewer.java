// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.objects;

import cc.squirreljme.vm.springcoat.SpringPointer;

/**
 * This is the base interface for any object or address which has a viewer.
 *
 * @since 2020/03/22
 */
public interface ObjectViewer
{
	/**
	 * Returns the pointer to this object.
	 *
	 * @return The object pointer.
	 * @since 2020/03/22
	 */
	SpringPointer pointer();
}

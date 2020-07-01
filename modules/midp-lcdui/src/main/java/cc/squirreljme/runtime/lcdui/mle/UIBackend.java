// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.UIFormShelf;

/**
 * This interface is used as a wrapper around {@link UIFormShelf}, it is
 * implemented by a class and essentially is used to forward calls to the
 * true backing implementation. This is here so that in the event that
 * {@link UIFormShelf} is not supported, that there is a fall-back.
 *
 * @since 2020/06/30
 */
public interface UIBackend
{
	/**
	 * Returns the displays that are attached to the system.
	 * 
	 * @return The displays attached to the system.
	 * @since 2020/07/01
	 */
	UIDisplayInstance[] displays();
	
	/**
	 * Deletes the given form.
	 * 
	 * @param __form The form to delete.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	void formDelete(UIFormInstance __form)
		throws NullPointerException;
	
	/**
	 * Creates a new instance of a form.
	 * 
	 * @return A new form instance.
	 * @since 2020/07/01
	 */
	UIFormInstance formNew();
	
	/**
	 * Shows the given form.
	 * 
	 * @param __display The display to show on.
	 * @param __form The form to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	void displayShow(UIDisplayInstance __display, UIFormInstance __form)
		throws NullPointerException;
}

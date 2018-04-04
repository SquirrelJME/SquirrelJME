// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

/**
 * This represents a form which will consist of various input items as they
 * are used and needed.
 *
 * @since 2018/04/04
 */
public interface UiForm
	extends UiDisplayable, UiTabbedDisplayable
{
	/**
	 * Returns the items that represent this form.
	 *
	 * @return The items representing the form.
	 * @since 2018/04/04
	 */
	public abstract UiFormItems getFormItems();
}


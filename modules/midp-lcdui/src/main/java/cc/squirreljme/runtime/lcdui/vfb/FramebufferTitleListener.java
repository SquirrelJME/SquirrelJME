// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.vfb;

/**
 * This interface is used to notify of any title updates that have been
 * performed.
 *
 * @since 2020/01/17
 */
public interface FramebufferTitleListener
{
	/**
	 * This is called when the title is updated.
	 *
	 * @param __s The title to set.
	 * @since 2020/01/17
	 */
	void titleUpdated(String __s);
}


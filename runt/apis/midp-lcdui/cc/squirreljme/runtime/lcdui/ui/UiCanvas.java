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
 * This represents a canvas which may be displayed, it is mostly drawn on by
 * client code and may potentially be displayed as fullscreen.
 *
 * @since 2018/04/04
 */
public interface UiCanvas
	extends UiDisplayable, UiHasShownCallback, UiInputEvents, UiPaintable
{
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This class acts as the base for classes which can be cleaned up when they
 * are no longer referenced so that the server can operate correctly.
 *
 * @since 2018/03/23
 */
abstract class __Cleanupable__
{
	/** The handle of this item. */
	final int _handle =
		__Queue__.INSTANCE.__register(this);
}


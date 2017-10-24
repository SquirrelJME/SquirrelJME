// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.event;

/**
 * This class is used to manage the queue of events which may be generated on
 * native LCDUI widgets and such. This enables everything about the UI system
 * to be natively serialized as needed.
 *
 * Monitor locks and notifications are performed on the queue object itself.
 *
 * @since 2017/10/24
 */
public final class EventQueue
{
}


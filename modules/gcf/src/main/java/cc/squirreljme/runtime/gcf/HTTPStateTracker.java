// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This is used to track the state of the HTTP connection since it must be
 * shared across many classes for HTTP.
 *
 * @since 2019/05/13
 */
public final class HTTPStateTracker
{
	/** The state of this connection. */
	HTTPState _state =
		HTTPState.SETUP;
}


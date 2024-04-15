// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This is a tracker for the connection state.
 *
 * @since 2019/05/13
 */
public final class ConnectionStateTracker
{
	/** Has the input been closed? */
	boolean _inclosed;
	
	/** Has the output been closed? */
	boolean _outclosed;
}


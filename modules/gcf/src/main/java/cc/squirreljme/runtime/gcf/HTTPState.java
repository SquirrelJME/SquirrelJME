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
 * This represents a state of the HTTP connection.
 *
 * @since 2019/05/12
 */
public enum HTTPState
{
	/** Setup, before a connection is made. */
	SETUP,
	
	/** Connected to remote HTTP server. */
	CONNECTED,
	
	/** Connection is closed. */
	CLOSED,
	
	/** End. */
	;
}


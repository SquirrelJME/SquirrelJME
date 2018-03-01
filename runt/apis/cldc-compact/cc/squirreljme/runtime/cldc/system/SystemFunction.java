// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

/**
 * This class contains the list of functions which are available to the client
 * processes when it needs to interact with the kernel or perform an action.
 *
 * System functions are either local or require access to the kernel. Local
 * ones may be implemented locally, but they could potentially go to the
 * kernel accordingly.
 *
 * @since 2018/02/21
 */
public enum SystemFunction
{	
	/** Client initialization complete. */
	CLIENT_INITIALIZATION_COMPLETE,
	
	/** Current time in milliseconds since UTC. */
	CURRENT_TIME_MILLIS,
	
	/** Exits the current process. */
	EXIT,
	
	/** Hint that garbage collection should be done. */
	GARBAGE_COLLECTION_HINT,
	
	/** Invoke public static void main method of a class. */
	INVOKE_STATIC_MAIN,
	
	/** Returns the current monotonic clock time. */
	NANOTIME,
	
	/** Pipe single byte to stdout or stderr. */
	PIPE_OUTPUT_ZI,
	
	/** Pipe multiple bytes to stdout or stderr. */
	PIPE_OUTPUT_ZABII,
	
	/** Set thread as daemon thread. */
	SET_DAEMON_THREAD,
	
	/** End. */
	;
}


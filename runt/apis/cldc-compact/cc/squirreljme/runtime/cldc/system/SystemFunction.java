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
	
	/** Memory map a boolean array. */
	MEMORY_MAP_BOOLEAN_ARRAY,

	/** Memory map a byte array. */
	MEMORY_MAP_BYTE,

	/** Memory map a char array. */
	MEMORY_MAP_CHARACTER,

	/** Memory map a double array. */
	MEMORY_MAP_DOUBLE,

	/** Memory map a float array. */
	MEMORY_MAP_FLOAT,

	/** Memory map a integer array. */
	MEMORY_MAP_INTEGER,

	/** Memory map a long array. */
	MEMORY_MAP_LONG,

	/** Memory map a short array. */
	MEMORY_MAP_SHORT,

	/** Memory map a String array. */
	MEMORY_MAP_STRING,
	
	/** Returns the current monotonic clock time. */
	NANOTIME,
	
	/** Pipe single byte to stdout or stderr. */
	PIPE_OUTPUT_ZI,
	
	/** Pipe multiple bytes to stdout or stderr. */
	PIPE_OUTPUT_ZABII,
	
	/** Perform a call into a service. */
	SERVICE_CALL,
	
	/** Returns the number of available services. */
	SERVICE_COUNT,
	
	/** Quert which client class implements the service functions. */
	SERVICE_QUERY_CLASS,
	
	/** Query a service index which is available. */
	SERVICE_QUERY_INDEX,
	
	/** Set thread as daemon thread. */
	SET_DAEMON_THREAD,
	
	/** List tasks running on the system. */
	TASK_LIST,
	
	/** Gets the stack trace of a throwable. */
	THROWABLE_GET_STACK,
	
	/** Sets the stack trace in a throwable. */
	THROWABLE_SET_STACK,
	
	/** End. */
	;
	
	/**
	 * Is this function intended to be locally executed?
	 *
	 * This allows certain system calls to become very relaxed on how their
	 * arguments are validated and any exceptions that may be thrown.
	 *
	 * @return If this is a function intended to be executed locally.
	 * @since 2018/03/14
	 */
	public final boolean isLocal()
	{
		switch (this)
		{
			case SET_DAEMON_THREAD:
			case THROWABLE_GET_STACK:
			case THROWABLE_SET_STACK:
				return true;
			
			default:
				return false;
		}
	}
}


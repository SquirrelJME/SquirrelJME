// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

/**
 * This represents a command that is used for the re-recorded interpreter
 * replay data stream.
 *
 * @since 2016/06/01
 */
public enum RRDataCommand
{
	/** Set the Java instructions per second. */
	SET_JIPS(true),
	
	/** End. */
	;
	
	/** Is this command asynchronous? */
	protected final boolean async;
	
	/**
	 * Initializes the data command.
	 *
	 * @param __async Is this an asynchronous command?
	 * @since 2016/06/01
	 */
	private RRDataCommand(boolean __async)
	{
		this.async = __async;
	}
	
	/**
	 * Returns {@code true} if this is an asynchronous command.
	 *
	 * Asynchronous commands when they are encountered on the read stream are
	 * executed immedietly and their verification state is no explicitely set.
	 *
	 * @return {@code true} if this is asynchronous.
	 * @since 2016/06/01
	 */
	public boolean isAsynchronous()
	{
		return this.async;
	}
	
	/**
	 * Returns {@code true} if this is an synchronous command.
	 *
	 * Synchronous commands when they are encountered by the playback system
	 * must be of the same command type on playback. For example if there was
	 * recorded a start of a thread, and a read command was for example the
	 * allocation of a new object, then the replay has desynchronized.
	 *
	 * @return {@code true} if this is synchronous.
	 * @since 2016/06/01
	 */
	public boolean isSynchronous()
	{
		return !this.async;
	}
}


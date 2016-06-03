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

import net.multiphasicapps.squirreljme.terp.InterpreterProcess;

/**
 * This represents a process within the rerecording interpreter which has its
 * own object state.
 *
 * @since 2016/06/03
 */
public class RRProcess
	extends InterpreterProcess
{
	/** The owning rerecording interpreter. */
	protected final RRInterpreter rrinterpreter;
	
	/**
	 * Initializes the process which runs under the rerecording interpreter.
	 *
	 * @param __terp The owning interpreter.
	 * @since 2016/06/03
	 */
	public RRProcess(RRInterpreter __terp)
	{
		super(__terp);
		
		// Set
		RRInterpreter rri = (RRInterpreter)this.interpreter;
		this.rrinterpreter = rri;
		
		// Lock the data stream for synchronization details
		RRDataStream rds = rri.dataStream();
		synchronized (rds)
		{
			// If playing back, process creation must happen
			if (rds.isPlaying())
				throw new Error("TODO");
			
			// Record process creation
			if (rds.isRecording())
				try (RRDataPacket pk = rds.createPacket(
					RRDataCommand.CREATE_PROCESS, 0))
				{
					// It is just noted, so record it
					rds.record(pk);
				}
		}
	}
}


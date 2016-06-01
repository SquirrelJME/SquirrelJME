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

import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;

/**
 * This handles the data stream which is used to read sessions which were
 * previously recorded and then to play them back.
 *
 * @since 2016/05/30
 */
public class RRDataStream
{
	/** The lock to use. */
	protected final Object lock;
	
	/** The owning interpreter. */
	protected final RRInterpreter interpreter;
	
	/** The playback file. */
	private volatile DataInputStream _replay;
	
	/** The recording file. */
	private volatile DataOutputStream _record;
	
	/** The current re-record count. */
	private volatile int _rerecordcount;
	
	/**
	 * Initializes the data stream.
	 *
	 * @param __i The owning interpreter.
	 * @param __lk The interpreter lock.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public RRDataStream(RRInterpreter __i, Object __lk)
		throws NullPointerException
	{
		// Check
		if (__i == null || __lk == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __i;
		this.lock = __lk;
	}
	
	/**
	 * Specifies that the given path should be used as input for a playback
	 * session.
	 *
	 * @param __p The source replay to run, if {@code null} then playback
	 * stops.
	 * @since 2016/05/30
	 */
	public void streamInput(Path __p)
	{
		// The Interpreter
		RRInterpreter terp = this.interpreter;
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Starts recording to the specified output stream.
	 *
	 * @param __p The file to write a recorded session to.
	 * @since 2016/05/30
	 */
	public void streamOutput(Path __p)
	{
		// The Interpreter
		RRInterpreter terp = this.interpreter;
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}


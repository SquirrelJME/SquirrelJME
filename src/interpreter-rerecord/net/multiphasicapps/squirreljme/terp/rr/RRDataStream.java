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
	 * This adjusts the program to start on initial launch.
	 *
	 * @param __cp The {@link ClassPath} to adjust.
	 * @param __mm The The main method to adjust.
	 * @param __args The program arguments to adjust.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public void adjustProgramStart(ClassPath[] __cp, CIMethod[] __mm,
		String[][] __args)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __mm == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}


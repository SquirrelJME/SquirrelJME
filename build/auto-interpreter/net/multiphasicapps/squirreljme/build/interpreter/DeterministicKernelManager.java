// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This is a deterministic kernel manager which is capable of recording and
 * playing back previously recorded sessions.
 *
 * Try to use this for debugging and not cheating in video games.
 *
 * @since 2016/11/02
 */
public class DeterministicKernelManager
	extends AbstractKernelManager
	implements Closeable
{
	/**
	 * Initializes the deterministic kernel manager.
	 *
	 * @param __ai The owning interpreter.
	 * @param __in The input playback stream.
	 * @param __out The output recording stream.
	 * @throws IOException On read/write errors.
	 * @since 2016/11/02
	 */
	public DeterministicKernelManager(AutoInterpreter __ai, Path __in,
		Path __out)
		throws IOException
	{
		super(__ai);
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/02
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/03
	 */
	@Override
	public void runThreads()
		throws InterruptedException
	{
		throw new Error("TODO");
	}
}


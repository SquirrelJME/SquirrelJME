// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.profiler;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes the Java Flight Recorder {@code .jfr} format.
 *
 * @since 2023/08/19
 */
public class JavaFlightRecorderWriter
	implements SnapshotWriter
{
	/** The stream to write to. */
	protected final DataOutputStream out;
	
	/** The snapshot to write to. */
	protected final ProfilerSnapshot snapshot;
	
	/**
	 * Initializes the Java Flight Recorder Writer.
	 *
	 * @param __snapshot The snapshot to write to.
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/19
	 */
	public JavaFlightRecorderWriter(ProfilerSnapshot __snapshot,
		OutputStream __out)
	{
		if (__out == null || __snapshot == null)
			throw new NullPointerException("NARG");
		
		// We always want a data output stream here for binary data
		if (__out instanceof DataOutputStream)
			this.out = (DataOutputStream)__out;
		else
			this.out = new DataOutputStream(__out);
		
		this.snapshot = __snapshot;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/19
	 */
	@Override
	public void write()
		throws IOException
	{
		throw Debugging.todo();
	}
}

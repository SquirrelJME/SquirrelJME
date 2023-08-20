// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.profiler;

import java.io.OutputStream;
import java.util.NoSuchElementException;

/**
 * Represents the format for snapshots.
 *
 * @since 2023/08/19
 */
public enum SnapshotFormat
{
	/** VisualVM Format. */
	NPS,
	
	/** Java Flight Recorder Format. */
	JFR,
	
	/* End. */
	;
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/19
	 */
	@Override
	public final String toString()
	{
		if (this == SnapshotFormat.JFR)
			return "jfr";
		return "nps";
	}
	
	/**
	 * Initializes a snapshot writer.
	 *
	 * @param __snapshot The snapshot to write.
	 * @param __out The stream to write to.
	 * @return The writer.
	 * @since 2023/08/19
	 */
	public final SnapshotWriter writer(ProfilerSnapshot __snapshot,
		OutputStream __out)
	{
		if (this == SnapshotFormat.JFR)
			return new JavaFlightRecorderWriter(__snapshot, __out);
		return new VisualVMWriter(__snapshot, __out);
	}
	
	/**
	 * Locates the format to write.
	 *
	 * @param __str The input string.
	 * @return The format to use.
	 * @throws NoSuchElementException If the format does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/19
	 */
	public static SnapshotFormat of(String __str)
		throws NoSuchElementException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		switch (__str)
		{
			case "nps":
			case "NPS":
				return SnapshotFormat.NPS;
				
			case "jfr":
			case "JFR":
				return SnapshotFormat.JFR;
		}
		
		throw new NoSuchElementException("NSEE " + __str);
	}
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.profiler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * This class contains the parsed node table which defines the structure for
 * each entry within the frame tree.
 *
 * @since 2018/11/11
 */
final class __NodeTable__
{
	/**
	 * Parses the frames and loads into a node table.
	 *
	 * @param __mids The frame location IDs.
	 * @param __fs The input frames to map.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void parse(Map<FrameLocation, Integer> __mids,
		Iterable<ProfiledFrame> __fs)
		throws NullPointerException
	{
		if (__mids == null || __fs == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes the node table to the given stream.
	 *
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void writeTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		DataOutputStream dos = new DataOutputStream(__os);
		
		throw new todo.TODO();
	}
}


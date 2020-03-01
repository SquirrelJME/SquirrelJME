// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

import cc.squirreljme.jvm.boot.io.BinaryBlob;

/**
 * This represents a noted string.
 *
 * @since 2019/12/14
 */
public final class PoolNotedString
{
	/** The blob for the string data. */
	protected final BinaryBlob blob;
	
	/**
	 * Initializes the noted string.
	 *
	 * @param __blob The blob for this string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/14
	 */
	public PoolNotedString(BinaryBlob __blob)
		throws NullPointerException
	{
		if (__blob == null)
			throw new NullPointerException("NARG");
		
		this.blob = __blob;
	}
	
	/**
	 * Returns the blob for this string.
	 *
	 * @return The used blob.
	 * @since 2019/12/14
	 */
	public final BinaryBlob blob()
	{
		return this.blob;
	}
}


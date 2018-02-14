// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

/**
 * This interface represents a resource which is available in the system and
 * provides data that may be optionally compressed.
 *
 * @since 2018/02/13
 */
public interface SystemResource
{
	/** Data is stored using no compression. */
	public static final int COMPRESSION_NONE =
		1;
	
	/** Data is stored using deflate compression. */
	public static final int COMPRESSION_DEFLATE =
		2;
	
	/**
	 * Returns the type of compression that is used on the data.
	 *
	 * @return The compression type for the data.
	 * @since 2018/02/13
	 */
	public abstract int compressionType();
	
	/**
	 * Returns the length of the resource.
	 *
	 * @return The length of the resource.
	 * @since 2018/02/13
	 */
	public abstract int length();
}


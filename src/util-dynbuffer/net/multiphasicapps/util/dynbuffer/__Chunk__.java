// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.dynbuffer;

/**
 * This represents a chunk of data within a buffer.
 *
 * Each chunk initially starts with a byte array of the default size. If bytes
 * are removed or inserted in specific locations within the chunk that cause
 * lots of data to be moved around (above the threshold) then the chunk will
 * be split and still use the same byte array.
 *
 * @since 2016/07/31
 */
final class __Chunk__
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.util.dynbuffer.chunksize=(size) This is used to
	 * specify an alternative size ot be used for the default size of a chunk
	 * within a dynamic buffer.}
	 */
	private static final int _DEFAULT_SIZE =
		Math.max(Integer.getInteger(
			"net.multiphasicapps.util.dynbuffer.chunksize", 128), 1);
	
	/** The owning buffer. */
	protected final DynamicByteBuffer owner;
	
	/** The chunk index, gets adjusted when chunks are added/removed. */
	volatile int _index;
	
	/**
	 * This initializes a blank chunk using the specified buffer as the
	 * owner.
	 *
	 * @param __dbb The byte buffer which owns this chunk.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/01
	 */
	__Chunk__(DynamicByteBuffer __dbb)
		throws NullPointerException
	{
		// Check
		if (__dbb == null)
			throw new NullPointerException("NARG");
		
		this.owner = __dbb;
	}
	
	/**
	 * Returns the end position of this chunk.
	 *
	 * @return The end position.
	 * @since 2016/08/01
	 */
	final int __endPosition()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the physical position associated with the start of the chunk.
	 *
	 * @return The chunk's physical starting position.
	 * @since 2016/08/01
	 */
	final int __position()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the size of this chunk.
	 *
	 * @return The chunk size.
	 * @since 2016/08/01
	 */
	final int __size()
	{
		throw new Error("TODO");
	}
}


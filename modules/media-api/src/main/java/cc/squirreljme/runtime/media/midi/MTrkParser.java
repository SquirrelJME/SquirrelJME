// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayInputStream;

/**
 * This is a parser for MIDI's {@code MTrk} which is a single MIDI track, it
 * is able to read events and wait for the next one accordingly.
 *
 * @since 2022/04/24
 */
public final class MTrkParser
{
	/** The MIDI buffer data. */
	private final byte[] _buffer;
	
	/** The offset into the buffer. */
	private final int _offset;
	
	/** The length of the buffer. */
	private final int _length;
	
	/** The tick division duration of the track. */
	private int _tickDivDuration =
		-1;
	
	/**
	 * Initializes the parser for MIDI {@code MTrk}.
	 * 
	 * @param __b The buffer to read data from.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	public MTrkParser(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._buffer = __b;
		this._offset = __o;
		this._length = __l;
	}
	
	/**
	 * Returns an input stream over the track data.
	 *
	 * @return The input stream used.
	 * @since 2024/02/26
	 */
	public ByteArrayInputStream inputStream()
	{
		return new ByteArrayInputStream(this._buffer, this._offset,
			this._length);
	}
	
	/**
	 * Returns the length of the track in bytes.
	 *
	 * @return The length of the track in bytes.
	 * @since 2024/02/25
	 */
	public int length()
	{
		return this._length;
	}
	
	/**
	 * Returns the total tick division duration for this given track.
	 * 
	 * @return The total tick division duration for this track.
	 * @since 2022/04/25
	 */
	public final int tickDivDuration()
	{
		// Does the duration need to be figured out?
		int deltaDuration = this._tickDivDuration;
		if (deltaDuration < 0)
			this._tickDivDuration =
				(deltaDuration = this.__calculateTickDivDuration());
		
		return deltaDuration;
	}
	
	/**
	 * Calculates the tick division duration of the track.
	 * 
	 * @return The calculated tick division duration of the track.
	 * @since 2022/04/24
	 */
	private int __calculateTickDivDuration()
	{
		throw Debugging.todo();
	}
}

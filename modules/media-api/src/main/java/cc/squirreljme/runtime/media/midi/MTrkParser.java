// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

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
}

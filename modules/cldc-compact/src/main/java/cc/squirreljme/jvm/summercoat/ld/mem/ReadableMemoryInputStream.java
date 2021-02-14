// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an input stream which can read from readable memory.
 *
 * @since 2019/04/21
 */
public final class ReadableMemoryInputStream
	extends InputStream
	implements DataInput, MemoryStream
{
	/** The base read address. */
	protected final int address;
	
	/** Is this being read as little endian? */
	protected final boolean isLittle;
	
	/** The number of bytes that can be read. */
	protected final int length;
	
	/** The input memory. */
	protected final ReadableMemory memory;
	
	/** The current read offset. */
	private int _at;
	
	/**
	 * Initializes the input read memory, the byte order is big endian.
	 * 
	 * @param __mem The memory to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/09
	 */
	public ReadableMemoryInputStream(ReadableMemory __mem)
		throws NullPointerException
	{
		// The address is zero and not __mem.memRegionOffset() because this
		// address is relative to the piece of memory and not the address
		// of that memory
		this(__mem, 0, __mem.memRegionSize(), false);
	}
	
	/**
	 * Initializes the input read memory, the byte order is big endian.
	 * 
	 * @param __mem The memory to read from.
	 * @param __isLittle Write as little endian?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/09
	 */
	public ReadableMemoryInputStream(ReadableMemory __mem, boolean __isLittle)
		throws NullPointerException
	{
		// The address is zero and not __mem.memRegionOffset() because this
		// address is relative to the piece of memory and not the address
		// of that memory
		this(__mem, 0, __mem.memRegionSize(), __isLittle);
	}
	
	/**
	 * Initializes the stream, the byte order is big endian.
	 *
	 * @param __mem The memory.
	 * @param __ad The start address.
	 * @param __ln The length.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public ReadableMemoryInputStream(ReadableMemory __mem, int __ad, int __ln)
		throws NullPointerException
	{
		this(__mem, __ad, __ln, false);
	}
	
	/**
	 * Initializes the stream.
	 *
	 * @param __mem The memory.
	 * @param __ad The start address.
	 * @param __ln The length.
	 * @param __isLittle Write as little endian?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/09
	 */
	public ReadableMemoryInputStream(ReadableMemory __mem, int __ad, int __ln,
		boolean __isLittle)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		this.memory = __mem;
		this.address = __ad;
		this.length = __ln;
		this.isLittle = __isLittle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/04
	 */
	@Override
	public int address()
	{
		return this.address + this._at;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int available()
	{
		return this.length - this._at;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int read()
	{
		// EOF?
		int at = this._at;
		if (at >= this.length)
			return -1;
		
		// Read
		this._at = at + 1;
		return this.memory.memReadByte(this.address + at) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int read(byte[] __b)
		throws NullPointerException
	{
		return this.read(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Used to check bounds
		int at = this._at;
		int length = this.length;
		int address = this.address;
		
		// Read in all bytes
		ReadableMemory memory = this.memory;
		for (int i = 0; i < __l; i++)
		{
			// EOF?
			if (at >= length)
			{
				this._at = at + i;
				return (i == 0 ? -1 : i);
			}
			
			// Read in
			__b[__o++] = (byte)memory.memReadByte(address + at + i);
		}
		
		// Count
		this._at = at + __l;
		return __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public boolean readBoolean()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public byte readByte()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public char readChar()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public double readDouble()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public float readFloat()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void readFully(byte[] __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void readFully(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public int readInt()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@SuppressWarnings("override")
	public String readLine()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public long readLong()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public short readShort()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public String readUTF()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public int readUnsignedByte()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public int readUnsignedShort()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/04
	 */
	@Override
	public int offset()
	{
		return this._at;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public int skipBytes(int __n)
		throws IOException
	{
		throw Debugging.todo();
	}
}


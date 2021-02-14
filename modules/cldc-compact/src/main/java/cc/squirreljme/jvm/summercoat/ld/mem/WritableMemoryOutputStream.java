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
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream which can directly write to memory areas.
 *
 * @since 2019/06/14
 */
public final class WritableMemoryOutputStream
	extends OutputStream
	implements DataOutput, MemoryStream
{
	/** The base write address. */
	protected final int address;
	
	/** Is this being read as little endian? */
	protected final boolean isLittle;
	
	/** The number of bytes that can be written. */
	protected final int length;
	
	/** The output memory. */
	protected final WritableMemory memory;
	
	/** The current write offset. */
	private int _at;
	
	/**
	 * Initializes the stream, the byte order is big endian.
	 *
	 * @param __mem The memory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/09
	 */
	public WritableMemoryOutputStream(WritableMemory __mem)
		throws NullPointerException
	{
		this(__mem, 0, __mem.memRegionSize());
	}
	
	/**
	 * Initializes the stream, the byte order is big endian.
	 *
	 * @param __mem The memory.
	 * @param __isLittle Write as little endian?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/09
	 */
	public WritableMemoryOutputStream(WritableMemory __mem, boolean __isLittle)
		throws NullPointerException
	{
		this(__mem, 0, __mem.memRegionSize(), __isLittle);
	}
	
	/**
	 * Initializes the stream, the byte order is big endian.
	 *
	 * @param __mem The memory.
	 * @param __ad The start address.
	 * @param __ln The length.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/14
	 */
	public WritableMemoryOutputStream(WritableMemory __mem, int __ad, int __ln)
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
	public WritableMemoryOutputStream(WritableMemory __mem, int __ad, int __ln,
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
	 * @since 2021/02/04
	 */
	@Override
	public int offset()
	{
		return this._at;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/14
	 */
	@Override
	public final void close()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/14
	 */
	@Override
	public final void flush()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/14
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		// {@squirreljme.error AE0k Reached end of memory.}
		int at = this._at;
		if (at >= this.length)
			throw new EOFException("AE0k");
		
		// Write
		this.memory.memWriteByte(this.address + at, __b);
		this._at = at + 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/14
	 */
	@Override
	public final void write(byte[] __b)
		throws IOException, NullPointerException
	{
		this.write(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/14
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Needed to check bounds.
		int memLen = this.length;
		int at = this._at;
		int left = memLen - at;
		
		// {@squirreljme.error AE0l Reached end of memory.}
		if (left <= 0)
			throw new EOFException("AE0l");
		
		// Do not write past the bounds
		if (__l > left)
			__l = left;
		
		// Write to memory
		this.memory.memWriteBytes(this.address + at, __b, __o, __l);
		
		// Increase pointer
		this._at = at + __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeBoolean(boolean __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeByte(int __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeBytes(String __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeChar(int __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeChars(String __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeDouble(double __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeFloat(float __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeInt(int __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeLong(long __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeShort(int __v)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/09
	 */
	@Override
	public void writeUTF(String __v)
		throws IOException
	{
		throw Debugging.todo();
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class buffers characters into an internal buffer so that they can be
 * written in bulk to the underlying stream. This increases performance and
 * is a more optimal operation because it writes multiple characters at once
 * instead of writing them one at a time.
 *
 * The default buffer size in SquirrelJME is very small due to potential memory
 * limitations.
 *
 * @since 2018/09/18
 */
@ImplementationNote("The buffer size is undefined in the class library, " +
	"therefor due to potential memory limitations the buffer size should " +
	"be kept small.")
@Api
public class BufferedWriter
	extends Writer
{
	/**
	 * This buffer size is kept small by default since having a large buffer
	 * for a memory contrained system is not really that important and will
	 * pretty much only be used by console output. So as such to prevent
	 * wasting memory with streams that will not likely be used for most
	 * programs (MIDlets) or they will be outputting characters anyway.
	 *
	 * It should be noted that Java SE uses a buffer size of 8KiB.
	 */
	private static final int _DEFAULT_BUFFER_SIZE =
		128;
	
	/** The writer to buffer at. */
	private final Writer _out;
	
	/**
	 * Initializes the buffered writer to the target writer using the default
	 * buffer size.
	 *
	 * @param __out The writer to buffer to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/18
	 */
	@Api
	public BufferedWriter(Writer __out)
		throws NullPointerException
	{
		this(__out, BufferedWriter._DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Initializes the buffered writer to the target writer using the given
	 * buffer size.
	 *
	 * @param __out The writer to buffer to.
	 * @param __bs The number of characters to store in the buffer before
	 * flushing.
	 * @throws IllegalArgumentException If the buffer size is less than or
	 * equal to zero.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/18
	 */
	@Api
	public BufferedWriter(Writer __out, int __bs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ0h Requested a buffer size that is zero or
		negative. (The requested buffer size)} */
		if (__bs <= 0)
			throw new IllegalArgumentException(String.format("ZZ0h %d", __bs));
		
		throw Debugging.todo();
	}
	
	/**
	 * Closes the stream, it is flushed first.
	 *
	 * @throws IOException On write errors.
	 * @since 2018/09/18
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/18
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes the value of {@code System.getProperty("line.separator")} to the
	 * stream.
	 *
	 * @throws IOException On write errors.
	 * @since 2018/09/18
	 */
	@Api
	public void newLine()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/18
	 */
	@Override
	public void write(int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	/**
	 * This normally stores characters into the internal buffer, however if
	 * the length of the output is at least the size of the buffer then it
	 * will flush the buffer then write all the input characters.
	 *
	 * {@inheritDoc}
	 * @since 2018/09/18
	 * @param __b
	 * @param __o
	 * @param __l
	 */
	@Override
	public void write(char[] __b, int __o, int __l)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/18
	 * @param __s
	 * @param __o
	 * @param __l
	 */
	@Override
	public void write(String __s, int __o, int __l)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
}


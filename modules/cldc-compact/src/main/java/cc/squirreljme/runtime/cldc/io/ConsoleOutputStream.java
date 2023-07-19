// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This provides an output stream which writes to a console file descriptor.
 *
 * @see StandardPipeType
 * @see TerminalShelf
 * @since 2018/12/08
 */
public final class ConsoleOutputStream
	extends OutputStream
	implements Appendable
{
	/** the file descriptor to write to. */
	protected final PipeBracket pipe;
	
	/** Is the output always flushed? */
	protected final boolean alwaysFlush;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __fd The {@link StandardPipeType} to write to.
	 * @param __alwaysFlush Is the pipe to be always flushed?
	 * @throws IllegalArgumentException If the pipe is not valid.
	 * @deprecated Use {@link PipeBracket} and otherwise.
	 * @since 2018/12/08
	 */
	@Deprecated
	public ConsoleOutputStream(int __fd, boolean __alwaysFlush)
		throws IllegalArgumentException
	{
		try
		{
			this.pipe = TerminalShelf.fromStandard(__fd);
			this.alwaysFlush = __alwaysFlush;
		}
		catch (MLECallError e)
		{
			/* {@squirreljme.error ZZ5g The standard pipe is not valid.} */
			throw new IllegalArgumentException("ZZ5g", e);
		}
	}
	
	/**
	 * Writes to the given output pipe.
	 * 
	 * @param __pipe The pipe to write to.
	 * @param __alwaysFlush Is the pipe to be always flushed?
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/19
	 */
	public ConsoleOutputStream(PipeBracket __pipe, boolean __alwaysFlush)
		throws NullPointerException
	{
		if (__pipe == null)
			throw new NullPointerException("NARG");
		
		this.pipe = __pipe;
		this.alwaysFlush = __alwaysFlush;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/15
	 */
	@Override
	public Appendable append(CharSequence __seq)
		throws IOException
	{
		if (__seq == null)
			return this.append("null", 0, 4);
		return this.append(__seq, 0, __seq.length());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/15
	 */
	@Override
	public Appendable append(CharSequence __seq, int __s, int __e)
		throws IOException
	{
		CharSequence trueSeq = (__seq == null ? "null" : __seq);
		
		for (int i = __s; i < __e; i++)
			this.append(trueSeq.charAt(i));
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/15
	 */
	@Override
	public Appendable append(char __c)
		throws IOException
	{
		// Encode bytes to whatever the system encoding is
		Encoder encoder = CodecFactory.defaultEncoder();
		int maxSeq = encoder.maximumSequenceLength();
		byte[] enc = new byte[maxSeq];
		int n = encoder.encode(__c, enc, 0, maxSeq);
		
		for (int i = 0; i < n; i++)
			this.write(enc[i]);
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/15
	 */
	@Override
	public void close()
		throws IOException
	{
		/* {@squirreljme.error ZZ3y Could not close the terminal output.} */
		if (TerminalShelf.close(this.pipe) < 0)
			throw new IOException("ZZ3y");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public void flush()
		throws IOException
	{
		/* {@squirreljme.error ZZ05 Could not flush the console.} */
		if (TerminalShelf.flush(this.pipe) < 0)
			throw new IOException("ZZ05");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/16
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		/* {@squirreljme.error ZZ06 Error writing to console.} */
		if (TerminalShelf.write(this.pipe, __b) < 0)
			throw new IOException("ZZ06");
		
		// Always flush to force printing
		if (this.alwaysFlush)
			this.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/05
	 */
	@Override
	public void write(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ07 Error writing to console.} */
		if (TerminalShelf.write(this.pipe, __b, 0, __b.length) < 0)
			throw new IOException("ZZ07");
		
		// Always flush to force printing
		if (this.alwaysFlush)
			this.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/05
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		/* {@squirreljme.error ZZ08 Error writing to console.} */
		if (TerminalShelf.write(this.pipe, __b, __o, __l) < 0)
			throw new IOException("ZZ08");
		
		// Always flush to force printing
		if (this.alwaysFlush)
			this.flush();
	}
}


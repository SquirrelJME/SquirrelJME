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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Encoder;

/**
 * This class maps character based output to a byte based output, encoding
 * any written characters to the output.
 * 
 * @since 2022/07/12
 */
@Api
public class OutputStreamWriter
	extends Writer
{
	/** The encoder used to encode bytes. */
	private final Encoder _encoder;
	
	/** The stream to write to. */
	private final OutputStream _out;
	
	/** The internal working buffer for the encoder. */
	private final byte[] _workBuf;
	
	/**
	 * Initializes the writer, using the default character set.
	 * 
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	@Api
	public OutputStreamWriter(OutputStream __out)
		throws NullPointerException
	{
		this(__out, CodecFactory.defaultEncoder());
	}
	
	/**
	 * Initializes the writer.
	 * 
	 * @param __out The stream to write to.
	 * @param __charset The character set to use for encoding.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2022/07/12
	 */
	@Api
	public OutputStreamWriter(OutputStream __out, String __charset)
		throws NullPointerException, UnsupportedEncodingException
	{
		this(__out, CodecFactory.encoder(__charset));
	}
	
	/**
	 * Initializes the writer, using the specified internal encoder.
	 * 
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	@Api
	private OutputStreamWriter(OutputStream __out, Encoder __encoder)
		throws NullPointerException
	{
		if (__out == null || __encoder == null)
			throw new NullPointerException("NARG");
		
		this._out = __out;
		this._encoder = __encoder;
		this._workBuf = new byte[__encoder.maximumSequenceLength()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public void close()
		throws IOException
	{
		// Forward
		this._out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Forward
		this._out.flush();
	}
	
	/**
	 * Returns the name of the encoding.
	 * 
	 * @return The encoding name.
	 * @since 2022/07/12
	 */
	@Api
	public String getEncoding()
	{
		return this._encoder.encodingName();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public void write(int __c)
		throws IOException
	{
		this.__write((char)__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public void write(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 ||
			(__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		for (int i = 0; i < __l; i++)
			this.__write(__c[__o + i]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public void write(String __str, int __off, int __len)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __str.length())
			throw new IndexOutOfBoundsException("IOOB");
		
		for (int i = 0; i < __len; i++)
			this.__write(__str.charAt(__off + i));
	}
	
	/**
	 * Writes the given character.
	 * 
	 * @param __c The character to write.
	 * @throws IOException On write errors.
	 * @since 2022/07/12
	 */
	private void __write(char __c)
		throws IOException
	{
		byte[] workBuf = this._workBuf;
		int len = this._encoder.encode(__c, workBuf, 0, workBuf.length);
		
		// Should never happen!
		if (len < 0)
			throw Debugging.oops();
		
		this._out.write(workBuf, 0, len);
	}
}


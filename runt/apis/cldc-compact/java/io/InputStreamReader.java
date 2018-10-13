// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Decoder;

/**
 * This is a reader which adapts to an input stream and decodes the input
 * bytes into characters.
 *
 * @since 2018/10/13
 */
public class InputStreamReader
	extends Reader
{
	/** The input source. */
	private final InputStream _in;
	
	/** The decoder to use. */
	private final Decoder _decoder;
	
	/**
	 * Initializes the reader from the given input stream using the default
	 * encoding.
	 *
	 * @param __in The input byte source.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	public InputStreamReader(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this._in = __in;
		this._decoder = CodecFactory.defaultDecoder();
	}
	
	/**
	 * Initializes the reader from the given input stream using the default
	 * encoding.
	 *
	 * @param __in The input byte source.
	 * @param __enc The encoding to decode.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/10/13
	 */
	public InputStreamReader(InputStream __in, String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this._in = __in;
		this._decoder = CodecFactory.decoder(__enc);
	}
	
	@Override
	public void close()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public String getEncoding()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int read()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public int read(char[] __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public boolean ready()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
}


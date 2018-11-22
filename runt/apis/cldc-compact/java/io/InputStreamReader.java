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
	
	/** The input read storage. */
	private final byte[] _store;
	
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
		
		Decoder d;
		this._decoder = (d = CodecFactory.defaultDecoder());
		this._store = new byte[d.maximumSequenceLength()];
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
		
		Decoder d;
		this._decoder = (d = CodecFactory.decoder(__enc));
		this._store = new byte[d.maximumSequenceLength()];
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IOException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		InputStream in = this._in;
		Decoder decoder = this._decoder;
		byte[] store = this._store;
		int storelen = 0,
			declimit = store.length;
		
		int rv = 0,
			baseo = __o;
		for (int o = __o; rv < __l;)
		{
			// {@squirreljme.error ZZ0j Read of input byte sequence exceeded
			// the maximum specified sequence length. (The store length)}
			if (storelen >= declimit)
				throw new IOException("ZZ2l " + storelen);
			
			// Read byte from input stream
			int brc = in.read(store, storelen, 1);
			
			// Reached EOF from the input bytes
			if (brc < 0)
			{
				// No characters were read, so this is a complete EOF
				// However if there were characters sitting in the output we
				// need to return those
				if (storelen <= 0)
					return (rv > 0 ? rv : brc);
				
				// Try to decode whatever was read, if it ends up not being
				// valid then just use the replacement character because it
				// probably got chopped off
				int cha = decoder.decode(store, 0, storelen);
				if (cha >= 0)
					__c[o++] = (char)cha;
				else
					__c[o++] = (char)0xFFFD;
				rv++;
				
				// There could have been characters placed before this, so
				// this should be at least 1
				return rv;
			}
			
			// Increment the store length since bytes were read
			storelen++;
			
			// Try to decode a character, if it decodes to a valid character we
			// just store that
			int cha = decoder.decode(store, 0, storelen);
			if (cha >= 0)
			{
				__c[o++] = (char)cha;
				storelen = 0;
				rv++;
			}
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public boolean ready()
		throws IOException
	{
		// If the number of available bytes is at least the maximum sequence
		// length then we can read a single character without blocking
		return this._in.available() >= this._store.length;
	}
}


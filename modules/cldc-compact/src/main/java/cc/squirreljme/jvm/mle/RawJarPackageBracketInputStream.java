// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an input stream over the raw representation of a
 * {@link JarPackageBracket} which might not be in the form of a JAR or any
 * actual readable data.
 *
 * @since 2022/03/04
 */
@SquirrelJMEVendorApi
public class RawJarPackageBracketInputStream
	extends InputStream
{
	/** The given library. */
	@SquirrelJMEVendorApi
	protected final JarPackageBracket jar;
	
	/** The size of the JAR. */
	@SquirrelJMEVendorApi
	protected final int jarSize;
	
	/** Single byte read, as only bulk read is supported. */
	private final byte[] _singleByte =
		new byte[1];
	
	/** The current read position. */
	private int _readPos;
	
	/**
	 * Initializes the input stream to read the raw JAR.
	 * 
	 * @param __jar The JAR to read raw data from.
	 * @throws IOException If reading from the given JAR in its
	 * raw data form is not possible.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/04
	 */
	@SquirrelJMEVendorApi
	public RawJarPackageBracketInputStream(JarPackageBracket __jar)
		throws IOException, NullPointerException
	{
		this(__jar, 0);
	}
	
	/**
	 * Initializes the input stream to read the raw JAR.
	 * 
	 * @param __jar The JAR to read raw data from.
	 * @param __offset The initial read offset.
	 * @throws IndexOutOfBoundsException If the offset is out of bounds.
	 * @throws IOException If reading from the given JAR in its
	 * raw data form is not possible.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/09
	 */
	@SquirrelJMEVendorApi
	public RawJarPackageBracketInputStream(JarPackageBracket __jar,
		int __offset)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ3u The specified JAR cannot be accessed
		directly. (The JAR path)} */
		int jarSize = JarPackageShelf.rawSize(__jar);
		if (jarSize < 0)
			throw new IOException("ZZ3u " +
				JarPackageShelf.libraryPath(__jar));
		
		/* {@squirreljme.error ZZ4j Invalid offset into direct JAR.} */
		if (__offset < 0 || __offset > jarSize)
			throw new IndexOutOfBoundsException("ZZ4j");
		
		// Set for later
		this.jar = __jar;
		this.jarSize = jarSize;
		this._readPos = __offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/04
	 */
	@Override
	public int available()
		throws IOException
	{
		return this.jarSize - this._readPos;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/04
	 */
	@Override
	public int read()
		throws IOException
	{
		// Keep trying to read a single byte
		byte[] singleByte = this._singleByte;
		for (;;)
		{
			// Try reading byte
			int read = this.read(singleByte, 0, 1);
			
			// EOF?
			if (read < 0)
				return -1;
			
			// Use the given byte assuming it was read
			if (read != 0)
				return singleByte[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/04
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// At EOF?
		int readPos = this._readPos;
		int jarSize = this.jarSize;
		if (readPos >= jarSize)
			return -1;
		
		// How many bytes can actually be read?
		int limit = Math.min(Math.max(0, jarSize - readPos), __l);
		
		// Read in the JAR data, note that since this could be a filesystem
		// read this can be shorter than we actually request. However, we still
		// cannot read past the end of the Jar, hence the limit
		int count = JarPackageShelf.rawData(this.jar,
			readPos, __b, __o, limit);
		
		// EOF?
		if (count < 0)
		{
			this._readPos = this.jarSize;
			return -1;
		}
		
		// Move the read counter up
		readPos += count;
		this._readPos = readPos;
		
		// Did we read EOF?
		if (count == 0 && readPos >= jarSize)
			return -1;
		return count;
	}
}

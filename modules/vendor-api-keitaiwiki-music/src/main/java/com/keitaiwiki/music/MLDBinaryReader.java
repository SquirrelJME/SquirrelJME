// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

/**
 * Utility class for reading binary data
 */
class MLDBinaryReader
{
	/**
	 * Backing data store
	 */
	final byte[] data;
	
	/**
	 * Length of current segment
	 */
	final int length;
	
	/**
	 * Current input offset
	 */
	int offset;
	
	/**
	 * Offset of start of current segment
	 */
	final int start;
	
	/**
	 * Constructor
	 */
	MLDBinaryReader(byte[] data, int start, int length)
	{
		this.data = data;
		this.length = length;
		this.offset = start;
		this.start = start;
	}
	
	/**
	 * Read a byte array
	 */
	byte[] bytes(int length)
	{
		if (this.offset + length > this.start + this.length)
			throw new RuntimeException("Unexpected EOF.");
		byte[] ret = new byte[length];
		System.arraycopy(this.data, this.offset, ret, 0, length);
		this.offset += length;
		return ret;
	}
	
	/**
	 * Determine whether the stream has reached its end
	 */
	boolean isEOF()
	{
		return this.offset == this.start + this.length;
	}
	
	/**
	 * Produce a new Reader to access a subset of this one
	 */
	MLDBinaryReader reader(int length)
	{
		MLDBinaryReader ret = new MLDBinaryReader(this.data, this.offset,
			length);
		this.skip(length);
		return ret;
	}
	
	/**
	 * Advance the input
	 */
	void skip(int length)
	{
		if (this.offset + length > this.start + this.length)
			throw new RuntimeException("Unexpected EOF.");
		this.offset += length;
	}
	
	/**
	 * Read a 16-bit unsigned integer
	 */
	int u16()
	{
		int ret = this.u8() << 8;
		return ret | this.u8();
	}
	
	/**
	 * Read a 32-bit unsigned integer
	 */
	int u32()
	{
		int ret = this.u16() << 16;
		if (ret < 0)
			throw new RuntimeException("Unsupported U32 value.");
		return ret | this.u16();
	}
	
	/**
	 * Read an 8-bit unsigned integer
	 */
	int u8()
	{
		if (this.offset == this.start + this.length)
			throw new RuntimeException("Unexpected EOF.");
		return this.data[this.offset++] & 0xFF;
	}
	
}

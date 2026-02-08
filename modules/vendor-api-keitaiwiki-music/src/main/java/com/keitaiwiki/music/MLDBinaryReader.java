// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Keitai Wiki Community Music Implementation
//     Originally written and contributed by Guy Perfect
//     Continued maintenance and upkeep by SquirrelJME/Stephanie Gawroriski
// ---------------------------------------------------------------------------
// This specific file is under the given license:
// This is free and unencumbered software released into the public domain.
// 
// Anyone is free to copy, modify, publish, use, compile, sell, or
// distribute this software, either in source code form or as a compiled
// binary, for any purpose, commercial or non-commercial, and by any
// means.
// 
// In jurisdictions that recognize copyright laws, the author or authors
// of this software dedicate any and all copyright interest in the
// software to the public domain. We make this dedication for the benefit
// of the public at large and to the detriment of our heirs and
// successors. We intend this dedication to be an overt act of
// relinquishment in perpetuity of all present and future rights to this
// software under copyright law.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
// 
// For more information, please refer to <https://unlicense.org/>
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

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
	 * Offset of start of current segment
	 */
	final int start;
	
	/**
	 * Current input offset
	 */
	int offset;
	
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
		ObjectShelf.arrayCopy(this.data, this.offset, ret, 0, length);
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

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
import javax.microedition.media.MediaException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Utility class for reading MLD binary data
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
class MLDBinaryReader
{
	/** Backing data store. */
	@SquirrelJMEVendorApi
	final byte[] data;

	/** Length of current data segment. */
	@SquirrelJMEVendorApi
	final int length;

	/** Offset of start of current data segment. */
	@SquirrelJMEVendorApi
	final int start;

	/** Current input offset. */
	@SquirrelJMEVendorApi
	int offset;

	/**
	 *
	 * @param __data
	 * @param __start
	 * @param __length
	 * @throws ArrayIndexOutOfBoundsException If {@code __start} is negative or
	 * larger than {@code __data.length}, {@code __length} is negative, or if
	 * {@code ((__start + __length) > __data.length)}.
	 * @throws NullPointerException If {@code __data} is {@code null};
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MLDBinaryReader(@NotNull byte[] __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __start,
		@Range(from = 0, to = Integer.MAX_VALUE) int __length)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");

		if (__start < 0 || __start > __data.length || __length < 0 ||
			(__start + __length) > __data.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		this.data = __data;
		this.length = __length;
		this.offset = __start;
		this.start = __start;
	}

	/**
	 * Reads an array of bytes.
	 *
	 * @param __length The amount of bytes to read.
	 * @throws ArrayIndexOutOfBoundsException If {@code __length} is a value
	 * such that {@code (this.offset + __length > this.start + this.length)}.
	 * @return An array containing the bytes that were read.
	 * @since 2025/05/05
	 */
	@NotNull
	@SquirrelJMEVendorApi
	byte[] bytes(
		@Range(from = 0, to = Integer.MAX_VALUE) int __length)
		throws ArrayIndexOutOfBoundsException
	{
		if (this.offset + __length > this.start + this.length)
			throw new ArrayIndexOutOfBoundsException("Unexpected EOF.");

		byte[] ret = new byte[__length];
		ObjectShelf.arrayCopy(this.data, this.offset, ret, 0, __length);
		this.offset += __length;
		return ret;
	}

	/**
	 * Determine whether the stream has reached its end.
	 *
	 * @return If the stream has reached its end.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	boolean isEOF()
	{
		return this.offset == this.start + this.length;
	}

	/**
	 * Create a new {@link MLDBinaryReader} that accesses a subset of the data
	 * in this one.
	 *
	 * @param __length The maximum amount of bytes that the new reader is able
	 * to access, starting from this one's current data offset.
	 * @return A new {@link MLDBinaryReader} instance.
	 * @throws ArrayIndexOutOfBoundsException If {@code __length} is a value
	 * such that {@code (this.offset + __length > this.start + this.length)}.
	 * @since 2025/05/05
	 */
	@NotNull
	@SquirrelJMEVendorApi
	MLDBinaryReader reader(
		@Range(from = 0, to = Integer.MAX_VALUE) int __length)
		throws ArrayIndexOutOfBoundsException
	{
		if (this.offset + __length > this.start + this.length)
			throw new ArrayIndexOutOfBoundsException("Unexpected EOF.");

		MLDBinaryReader ret = new MLDBinaryReader(this.data, this.offset,
			__length);

		this.skip(__length);
		return ret;
	}

	/**
	 * Advances the input data by the given amount of bytes.
	 *
	 * @param __length The amount of bytes to skip.
	 * @throws ArrayIndexOutOfBoundsException If {@code __length} is a value
	 * such that {@code (this.offset + __length > this.start + this.length)}.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void skip(
		@Range(from = 0, to = Integer.MAX_VALUE) int __length)
		throws ArrayIndexOutOfBoundsException
	{
		if (this.offset + __length > this.start + this.length)
			throw new ArrayIndexOutOfBoundsException("Unexpected EOF.");

		this.offset += __length;
	}

	/**
	 * Reads a 16-bit unsigned integer comprised of the data array's next two
	 * bytes.
	 *
	 * @return A 16-bit unsigned integer that was read from the data array.
	 * @throws ArrayIndexOutOfBoundsException If the reader is already at EOF
	 * when this method is called.
	 * @throws MediaException If the read value is negative.
	 * @since 2025/05/05
	 */
	@Range(from = 0, to = 0xFFFF)
	@SquirrelJMEVendorApi
	int u16()
		throws ArrayIndexOutOfBoundsException, MediaException
	{
		int ret = this.u8() << 8;
		return ret | this.u8();
	}

	/**
	 * Reads a 32-bit unsigned integer comprised of the data array's next four
	 * bytes.
	 *
	 * @return A 32-bit unsigned integer that was read from the data array.
	 * @throws ArrayIndexOutOfBoundsException If the reader is already at EOF
	 * when this method is called.
	 * @throws MediaException If the read value is negative.
	 * @since 2025/05/05
	 */
	@Range(from = 0, to = 0xFFFFFFFF)
	@SquirrelJMEVendorApi
	int u32()
		throws ArrayIndexOutOfBoundsException, MediaException
	{
		int ret = this.u16() << 16;
		if (ret < 0)
			throw new MediaException("Unsupported U32 value.");

		return ret | this.u16();
	}

	/**
	 * Reads an 8-bit unsigned integer.
	 *
	 * @return An 8-bit unsigned integer that was read from the data array.
	 * @throws ArrayIndexOutOfBoundsException If the reader is already at EOF
	 * when this method is called.
	 * @since 2025/05/05
	 */
	@Range(from = 0, to = 0xFF)
	@SquirrelJMEVendorApi
	int u8()
		throws ArrayIndexOutOfBoundsException
	{
		if (this.offset == this.start + this.length)
			throw new ArrayIndexOutOfBoundsException("Unexpected EOF.");

		return this.data[this.offset++] & 0xFF;
	}
}

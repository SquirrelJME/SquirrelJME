// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Utilities to use for image parsing.
 *
 * @since 2021/12/08
 */
public final class ImageParseUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2021/12/08
	 */
	private ImageParseUtils()
	{
	}
	
	/**
	 * Reads a big-endian signed three-byte integer.
	 * 
	 * @param __in The stream to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/08
	 */
	public static int readBigSignedThree(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return ImageParseUtils.signExtendThree(
			ImageParseUtils.readBigUnsignedThree(__in));
	}
	
	/**
	 * Reads a big-endian unsigned three-byte integer.
	 * 
	 * @param __in The stream to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/08
	 */
	@SuppressWarnings("MagicNumber")
	public static int readBigUnsignedThree(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
			
		return (__in.readUnsignedByte() << 16) |
			(__in.readUnsignedByte() << 8) |
			__in.readUnsignedByte();
	}
	
	/**
	 * Reads a little-endian signed three-byte integer.
	 * 
	 * @param __in The stream to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/08
	 */
	public static int readLittleSignedThree(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return ImageParseUtils.signExtendThree(
			ImageParseUtils.readLittleUnsignedThree(__in));
	} 
	
	/**
	 * Reads a little-endian unsigned three-byte integer.
	 * 
	 * @param __in The stream to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/08
	 */
	@SuppressWarnings("MagicNumber")
	public static int readLittleUnsignedThree(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
			
		return __in.readUnsignedByte() |
			(__in.readUnsignedByte() << 8) |
			(__in.readUnsignedByte() << 16);
	}
	
	/**
	 * Sign extends the three byte value.
	 * 
	 * @param __v The value to extend.
	 * @return The sign extended three byte value.
	 * @since 2021/12/08
	 */
	@SuppressWarnings("MagicNumber")
	public static int signExtendThree(int __v)
	{
		if ((__v & 0x800000) != 0)
			return __v | 0xFF_000000;
		return __v;
	}
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * These are utility methods to write to config space.
 *
 * @since 2019/06/14
 */
public final class ConfigRomWriter
{
	/**
	 * Not used.
	 *
	 * @since 2019/06/14
	 */
	private ConfigRomWriter()
	{
	}
	
	/**
	 * Writes raw byte data.
	 *
	 * @param __dos The output stream.
	 * @param __opt The option to write.
	 * @param __b The value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/14
	 */
	public static final void writeData(DataOutputStream __dos, int __opt,
		byte[] __b)
		throws IOException, NullPointerException
	{
		if (__dos == null || __b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AE0e Attempt to write very large configuration
		// item. (The length)}
		int len = __b.length;
		if (len >= 65536)
			throw new IOException("AE0e " + len);
		
		// Key, value, and the data
		__dos.writeShort(__opt);
		__dos.writeShort(len);
		__dos.write(__b, 0, len);
	}
	
	/**
	 * Writes key/value.
	 *
	 * @param __dos The output stream.
	 * @param __opt The option to write.
	 * @param __k The key.
	 * @param __v The value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/14
	 */
	public static final void writeKeyValue(DataOutputStream __dos, int __opt,
		String __k, String __v)
		throws IOException, NullPointerException
	{
		if (__dos == null || __k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Write data area
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream xdos = new DataOutputStream(baos))
		{
			xdos.writeUTF(__k);
			xdos.writeUTF(__v);
			
			// Write in
			ConfigRomWriter.writeData(__dos, __opt, baos.toByteArray());
		}
	}
	
	/**
	 * Writes the specified integer.
	 *
	 * @param __dos The output stream.
	 * @param __opt The option to write.
	 * @param __v The value to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/14
	 */
	public static final void writeInteger(DataOutputStream __dos, int __opt,
		int __v)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Write data area
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream xdos = new DataOutputStream(baos))
		{
			xdos.writeInt(__v);
			
			// Write in
			ConfigRomWriter.writeData(__dos, __opt, baos.toByteArray());
		}
	}
	
	/**
	 * Writes the specified string.
	 *
	 * @param __dos The output stream.
	 * @param __opt The option to write.
	 * @param __v The string to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/14
	 */
	public static final void writeString(DataOutputStream __dos, int __opt,
		String __v)
		throws IOException, NullPointerException
	{
		if (__dos == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Write data area
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream xdos = new DataOutputStream(baos))
		{
			xdos.writeUTF(__v);
			
			// Write in
			ConfigRomWriter.writeData(__dos, __opt, baos.toByteArray());
		}
	}
	
	/**
	 * Writes the specified strings.
	 *
	 * @param __dos The output stream.
	 * @param __opt The option to write.
	 * @param __v The strings to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/14
	 */
	public static final void writeStrings(DataOutputStream __dos, int __opt,
		String... __v)
		throws IOException, NullPointerException
	{
		if (__dos == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Write data area
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream xdos = new DataOutputStream(baos))
		{
			// Write string length
			int n = __v.length;
			xdos.writeShort(n);
			
			// Write actual strings
			for (int i = 0; i < n; i++)
				xdos.writeUTF(__v[i]);
			
			// Write in
			ConfigRomWriter.writeData(__dos, __opt, baos.toByteArray());
		}
	}
}


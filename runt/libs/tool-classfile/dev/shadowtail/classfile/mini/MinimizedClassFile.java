// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This contains the minimized class file which is a smaller format of the
 * {@link net.multiphasicapps.classfile.ClassFile} that is more optimized to
 * virtual machines for usage.
 *
 * @since 2019/03/10
 */
public final class MinimizedClassFile
{
	/**
	 * Decodes and returns the minimized representation of the class file.
	 *
	 * @param __is The stream to decode from.
	 * @return The resulting minimized class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	public static final MinimizedClassFile decode(InputStream __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Initialize byte array with guess to how many bytes can be read
		byte[] mcdata;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
			Math.max(4096, __is.available())))
		{
			// Copy data
			byte[] buf = new byte[512];
			for (;;)
			{
				int rc = __is.read(buf);
				
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Finish off
			mcdata = baos.toByteArray();
		}
		
		// Decode now
		return MinimizedClassFile.decode(mcdata);
	}
	
	/**
	 * Decodes and returns the minimized representation of the class file.
	 *
	 * @param __is The bytes to decode from.
	 * @return The resulting minimized class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public static final MinimizedClassFile decode(byte[] __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Read class header
		MinimizedClassHeader header = MinimizedClassHeader.decode(
			new ByteArrayInputStream(__is));
		
		// Read constant pool
		MinimizedPool pool = MinimizedPool.decode(header.poolcount,
			__is, header.pooloff, header.poolsize);
		
		// Read static and instance fields
		MinimizedField[] sfields = MinimizedField.decodeFields(
				header.sfcount, pool, __is, header.sfoff, header.sfsize),
			ifields = MinimizedField.decodeFields(
				header.ifcount, pool, __is, header.ifoff, header.ifsize);
		
		// Read static and instance methods
		MinimizedMethod[] smethods = MinimizedMethod.decodeMethods(
				header.smcount, pool, __is, header.smoff, header.smsize),
			imethods = MinimizedMethod.decodeMethods(
				header.imcount, pool, __is, header.imoff, header.imsize);
		
		throw new todo.TODO();
	}
}


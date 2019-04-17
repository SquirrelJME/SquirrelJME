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
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
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
	/** Header. */
	public final MinimizedClassHeader header;
	
	/** Pool. */
	public final MinimizedPool pool;
	
	/** Static fields. */
	private final MinimizedField[] _sfields;
	
	/** Instance fields. */
	private final MinimizedField[] _ifields;
	
	/** Static methods. */
	private final MinimizedMethod[] _smethods;
	
	/** Instance methods. */
	private final MinimizedMethod[] _imethods;
	
	/**
	 * Initializes the minimized class file.
	 *
	 * @param __h The header.
	 * @param __p The pool.
	 * @param __sf Static fields.
	 * @param __if Instance fields.
	 * @param __sm Static methods.
	 * @param __im Instance methods.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public MinimizedClassFile(MinimizedClassHeader __h, MinimizedPool __p,
		MinimizedField[] __sf, MinimizedField[] __if,
		MinimizedMethod[] __sm, MinimizedMethod[] __im)
		throws NullPointerException
	{
		if (__h == null || __p == null || __sf == null || __if == null ||
			__sm == null || __im == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.header = __h;
		this.pool = __p;
		this._sfields = (__sf = __sf.clone());
		this._ifields = (__if = __if.clone());
		this._smethods = (__sm = __sm.clone());
		this._imethods = (__im = __im.clone());
		
		// Check for nulls
		for (Object[] va : new Object[][]{__sf, __if, __sm, __im})
			for (Object v : va)
				if (v == null)
					throw new NullPointerException("NARG");
	}
	
	/**
	 * Returns the names of all the interfaces.
	 *
	 * @return The interface names.
	 * @since 2019/04/17
	 */
	public final ClassNames interfaceNames()
	{
		return this.pool.<ClassNames>get(this.header.classints,
			ClassNames.class);
	}
	
	/**
	 * Returns the minimized constant pool.
	 *
	 * @return The minimized constant pool.
	 * @since 2019/04/17
	 */
	public final MinimizedPool pool()
	{
		return this.pool;
	}
	
	/**
	 * Returns the super name of the current class.
	 *
	 * @return The class super name or {@code null} if there is none.
	 * @since 2019/04/17
	 */
	public final ClassName superName()
	{
		int id = this.header.classsuper;
		return (id == 0 ? null :
			this.pool.<ClassName>get(id, ClassName.class));
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The name of the current class.
	 * @since 2019/04/17
	 */
	public final ClassName thisName()
	{
		return this.pool.<ClassName>get(
			this.header.classname, ClassName.class);
	}
	
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
		
		// {@squirreljme.error JC45 Length of class file does not match
		// length of array. (The file length; The array length)}
		int fsz = header.filesize;
		if (fsz != __is.length)
			throw new InvalidClassFormatException("JC45 " + fsz +
				" " + __is.length);
		
		// {@squirreljme.error JC46 End of file magic number is invalid.
		// (The read magic number)}
		int endmagic;
		if (MinimizedClassHeader.END_MAGIC_NUMBER !=
			(endmagic = (((__is[fsz - 4] & 0xFF) << 24) |
			((__is[fsz - 3] & 0xFF) << 16) |
			((__is[fsz - 2] & 0xFF) << 8) |
			(__is[fsz - 1] & 0xFF))))
			throw new InvalidClassFormatException(
				String.format("JC46 %08x", endmagic));
		
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
		
		// Build final class
		return new MinimizedClassFile(header, pool,
			sfields, ifields, smethods, imethods);
	}
}


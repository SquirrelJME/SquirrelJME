// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

import cc.squirreljme.jvm.ConfigRomKey;
import cc.squirreljme.jvm.ConfigRomType;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is the writer which is used to write configuration items which
 * configure the virtual machine start parameters and otherwise.
 *
 * The keys which can be written are of {@link ConfigRomKey}.
 *
 * Configuration ROMs are always big-endian.
 *
 * Entries have the following format and are always rounded at 4 bytes:
 *  - byte {@link ConfigRomType}.
 *  - byte {@link ConfigRomKey}.
 *  - unsigned short DataSize.
 *  - byte... Data.
 *
 * The final entry is the end marker, where all the entry data is zero.
 *
 * @since 2020/04/11
 */
public final class ConfigWriter
{
	/** The size of the end marker in bytes. */
	private static final int _END_MARKER_SIZE =
		4;
	
	/** The maximum size of entries. */
	private static final int _MAX_ENTRY_SIZE =
		65535;
	
	/** The stream of bytes to write the config to. */
	@SuppressWarnings("resource")
	protected final ByteArrayOutputStream out =
		new ByteArrayOutputStream(1024);
	
	/** Is this little endian? */
	protected final boolean littleEndian;
	
	/**
	 * Initializes the configuration writer.
	 *
	 * @param __littleEndian Is this little endian?
	 * @since 2020/04/11
	 */
	public ConfigWriter(boolean __littleEndian)
	{
		this.littleEndian = __littleEndian;
	}
	
	/**
	 * Returns the number of bytes stored in the configuration.
	 *
	 * @return The byte count size of the configuration.
	 * @since 2020/04/11
	 */
	public int byteCount()
	{
		// The end marker is written when toByteArray() is called, so we
		// must account for that data
		return this.out.size() + ConfigWriter._END_MARKER_SIZE;
	}
	
	/**
	 * Returns the byte array which contains the configuration data.
	 *
	 * @return The byte array containing the configuration data.
	 * @since 2020/04/11
	 */
	public final byte[] toByteArray()
	{
		try (ByteArrayOutputStream rv = new ByteArrayOutputStream(
			this.out.size() + ConfigWriter._END_MARKER_SIZE))
		{
			this.writeTo(rv);
			
			return rv.toByteArray();
		}
		catch (IOException e)
		{
			// {@squirreljme.error ZZ46 Could not return byte array of output
			// data.}
			throw new RuntimeException("ZZ46", e);
		}
	}
	
	/**
	 * Writes the config to the given output stream.
	 *
	 * @param __dest The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/12
	 */
	private void writeTo(OutputStream __dest)
		throws IOException, NullPointerException
	{
		if (__dest == null)
			throw new NullPointerException("NARG");
		
		// Write entire data
		this.out.writeTo(__dest);
		
		// Write end section
		for (int i = 0; i < ConfigWriter._END_MARKER_SIZE; i++)
			__dest.write(0);
	}
	
	/**
	 * Writes raw byte data to the output.
	 *
	 * @param __type The type of value.
	 * @param __key The configuration key.
	 * @param __b The byte data.
	 * @throws IllegalArgumentException If the type and/or key are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/12
	 */
	public final void write(int __type, int __key, byte... __b)
		throws IllegalArgumentException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3Z Invalid config type. (The type)}
		if (__type <= ConfigRomType.END || __type >= ConfigRomType.NUM_TYPES)
			throw new IllegalArgumentException("ZZ3Z " + __type);
		
		// {@squirreljme.error ZZ40 Invalid config key. (The key)}
		if (__key <= ConfigRomKey.END || __key >= ConfigRomKey.NUM_OPTIONS)
			throw new IllegalArgumentException("ZZ40 " + __key);
		
		// {@squirreljme.error ZZ44 Entry size exceeds limit. (The entry size)}
		int blen = __b.length,
			wlen = (blen + 3) & (~3);
		if (wlen > ConfigWriter._MAX_ENTRY_SIZE)
			throw new IllegalArgumentException("ZZ44 " + wlen);
		
		// Write header data
		ByteArrayOutputStream out = this.out;
		out.write(__type);
		out.write(__key);
		
		// Size of chunk, is clipped to byte naturally
		out.write(wlen >>> 8);
		out.write(wlen);
		
		// Write config value data
		out.write(__b, 0, __b.length);
		
		// All entries are padded to four bytes
		while ((blen++) < wlen)
			out.write(0);
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	public final void writeBoolean(int __id, boolean __v)
	{
		this.write(ConfigRomType.BOOLEAN, __id,
			(byte)(__v ? 1 : 0));
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __k The mapping key.
	 * @param __v The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/11
	 */
	public final void writeKeyValuePair(int __id, String __k, String __v)
		throws NullPointerException
	{
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos))
		{
			dos.writeUTF(__k);
			dos.writeUTF(__v);
			dos.writeByte(0);
			
			this.write(ConfigRomType.KEY_VALUE_PAIR, __id,
				baos.toByteArray());
		}
		catch (IOException e)
		{
			// {@squirreljme.error ZZ41 Could not write key/value pair.}
			throw new RuntimeException("ZZ41", e);
		}
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	@SuppressWarnings("MagicNumber")
	public final void writeInteger(int __id, int __v)
	{
		this.write(ConfigRomType.INTEGER, __id,
			(byte)(__v >>> 24),
			(byte)(__v >>> 16),
			(byte)(__v >>> 8),
			(byte)(__v));
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	@SuppressWarnings("MagicNumber")
	public final void writeLong(int __id, long __v)
	{
		this.write(ConfigRomType.LONG, __id,
			(byte)(__v >>> 48),
			(byte)(__v >>> 40),
			(byte)(__v >>> 32),
			(byte)(__v >>> 30),
			(byte)(__v >>> 24),
			(byte)(__v >>> 16),
			(byte)(__v >>> 8),
			(byte)(__v));
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/11
	 */
	public final void writeUtf(int __id, String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos))
		{
			dos.writeUTF(__v);
			dos.writeByte(0);
			
			this.write(ConfigRomType.UTF, __id,
				baos.toByteArray());
		}
		catch (IOException e)
		{
			// {@squirreljme.error ZZ42 Could not write UTF value.}
			throw new RuntimeException("ZZ42", e);
		}
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The values.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/11
	 */
	public final void writeUtfList(int __id, String... __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos))
		{
			// Config items are limited in size, so the lists should not be
			// this long?
			int n = __v.length;
			dos.writeShort(n);
			
			for (String s : __v)
			{
				dos.writeUTF(s);
				dos.writeByte(0);
			}
			
			// Write ending byte for NUL
			dos.writeByte(0);
			
			this.write(ConfigRomType.UTF_LIST, __id,
				baos.toByteArray());
		}
		catch (IOException e)
		{
			// {@squirreljme.error ZZ43 Could not write UTF list.}
			throw new RuntimeException("ZZ43", e);
		}
	}
}

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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;

/**
 * This is the writer which is used to write configuration items which
 * configure the virtual machine start parameters and otherwise.
 *
 * The keys which can be written are of {@link ConfigRomKey}.
 *
 * @since 2020/04/11
 */
public final class ConfigWriter
{
	/** The stream of bytes to write the config to. */
	@SuppressWarnings("resource")
	protected final ByteArrayOutputStream byteStream =
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
		throw Debugging.todo();
	}
	
	/**
	 * Returns the byte array which contains the configuration data.
	 *
	 * @return The byte array containing the configuration data.
	 * @since 2020/04/11
	 */
	public final byte[] toByteArray()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __k The mapping key.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	public final void writeKeyValuePair(int __id, String __k, String __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	public final void writeInteger(int __id, int __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	public final void writeLong(int __id, long __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The value.
	 * @since 2020/04/11
	 */
	public final void writeUtf(int __id, String __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes the specified value.
	 *
	 * @param __id The {@link ConfigRomKey}.
	 * @param __v The values.
	 * @since 2020/04/11
	 */
	public final void writeUtfList(int __id, String... __v)
	{
		throw Debugging.todo();
	}
}

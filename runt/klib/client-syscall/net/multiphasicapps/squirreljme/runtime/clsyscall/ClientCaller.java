// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.clsyscall;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;

/**
 * This is a system caller which uses a basic input stream and a basic output
 * stream to communicate with the kernel to provide system call support.
 *
 * Not all functionality is supported and as such still requires a native
 * implementation.
 *
 * @since 2017/12/31
 */
public abstract class ClientCaller
	extends SystemCaller
{
	/** The packet stream used to communicate with the kernel. */
	protected final PacketStream stream;
	
	/** Mapped service cache. */
	private final Map<String, String> _svcache =
		new HashMap<>();
	
	/**
	 * Initializes the client caller.
	 *
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public ClientCaller(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.stream = new PacketStream(__in, __out,
			new __ResponseHandler__(new WeakReference<>(this)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemProgramInstallReport installProgram(
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemTask launchTask(SystemProgram __program,
		String __mainclass, int __perms, String... __props)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemProgram[] listPrograms(int __typemask)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemTask[] listTasks(boolean __incsys)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final String mapService(String __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		// Cache the service so IPC calls are reduced
		Map<String, String> svcache = this._svcache;
		synchronized (svcache)
		{
			// This allows null to be cached as needed
			if (svcache.containsKey(__sv))
				return svcache.get(__sv);
			
			byte[] rv = this.stream.send(PacketTypes.MAP_SERVICE,
				__stringToBytes(__sv));
			String str = (rv == null || rv.length == 0 ? null :
				__bytesToString(rv));
			
			// Cache and return the string
			svcache.put(__sv, str);
			return str;
		}
	}
	
	/**
	 * Tells the remote end that the initialization has been complete.
	 *
	 * @since 2018/01/01
	 */
	public final void sendInitializationComplete()
	{
		this.stream.send(PacketTypes.INITIALIZATION_COMPLETE, new byte[0]);
	}
	
	/**
	 * Converts byte array to string.
	 *
	 * @param __b The byte array to convert.
	 * @return The converted string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	static final String __bytesToString(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new String(__b, "utf-8");
		}
		
		// {@squirreljme.error AR08 Encoding from UTF-8 should be supported.}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("AR08", e);
		}
	}
	
	/**
	 * Converts a String to a byte array.
	 *
	 * @param __s The string to convert.
	 * @return The resulting string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	static final byte[] __stringToBytes(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return __s.getBytes("utf-8");
		}
		
		// {@squirreljme.error AR07 Encoding to UTF-8 should be supported.}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("AR07", e);
		}
	}
}


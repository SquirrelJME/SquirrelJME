// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This is the base class for all instance of emulators.
 *
 * @since 2016/07/30
 */
public abstract class Emulator
	implements Closeable, Runnable
{
	/** Empty array. */
	private static final String[] _EMPTY_STRINGS =
		new String[0];
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Where display output is to be written. */
	protected final DisplayOutput displayout;
	
	/** Processes that are running in the emulator. */
	private final List<Process> _processes =
		new ArrayList<>();
	
	/** Mounted volumes. */
	private final Map<String, Volume> _mounted =
		new LinkedHashMap<>();
	
	/** Reverse volume name lookup. */
	private final Map<Volume, String> _revmount =
		new LinkedHashMap<>();
	
	/**
	 * Initializes an emulator.
	 *
	 * @param __do The display output to use.
	 * @since 2016/07/30
	 */
	public Emulator(DisplayOutput __do)
	{
		// The display output always exists.
		this.displayout = (__do == null ? new DefaultDisplayOutput() : __do);
	}
	
	/**
	 * Internally mounts the specified volume to the given identifier.
	 *
	 * @param __id The identifier of the volume.
	 * @param __v The volume for file access.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	protected abstract void internalMountVolume(String __id, Volume __v)
		throws IOException, NullPointerException;
	
	/**
	 * Resolves the path of a file in the virtual volume (which uses ZIP
	 * compatible file names) to one that is valid on the filesystem.
	 *
	 * The file to be resolved need not actually exist.
	 *
	 * @param __v The volume to get the real path for.
	 * @param __sp The path in the volume to resolve a file for, the input
	 * path must not be absolute.
	 * @return The resolved path to the file as it appears to the emulated
	 * system.
	 * @throws IOException On read errors or if the path could not be resolved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	protected abstract String internalResolvePath(Volume __v, String __sp)
		throws IOException, NullPointerException;
	
	/**
	 * Internally creates a new process.
	 *
	 * @param __env The environment to use, this a multiple of two and are
	 * in key/value pairs.
	 * @param __args The arguments to the process, this is system dependent.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	protected abstract Process internalStartProcess(String[] __env,
		String[] __args)
		throws IOException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the name of an input volume.
	 *
	 * @param __v The volume to get the name for.
	 * @return The name of the volume or {@code null} if it was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final String getVolumeName(Volume __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Volume, String> revmount = this._revmount;
		synchronized (this.lock)
		{
			return revmount.get(__v);
		}
	}
	
	/**
	 * Mounts the given volume.
	 *
	 * @param __id The ID the volume should have in the target system.
	 * @param __v The volume for file accessing.
	 * @throws IOException On read/write errors or if the volume could not be
	 * mounted.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final void mountVolume(String __id, Volume __v)
		throws IOException, NullPointerException
	{
		// Check
		if (__id == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Normalize the volume name
		__id = __normalizeVolumeID(__id);
		
		// Lock
		Map<String, Volume> mounted = this._mounted;
		Map<Volume, String> revmount = this._revmount;
		synchronized (this.lock)
		{
			// {@squirreljme.error AR01 The specified volume identifier is
			// already in use. (The volume identifier)}
			Volume v = mounted.get(__id);
			String i = revmount.get(__v);
			if (v != null || i != null)
				throw new IOException(String.format("AR01 %s", __id));
			
			// Internally mount the volume
			internalMountVolume(__id, __v);
			
			// Add volume
			mounted.put(__id, __v);
			revmount.put(__v, __id);
		}
	}
	
	/**
	 * Resolves the path of a file in the virtual volume (which uses ZIP
	 * compatible file names) to one that is valid on the filesystem.
	 *
	 * The file to be resolved need not actually exist.
	 *
	 * @param __v The volume to get the real path for.
	 * @param __sp The path in the volume to resolve a file for, the input
	 * path must not be absolute.
	 * @return The resolved path to the file as it appears to the emulated
	 * system.
	 * @throws IOException On read errors or if the path could not be resolved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final String resolvePath(Volume __v, String __sp)
		throws IOException, NullPointerException
	{
		// Check
		if (__v == null || __sp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Volume, String> revmount = this._revmount;
		synchronized (this.lock)
		{
			// {@squirreljme.error AR02 Cannot resolve the given path to a
			// volume because it is not mounted. (The path)}
			if (revmount.get(__v) == null)
				throw new IOException(String.format("AR02 %s", __sp));
			
			// Internal
			return internalResolvePath(__v, __sp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final void run()
	{
		// Infinite loop
		List<Process> processes = this._processes;
		for (;;)
			synchronized (this.lock)
			{
				// Stop?
				if (processes.isEmpty())
					return;
				
				throw new Error("TODO");
			}
	}
	
	/**
	 * Starts the given process using the given environment and set of
	 * arguments.
	 *
	 * @param __env The environment to use, the array is a multiple of two
	 * and appear in key/value pairs.
	 * @param __args The arguments to the process, the first argument is the
	 * name of the executable to be ran (if applicable).
	 * @return The created process.
	 * @throws IOException On read/write errors.
	 * @since 2016/07/30
	 */
	public final Process startProcess(String[] __env, String... __args)
		throws IOException
	{
		// Use empty array if these are not set
		if (__env == null)
			__env = _EMPTY_STRINGS;
		if (__args == null)
			__args = _EMPTY_STRINGS;
		
		// Lock
		List<Process> processes = this._processes;
		synchronized (this.lock)
		{
			// Create process
			Process rv = internalStartProcess(__env, __args);
			
			// Add it to the process list
			processes.add(rv);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Normalizes the volume ID.
	 *
	 * @param __id The volume ID.
	 * @return The normalized form of it.
	 * @since 2016/07/30
	 */
	private static String __normalizeVolumeID(String __id)
		throws NullPointerException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Check all characters first
		boolean donorm = false;
		int n = __id.length();
		for (int i = 0; i < n; i++)
		{
			// Normalize the given character?
			char c = __id.charAt(i);
			donorm |= (!((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') ||
				(c == '_')));
		}
		
		// Is fine, keep
		if (!donorm)
			return __id;
		
		// Normalize it
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
		{
			char c = __id.charAt(i);
			if (!((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') ||
				(c == '_')))
				sb.append('_');
			else
				sb.append(c);
		}
		
		// Build
		return sb.toString();
	}
}


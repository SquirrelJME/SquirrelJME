// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import dev.shadowtail.classfile.mini.MinimizedClassFile;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This contains the state of the entire bootstrap, its memory and all of the
 * classes which may be loaded accordingly.
 *
 * @since 2019/09/14
 */
public final class BootstrapState
{
	/** Class information which has been loaded. */
	private final Map<ClassName, LoadedClassInfo> _classinfos=
		new LinkedHashMap<>();
	
	/** Reference to self. */
	private final Reference<BootstrapState> _selfref =
		new WeakReference<>(this);
	
	/**
	 * Finds the class which uses the given name.
	 *
	 * @param __cl The class name to find.
	 * @return The loaded class information.
	 * @throws InvalidClassFormatException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final LoadedClassInfo findClass(String __cl)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return this.findClass(new ClassName(__cl));
	}
	
	/**
	 * Finds the class which uses the given name.
	 *
	 * @param __cl The class name to find.
	 * @return The loaded class information.
	 * @throws InvalidClassFormatException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final LoadedClassInfo findClass(ClassName __cl)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BC0c Could not find the specified class.
		// (The class name)}
		LoadedClassInfo rv = this._classinfos.get(__cl);
		if (rv == null)
			throw new InvalidClassFormatException("BC0c " + __cl);
		
		return rv;
	}
	
	/**
	 * Loads the class file information.
	 *
	 * @param __b The class file data.
	 * @param __pos The position of the class file in ROM.
	 * @return The loaded class information
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final LoadedClassInfo loadClassFile(byte[] __b, int __pos)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Decode it
		MinimizedClassFile cf = MinimizedClassFile.decode(__b);
		
		// Store loaded for later boot usage
		LoadedClassInfo rv;
		if (this._classinfos.put(cf.thisName(),
			(rv = new LoadedClassInfo(cf, __pos, this._selfref))) != null)
		{
			// {@squirreljme.error BC0b Class file has already been loaded.
			// (The class name)}
			throw new IllegalStateException("BC0b " + cf.thisName());
		}
		
		return rv;
	}
}


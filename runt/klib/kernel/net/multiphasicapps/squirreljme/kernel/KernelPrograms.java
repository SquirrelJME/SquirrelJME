// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramControlKey;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * This class is used to manage the programs which are available for usage.
 *
 * There should always be a program with the type of
 * {@link KernelProgramType#SYSTEM} in the first index slot. The first program
 * is used to determine what the system natively supports.
 *
 * @since 2017/12/14
 */
public abstract class KernelPrograms
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** Programs which are available for usage. */
	private final List<KernelProgram> _programs =
		new ArrayList<>();
	
	/**
	 * Initializes the program manager.
	 *
	 * @since 2017/12/14
	 */
	protected KernelPrograms()
	{
	}
	
	/**
	 * Specifies that the input ZIP be JIT compiled and installed.
	 *
	 * The implementation of this method must not register the program, it
	 * just needs to return one. The method which calls this method will handle
	 * registration.
	 *
	 * @param __info Installation information.
	 * @return The result of compilation and installation.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	protected abstract KernelProgramInstallReport accessJitAndInstall(
		KernelProgramInstallInfo __info)
		throws NullPointerException;
	
	/**
	 * Installs the specified JAR file into the program list.
	 *
	 * @param __by The task which is installing the given program.
	 * @param __b The bytes which make up the JAR file.
	 * @param __o The offset into the byte array.
	 * @param __l The number of bytes which are part of the JAR.
	 * @return The resulting installed program.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the task is not permitted to install
	 * programs.
	 * @since 2017/12/28
	 */
	public final KernelProgramInstallReport install(KernelTask __by,
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
			SecurityException
	{
		if (__by == null || __b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		// {@squirreljme.error ZZ0o The specified task is not permitted to
		// install new programs. (The task installing a program)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.INSTALL_PROGRAM))
			throw new SecurityException(
				String.format("ZZ0o %s", __by));
		
		// Defensive copy of the input array so that it is not modified as
		// it is processed by the kernel
		byte[] copy = new byte[__l];
		System.arraycopy(__b, __o, copy, 0, __l);
		
		// Setup and run installer
		List<KernelProgram> programs = this._programs;
		synchronized (this.lock)
		{
			return new __ProgramInstaller__(this, programs, copy, 0, __l).
				run();
		}
	}
	
	/**
	 * Returns the list of programs which are available.
	 *
	 * @param __by The task requesting the program list.
	 * @param __typemask A mask which is used to filter programs of a given
	 * type.
	 * @return An array containing the programs under the specified mask.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the operation is not permitted.
	 * @since 2017/12/11
	 */
	public final KernelProgram[] list(KernelTask __by, int __typemask)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0f The specified task is not permitted to
		// list available programs. (The task requesting the program list)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.LIST_PROGRAMS))
			throw new SecurityException(
				String.format("ZZ0f %s", __by));
		
		List<KernelProgram> rv = new ArrayList<>();
		
		// Go through registered programs and find matches
		List<KernelProgram> programs = this._programs;
		synchronized (this.lock)
		{
			for (KernelProgram p : programs)
				try
				{
					if ((p.type(__by) & __typemask) != 0)
						rv.add(p);
				}
				catch (SecurityException e)
				{
				}
		}
		
		return rv.<KernelProgram>toArray(new KernelProgram[rv.size()]);
	}
	
	/**
	 * Registers the specified program with the kernel manager.
	 *
	 * @param __n The program to register.
	 * @throws IllegalStateException If the index has already been registered
	 * or is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/25
	 */
	protected final void registerProgram(KernelProgram __p)
		throws IllegalStateException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		int dx = __p.index();
		
		// Progams may only be registered once
		List<KernelProgram> programs = this._programs;
		synchronized (this.lock)
		{
			// {@squirreljme.error ZZ0h Program with the given index has
			// already been registred. (The program index)}
			int n = programs.size();
			for (int i = 0; i < n; i++)
			{
				int pdx = programs.get(i).index();
				if (dx == pdx)
					throw new IllegalStateException(
						String.format("ZZ0h %d", dx));
			}
			
			// Insert new program
			programs.add(__p);
		}
	}
	
	/**
	 * Returns the kernel program associated by the given Id.
	 *
	 * @param __id The ID of the program to get.
	 * @return The program associated with the given ID or {@code null} if
	 * no such program exists.
	 * @since 2017/12/31
	 */
	final KernelProgram __byId(int __id)
	{
		List<KernelProgram> programs = this._programs;
		synchronized (this.lock)
		{
			for (int i = 0, n = programs.size(); i < n; i++)
			{
				KernelProgram rv;
				if (__id == (rv = programs.get(i)).index())
					return rv;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Returns the classpath which is needed to launch the target program.
	 *
	 * @param __program The program to launch.
	 * @return The classpath for the program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	final KernelProgram[] __classPath(KernelProgram __program)
		throws NullPointerException
	{
		if (__program == null)
			throw new NullPointerException("NARG");
		
		// Return value
		Set<KernelProgram> rv = new LinkedHashSet<>();
		
		// Recursively get the dependencies for this program
		synchronized (this.lock)
		{
			// Go through dependencies of this program
			for (int i = 1; i >= 0; i++)
			{
				String val = __program.__controlGet(
					SystemProgramControlKey.DEPENDENCY_PREFIX + i);
				if (val == null)
					break;
				
				// {@squirreljme.error AP0b Could not locate a dependency, it
				// may have been deleted.}
				KernelProgram found = this.__byId(Integer.parseInt(val));
				if (found == null)
					throw new IllegalStateException("AP0b");
				
				// Recursively add the dependencies
				for (KernelProgram fc : this.__classPath(found))
					rv.add(fc);
				rv.add(found);
			}
			
			// Always add this program to the path because it technically is
			// a part of the classpath
			rv.add(__program);
		}
		
		return rv.<KernelProgram>toArray(new KernelProgram[rv.size()]);
	}
	
	/**
	 * Specifies that the input ZIP be JIT compiled and installed.
	 *
	 * @param __info Installation information.
	 * @return The result of compilation and installation.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	final KernelProgramInstallReport __jitAndInstall(
		KernelProgramInstallInfo __info)
		throws NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		KernelProgramInstallReport result = this.accessJitAndInstall(__info);
		
		// If a program was generated then it becomes registered
		KernelProgram program = result.program();
		if (program != null)
		{
			// Set program control information
			program.__controlSet(
				SystemProgramControlKey.IS_INSTALLED, "true");
			program.__controlSet(
				SystemProgramControlKey.STATE_FLAG_AVAILABLE, "true");
			program.__controlSet(
				SystemProgramControlKey.STATE_FLAG_ENABLED, "true");
			
			// Record dependency IDs
			KernelProgram[] depends = __info.depends();
			for (int i = 0, n = depends.length; i < n; i++)
				program.__controlSet(
					SystemProgramControlKey.DEPENDENCY_PREFIX + (i + 1),
					Integer.toString(depends[i].index()));
			
			// Register the program
			registerProgram(program);
		}
		
		// Return the input result
		return result;
	}
}


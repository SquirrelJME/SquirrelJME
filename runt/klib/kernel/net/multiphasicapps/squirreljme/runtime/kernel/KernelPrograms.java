// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.runtime.midlet.depends.DependencyInfo;
import net.multiphasicapps.squirreljme.runtime.midlet.depends.MatchResult;
import net.multiphasicapps.squirreljme.runtime.midlet.depends.ProvidedInfo;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteInfo;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

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
		try (ZipBlockReader zip = new ZipBlockReader(copy))
		{
			// Open suite information for the program to be installed
			SuiteInfo info;
			try (InputStream in = zip.open("META-INF/MANIFEST.MF"))
			{
				if (in == null)
					return new KernelProgramInstallReport(
						InstallErrorCodes.CORRUPT_JAR);
				
				info = new SuiteInfo(new JavaManifest(in));
			}
			
			// Need to make sure that all dependencies are checked and recorded
			List<KernelProgram> depends = new ArrayList<>();
			DependencyInfo origdeps = info.dependencies(),
				restdeps = origdeps;
			
			// Programs are verified against other programs
			List<KernelProgram> programs = this._programs;
			synchronized (programs)
			{
				// Go through other programs and try to find dependency
				// matches
				for (KernelProgram program : programs)
				{
					// This program provides dependencies, do not remove any
					// dependencies because some dependencies such as MEEP-8
					// may be provided by a large number of programs and if
					// they are added on they may become missing
					ProvidedInfo provided = program.suiteInfo().provided();
					MatchResult result = origdeps.match(provided);
					if (result.hasMatches())
						depends.add(program);
					
					// But it still needs to be detected if any dependencies
					// have not been met at all, so keep removing them
					if (!restdeps.isEmpty())
					{
						MatchResult restres = restdeps.match(provided);
						restdeps = restres.unmatched();
					}
				}
				
				// Dependencies are missing so the application cannot be
				// installed
				if (!restdeps.noOptionals().isEmpty())
					return new KernelProgramInstallReport(
						InstallErrorCodes.
							APP_INTEGRITY_FAILURE_DEPENDENCY_MISMATCH);
				
				throw new todo.TODO();
			}
		}
		
		// Invalid JAR
		catch (ZipEntryNotFoundException e)
		{
			return new KernelProgramInstallReport(
				InstallErrorCodes.CORRUPT_JAR);
		}
		
		// Failed to read the ZIP
		catch (IOException e)
		{
			return new KernelProgramInstallReport(
				InstallErrorCodes.IO_FILE_ERROR);
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
}


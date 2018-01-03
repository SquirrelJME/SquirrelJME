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

import java.io.IOException;
import java.io.InputStream;
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
 * This class manages the installation of the input program and keeps track
 * of the state so that {@link KernelPrograms} is not complicated with the
 * installer code.
 *
 * @since 2017/12/31
 */
@Deprecated
final class __ProgramInstaller__
{
	/** The program manager. */
	protected final KernelPrograms manager;
	
	/** The offset into the data array. */
	protected final int offset;
	
	/** The length of the JAR. */
	protected int length;
	
	/** The JAR data. */
	private final byte[] _data;
	
	/** The list of other programs which are available. */
	private final List<KernelProgram> _programs;
	
	/**
	 * Initializes the installer.
	 *
	 * @param __mgr The program manager.
	 * @param __list The list of available programs.
	 * @param __b The input JAR bytes.
	 * @param __o The offset into the array.
	 * @param __l The length of the JAR file.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	__ProgramInstaller__(KernelPrograms __mgr, List<KernelProgram> __list,
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__mgr == null || __list == null || __b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		this.manager = __mgr;
		this._programs = __list;
		this._data = __b;
		this.offset = __o;
		this.length = __l;
	}
	
	/**
	 * Runs the installation process.
	 *
	 * @return The result of the installation.
	 * @since 2017/12/31
	 */
	public KernelProgramInstallReport run()
	{
		try (ZipBlockReader zip = new ZipBlockReader(this._data, this.offset,
			this.length))
		{
			// Open suite information for the program to be installed
			SuiteInfo info;
			try (InputStream in = zip.open("META-INF/MANIFEST.MF"))
			{
				// {@squirreljme.error AP04 Missing manifest file.}
				if (in == null)
					throw new __InstallException__(
						InstallErrorCodes.CORRUPT_JAR, "AP04");
				
				info = new SuiteInfo(new JavaManifest(in));
			}
			
			// Need to make sure that all dependencies are checked and recorded
			List<KernelProgram> depends = new ArrayList<>();
			DependencyInfo origdeps = info.dependencies(),
				restdeps = origdeps;
			
			// Go through other programs and try to find dependency
			// matches
			List<KernelProgram> programs = this._programs;
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
			// {@squirreljme.error AP05 Cannot install the JAR because some
			// dependencies have not been statisfied. (The remaining
			// dependencies)}
			if (!restdeps.noOptionals().isEmpty())
				throw new __InstallException__(
					InstallErrorCodes.
						APP_INTEGRITY_FAILURE_DEPENDENCY_MISMATCH,
					String.format("AP05 %s", restdeps));
			
			// Determine the next index
			int nextid = 1;
			for (KernelProgram program : programs)
			{
				int pdx = program.index();
				if (pdx >= nextid)
					nextid = pdx + 1;
			}
			
			// Perform installation
			return this.manager.__jitAndInstall(new KernelProgramInstallInfo(
				depends.<KernelProgram>toArray(
				new KernelProgram[depends.size()]), zip, info.suite(),
				nextid));
		}
		
		// Easily reported exception
		catch (__InstallException__ e)
		{
			// Report it
			e.printStackTrace();
			
			// Use the input error code.
			return new KernelProgramInstallReport(e.code(), e.getMessage());
		}
		
		// Caught some exception
		catch (Throwable e)
		{
			// Report it
			e.printStackTrace();
			
			// Determine error code based on input exception type
			int code = InstallErrorCodes.OTHER_ERROR;
			if (e instanceof ZipEntryNotFoundException)
				code = InstallErrorCodes.CORRUPT_JAR;
			else if (e instanceof IOException)
				code = InstallErrorCodes.IO_FILE_ERROR;
			
			return new KernelProgramInstallReport(
				InstallErrorCodes.OTHER_ERROR, e.getMessage());
		}
	}
}


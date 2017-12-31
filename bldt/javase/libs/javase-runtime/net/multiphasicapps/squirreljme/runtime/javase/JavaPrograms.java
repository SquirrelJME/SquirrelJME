// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgramInstallInfo;
import net.multiphasicapps.squirreljme.runtime.kernel.
	KernelProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelPrograms;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * This manages programs on the Java host.
 *
 * @since 2017/12/14
 */
public class JavaPrograms
	extends KernelPrograms
{
	/**
	 * Initializes the Java program manager.
	 *
	 * @since 2017/12/27
	 */
	public JavaPrograms()
	{
		// Always register the system program
		registerProgram(new JavaSystemProgram());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	protected KernelProgramInstallReport accessJitAndInstall(
		KernelProgramInstallInfo __info)
		throws NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}


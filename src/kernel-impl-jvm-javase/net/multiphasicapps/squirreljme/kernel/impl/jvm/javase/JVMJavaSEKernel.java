// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.multiphasicapps.squirreljme.kernel.archive.ArchiveFinder;
import net.multiphasicapps.squirreljme.kernel.archive.fs.FSArchiveFinder;
import net.multiphasicapps.squirreljme.kernel.display.ConsoleDisplay;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This contains the launcher used by the host Java SE system.
 *
 * @since 2016/05/14
 */
public class JVMJavaSEKernel
	extends Kernel
{
	/** The archive finders which are available. */
	protected final List<ArchiveFinder> finders;
	
	/**
	 * This initializes the launcher which uses an existing full Java SE JVM.
	 *
	 * @param __args The arguments to the launcher.
	 * @since 2016/05/14
	 */
	public JVMJavaSEKernel(String... __args)
	{
		// Must always exist
		if (__args == null)
			__args = new String[0];
		
		// Create
		finders = Collections.<ArchiveFinder>unmodifiableList(
			Arrays.<ArchiveFinder>asList(new FSArchiveFinder(this)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public List<ArchiveFinder> archiveFinders()
	{
		return finders;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public ConsoleDisplay createConsoleDisplay()
	{
		// Setup a new console which uses a Swing based interface
		// Send events to the current process
		KernelProcess kp = currentProcess();
		SwingConsoleView scv = new SwingConsoleView(kp);
		
		// Show it
		scv.setVisible();
		
		// Return it
		return scv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public void quitKernel()
	{
		// Can quit
		System.exit(0);
	}
}


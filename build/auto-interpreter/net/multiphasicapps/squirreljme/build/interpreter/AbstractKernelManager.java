// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.io.InputStream;
import java.util.List;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.kernel.InvalidSuiteException;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.KernelSuiteManager;
import net.multiphasicapps.squirreljme.kernel.KernelThreadListener;
import net.multiphasicapps.squirreljme.kernel.KernelThreadManager;
import net.multiphasicapps.squirreljme.suiteid.APIConfiguration;
import net.multiphasicapps.squirreljme.suiteid.APIProfile;
import net.multiphasicapps.squirreljme.suiteid.APIStandard;

/**
 * This is the base class for the interpreter kernel managers, since the two
 * kernel managers will generally share some code while have some other
 * differences this is used to keep the common code together.
 *
 * @since 2016/11/02
 */
public abstract class AbstractKernelManager
	implements KernelLaunchParameters, KernelSuiteManager, KernelThreadManager
{
	/** The owning auto interpreter. */
	protected final AutoInterpreter autointerpreter;
	
	/** The thread listener to use. */
	private volatile KernelThreadListener _threadlistener;
	
	/**
	 * Initializes the base abstract kernel manager.
	 *
	 * @param __ai The auto interpreter owning this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public AbstractKernelManager(AutoInterpreter __ai)
		throws NullPointerException
	{
		// Check
		if (__ai == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.autointerpreter = __ai;
	}
	
	/**
	 * Returns the launch parameters to actually use.
	 *
	 * @return The launch parameters to use.
	 * @since 2016/10/07
	 */
	protected abstract KernelLaunchParameters internalLaunchParameters();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/08
	 */
	@Override
	public final String getSystemProperty(String __k)
	{
		return internalLaunchParameters().getSystemProperty(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/02
	 */
	@Override
	public final void setThreadListener(KernelThreadListener __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._threadlistener = __t;
	}
}


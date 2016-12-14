// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import net.multiphasicapps.squirreljme.suiteid.APIConfiguration;
import net.multiphasicapps.squirreljme.suiteid.APIProfile;
import net.multiphasicapps.squirreljme.suiteid.APIStandard;

/**
 * This interface provides access to the class path that the kernel uses
 * itself and for processes. This essentially allows lookup of classes that
 * are built into SquirrelJME itself.
 *
 * This interface also handles APIs which are defined by the system and
 * included.
 *
 * Implementations of this interface must also handle primitive types and
 * array types.
 *
 * @since 2016/12/14
 */
public interface KernelSystemClassPath
{
	/**
	 * Returns an array of the configurations which are supported in the
	 * system's class path.
	 *
	 * @return The set of supported configurations.
	 * @since 2016/12/14
	 */
	public abstract APIConfiguration[] supportedConfigurations();
	
	/**
	 * Returns an array of the profiles which are supported in the system's
	 * class path.
	 *
	 * @return The set of supported profiles.
	 * @since 2016/12/14
	 */
	public abstract APIProfile[] supportedProfiles();
	
	/**
	 * Returns an array of the standards which are supported in the system's
	 * class path.
	 *
	 * @return The set of supported standards.
	 * @since 2016/12/14
	 */
	public abstract APIStandard[] supportedStandards();
}


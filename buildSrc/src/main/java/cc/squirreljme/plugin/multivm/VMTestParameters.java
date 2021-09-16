// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.workers.WorkParameters;

/**
 * This contains the parameters for work units, which are used to divide and
 * execute the actual work.
 *
 * @since 2020/09/06
 */
public interface VMTestParameters
	extends WorkParameters
{
	/**
	 * Returns the command line to be executed.
	 * 
	 * @return The command line to execute.
	 * @since 2020/09/07
	 */
	ListProperty<String> getCommandLine();
	
	/**
	 * The current test count.
	 * 
	 * @return The current test count.
	 * @since 2020/09/07
	 */
	Property<Integer> getCount();
	
	/**
	 * Returns the result of the test.
	 * 
	 * @return The result of the test.
	 * @since 2020/09/06
	 */
	RegularFileProperty getResultFile();
	
	/**
	 * The test to be ran.
	 * 
	 * @return The test to be ran.
	 * @since 2020/09/06
	 */
	Property<String> getTestName();
	
	/**
	 * The total number of tests.
	 * 
	 * @return The test total.
	 * @since 2020/09/07
	 */
	Property<Integer> getTotal();
	
	/**
	 * Returns the virtual machine name.
	 * 
	 * @return The virtual machine name.
	 * @since 2020/09/07
	 */
	Property<String> getVmName();
}

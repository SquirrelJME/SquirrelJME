// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import org.gradle.api.internal.tasks.testing.DefaultTestClassDescriptor;
import org.gradle.api.internal.tasks.testing.TestDescriptorInternal;

/**
 * Describes a class being tested.
 *
 * @since 2020/03/06
 */
public class EmulatedTestClassDescriptor
	extends DefaultTestClassDescriptor
{
	/** The owning suite. */
	protected final EmulatedTestSuiteDescriptor suite;
	
	/**
	 * Initializes the class descriptor.
	 *
	 * @param __suite The owning suite.
	 * @param __className The class being tested.
	 * @since 2020/03/06
	 */
	public EmulatedTestClassDescriptor(EmulatedTestSuiteDescriptor __suite,
		String __className)
	{
		super(new EmulatedTestId(), __className, __className);
		
		if (__suite == null)
			throw new NullPointerException("No suite specified.");
		
		this.suite = __suite;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
    @Override
    public TestDescriptorInternal getParent() {
        return this.suite;
    }
}

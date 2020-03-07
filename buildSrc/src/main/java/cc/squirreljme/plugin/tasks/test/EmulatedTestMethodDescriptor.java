// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor;
import org.gradle.api.internal.tasks.testing.TestDescriptorInternal;

/**
 * Not Described.
 *
 * @since 2020/03/06
 */
public class EmulatedTestMethodDescriptor
	extends DefaultTestMethodDescriptor
{
	/** The class this is in. */
	protected final EmulatedTestClassDescriptor inClass;
	
	/**
	 * Initializes the test method descriptor.
	 *
	 * @param __class The class to execute.
	 * @since 2020/03/06
	 */
	public EmulatedTestMethodDescriptor(EmulatedTestClassDescriptor __class)
	{
		super(new EmulatedTestId(), __class.getClassName(), "run");
		
		this.inClass = __class;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
    @Override
	public Object getOwnerBuildOperationId()
	{
		// This must always be set otherwise Gradle will crash
		return this.getId();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
    @Override
    public TestDescriptorInternal getParent() {
        return this.inClass;
    }
}

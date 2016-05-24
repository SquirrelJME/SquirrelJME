// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.swing;

import java.lang.ref.Reference;
import javax.swing.JComponent;
import net.multiphasicapps.squirreljme.ui.PIComponent;
import net.multiphasicapps.squirreljme.ui.UIComponent;
import net.multiphasicapps.squirreljme.ui.UIException;

/**
 * This is the base class for all components.
 *
 * @since 2016/05/24
 */
public class SwingComponent
	extends SwingBase
	implements PIComponent
{
	/** The component which is used. */
	protected final JComponent component;
	
	/**
	 * Initializes the swing component.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @param __jc The component to be used internally.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/24
	 */
	public SwingComponent(SwingManager __sm,
		Reference<? extends UIComponent> __ref, JComponent __jc)
		throws NullPointerException
	{
		super(__sm, __ref);
		
		// Check
		if (__jc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.component = __jc;
	}
	
	/**
	 * Returns the component to be placed in the display.
	 *
	 * @return The native component.
	 * @since 2016/05/24
	 */
	public JComponent getComponent()
	{
		return this.component;
	}
}


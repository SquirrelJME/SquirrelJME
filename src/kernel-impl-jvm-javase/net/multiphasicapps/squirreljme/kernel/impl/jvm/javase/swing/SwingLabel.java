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
import javax.swing.JLabel;
import net.multiphasicapps.squirreljme.ui.PILabel;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UILabel;

/**
 * This provides a native implementation of a label.
 *
 * @since 2016/05/24
 */
public class SwingLabel
	extends SwingComponent
	implements PILabel
{
	/** The label component. */
	protected final JLabel label;
	
	/**
	 * Initializes the swing label.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @since 2016/05/24
	 */
	public SwingLabel(SwingManager __sm, Reference<UILabel> __ref)
	{
		super(__sm, __ref, new JLabel());
		
		// Set
		this.label = (JLabel)this.component;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public void setIcon(UIImage __icon)
		throws UIException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public void setText(String __text)
		throws UIException
	{
		throw new Error("TODO");
	}
}


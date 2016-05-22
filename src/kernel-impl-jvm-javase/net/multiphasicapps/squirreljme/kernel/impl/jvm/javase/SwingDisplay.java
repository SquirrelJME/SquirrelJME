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

import java.lang.ref.Reference;
import javax.swing.JFrame;
import net.multiphasicapps.squirreljme.ui.InternalDisplay;
import net.multiphasicapps.squirreljme.ui.UIDisplay;

/**
 * This implemens the internal display in Swing.
 *
 * A display in Swing is mapped to a {@link JFrame}.
 *
 * @since 2016/05/21
 */
public class SwingDisplay
	extends InternalDisplay
{
	/** The frame for the display. */
	protected final JFrame frame;
	
	/**
	 * Initializes the swing display.
	 *
	 * @param __ref The external reference.
	 * @since 2016/05/22
	 */
	public SwingDisplay(Reference<UIDisplay> __ref)
	{
		super(__ref);
		
		// Create the frame
		JFrame frame = new JFrame();
		this.frame = frame;
	}
}


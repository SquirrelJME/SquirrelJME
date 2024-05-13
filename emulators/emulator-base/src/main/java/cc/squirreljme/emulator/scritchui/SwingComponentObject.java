// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import java.awt.Component;
import javax.swing.JComponent;

/**
 * Base bracket for components.
 *
 * @since 2024/03/16
 */
@Deprecated
public abstract class SwingComponentObject
	implements ScritchComponentBracket
{
	/**
	 * Returns the component.
	 *
	 * @return The component used.
	 * @since 2024/03/17
	 */
	public abstract JComponent component();
}

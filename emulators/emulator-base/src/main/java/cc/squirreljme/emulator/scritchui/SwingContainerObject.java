// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import java.awt.Container;

/**
 * Swing interface for containers.
 *
 * @since 2024/03/17
 */
@Deprecated
public interface SwingContainerObject
	extends ScritchContainerBracket
{
	/**
	 * Returns the Swing container.
	 *
	 * @return The Swing container.
	 * @since 2024/03/17
	 */
	Container swingContainer();
}

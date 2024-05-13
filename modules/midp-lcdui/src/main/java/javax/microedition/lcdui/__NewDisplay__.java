// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayFactory;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;

/**
 * Creates new displays.
 *
 * @since 2024/03/09
 */
final class __NewDisplay__
	implements DisplayFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public DisplayState create(ScritchInterface __scritch,
		ScritchWindowBracket __window, ScritchScreenBracket __screen)
		throws NullPointerException
	{
		if (__scritch == null || __window == null || __screen == null)
			throw new NullPointerException("NARG");
		
		return new Display(__scritch, __window, __screen).__state();
	}
}

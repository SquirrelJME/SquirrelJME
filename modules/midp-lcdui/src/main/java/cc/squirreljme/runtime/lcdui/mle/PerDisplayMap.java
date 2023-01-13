// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a map of {@link LinkedDisplay} which stores the native widget
 * implementations of {@link UIFormBracket} and {@link UIWidgetBracket}s for
 * each potential different display, as each display may have its own unique
 * set of widgets and otherwise. The primary purpose of this is to handle
 * situations where two different displays have completely different means
 * of handling widgets and otherwise.
 *
 * @param <W> The class which stores all the widgets.
 * @since 2023/01/12
 */
public final class PerDisplayMap<W>
{
	/** The subtype this contains. */
	public final Class<W> subType;
	
	public PerDisplayMap()
	{
		throw Debugging.todo();
	}
}

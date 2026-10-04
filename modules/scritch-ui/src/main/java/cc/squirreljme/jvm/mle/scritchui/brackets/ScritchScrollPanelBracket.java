// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.brackets;

import cc.squirreljme.jvm.mle.annotation.GhostObject;
import org.jetbrains.annotations.Debug;

/**
 * Bracket for scroll panels.
 *
 * @since 2024/07/29
 */
@GhostObject
@Debug.Renderer(text=GhostObject.INTELLIJ_RENDERER,
	hasChildren="false")
public interface ScritchScrollPanelBracket
	extends ScritchBaseBracket, ScritchViewBracket
{
}

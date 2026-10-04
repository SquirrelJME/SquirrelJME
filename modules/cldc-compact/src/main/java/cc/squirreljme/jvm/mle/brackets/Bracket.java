// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;
import org.jetbrains.annotations.Debug;

/**
 * Base for all bracket types.
 *
 * @since 2026/10/01
 */
@GhostObject
@Debug.Renderer(text= GhostObject.INTELLIJ_RENDERER,
	hasChildren="false")
@SquirrelJMENativeApi(min = "0.4.0")
public interface Bracket
{
}

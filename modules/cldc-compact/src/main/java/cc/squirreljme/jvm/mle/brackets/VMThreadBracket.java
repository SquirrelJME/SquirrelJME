// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;
import org.jetbrains.annotations.Debug;

/**
 * This bracket represents extra information regarding threads and otherwise.
 *
 * @see ThreadShelf
 * @since 2020/06/17
 */
@GhostObject
@Debug.Renderer(text=GhostObject.INTELLIJ_RENDERER,
	hasChildren="false")
@SquirrelJMENativeApi(min = "0.4.0")
public interface VMThreadBracket
	extends Bracket
{
}

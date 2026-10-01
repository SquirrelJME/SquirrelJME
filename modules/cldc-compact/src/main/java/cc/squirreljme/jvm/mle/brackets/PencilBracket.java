// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Debug;

/**
 * This is a bracket which maintains the state of graphics operations within
 * the pencil structure.
 *
 * @since 2020/09/25
 */
@SquirrelJMEVendorApi
@GhostObject
@Debug.Renderer(text=GhostObject.INTELLIJ_RENDERER,
	hasChildren="false")
public interface PencilBracket
	extends CloseableBracket
{
}

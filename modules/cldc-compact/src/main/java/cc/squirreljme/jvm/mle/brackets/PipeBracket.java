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
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Debug;

/**
 * This represents a pipe that contains a connection to either the terminal,
 * a true file descriptor, a discard, or a buffer.
 *
 * @since 2022/03/19
 */
@GhostObject
@Debug.Renderer(text=GhostObject.INTELLIJ_RENDERER,
	hasChildren="false")
public interface PipeBracket
	extends CloseableBracket
{
}

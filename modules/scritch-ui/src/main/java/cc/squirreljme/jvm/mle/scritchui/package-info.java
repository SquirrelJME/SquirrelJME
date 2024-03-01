// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This packages contains the ScritchUI interfaces and callbacks.
 * 
 * Unlike MLE, ScritchUI is entirely based on callbacks with a single native
 * method to get the system callback it if it exists. This is to not require
 * the use of a wrapper between native calls and non-natives, it is now
 * only needed to use the callback directly.
 * 
 * ScritchUI is modeled on top of Swing and is much simpler to use compared
 * to UIForm.
 *
 * @since 2024/02/29
 */

package cc.squirreljme.jvm.mle.scritchui;

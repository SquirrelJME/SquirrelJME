// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

/**
 * The type of backend to use, if forced.
 *
 * @since 2020/10/09
 */
@Deprecated
public enum UIBackendType
{
	/** Native backend, uses system widgets if supported/possible. */
	@Deprecated
	NATIVE,
	
	/** Framebuffer backend, uses a pre-existing image/graphics to draw on. */
	@Deprecated
	FRAMEBUFFER,
	
	/** Headless (no-op) backend, uses an inaccessible framebuffer. */
	@Deprecated
	HEADLESS,
	
	/* End. */
	;
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.annotation;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * Specifies how far through compilation and running of code that an annotation
 * should be visible.
 *
 * @since 2014/10/13
 */
@Api
public enum RetentionPolicy
{
	/** Kept after compilation, but not visible at runtime. */
	@Api
	CLASS(),
	
	/** Kept after compilation and visible at runtime. */
	@Api
	RUNTIME(),
	
	/** Removed after compilation, does not appear in the class file. */
	@Api
	SOURCE(),
	
	/** End. */
	;
}


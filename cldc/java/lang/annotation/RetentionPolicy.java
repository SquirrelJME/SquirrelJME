// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang.annotation;

/**
 * Specifies how far through compilation and running of code that an annotation
 * should be visible.
 *
 * @since k8 2014/10/13
 */
public enum RetentionPolicy
{
	/** Kept after compilation, but not visible at runtime. */
	CLASS(),
	
	/** Kept after compilation and visible at runtime. */
	RUNTIME(),
	
	/** Removed after compilation, does not appear in the class file. */
	SOURCE(),
	
	/** End. */
	;
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.tests;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This specifies a single set of input arguments which are to be used as
 * default test set for a given method.
 *
 * @since 2018/03/06
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface Parameters
{
	/**
	 * The return value for the parameters.
	 *
	 * @since 2018/03/06
	 */
	Argument returnValue();
	
	/**
	 * The parameters which are used as default input for the test method.
	 *
	 * @since 2018/03/06
	 */
	Argument[] args();
}


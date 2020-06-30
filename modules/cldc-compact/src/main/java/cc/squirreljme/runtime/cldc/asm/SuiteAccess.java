// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * Access to suites and other suites which are available for usage.
 *
 * @since 2018/10/26
 */
@Deprecated
public class SuiteAccess
{
	/**
	 * Compile/install using data which was passed directly to the given
	 * byte array.
	 */
	@Deprecated
	public static final int WHERE_DIRECT =
		1;
	
	/**
	 * Compile/install using the given JAR suite specified as a string, this
	 * may be the same as a classpath value.
	 */
	@Deprecated
	public static final int WHERE_SUITE =
		2;
	
	/**
	 * Returns the suites which are available for usage.
	 *
	 * @return The suites which are available for usage.
	 * @since 2018/10/26
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native String[] availableSuites();
	
	/**
	 * Returns the current classpath that is being used.
	 *
	 * @return The current classpath.
	 * @since 2018/12/06
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native String[] currentClassPath();
	
	/**
	 * Specifies that the given JAR should be installed into the suite
	 * manager, it may be compiled first.
	 *
	 * @param __wh Where is this JAR located?
	 * @param __data Data which depends on the where parameter.
	 * @return The install status.
	 * @since 2019/04/17
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final native int installJar(int __wh, byte[] __data);
	
	/**
	 * Returns the last compile error which was set.
	 *
	 * @return The last compile error.
	 * @since 2019/04/17
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final native int lastCompileError();
}


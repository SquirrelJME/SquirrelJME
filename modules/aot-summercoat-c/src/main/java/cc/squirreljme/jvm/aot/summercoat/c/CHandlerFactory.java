// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.c;

import cc.squirreljme.jvm.aot.summercoat.SummerCoatHandler;
import cc.squirreljme.jvm.aot.summercoat.SummerCoatHandlerFactory;
import cc.squirreljme.jvm.aot.summercoat.SummerCoatLinkGlob;
import cc.squirreljme.jvm.aot.summercoat.target.SummerCoatArchitectureVariant;
import cc.squirreljme.jvm.aot.summercoat.target.TargetArchitecture;
import cc.squirreljme.jvm.aot.summercoat.target.TargetArchitectureVariant;
import cc.squirreljme.jvm.aot.summercoat.target.TargetBang;
import cc.squirreljme.jvm.aot.summercoat.target.TargetOperatingSystem;
import cc.squirreljme.jvm.aot.summercoat.target.TargetOperatingSystemVariant;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Factory for creating the C handler.
 *
 * @since 2022/09/05
 */
public class CHandlerFactory
	implements SummerCoatHandlerFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public SummerCoatHandler handler(SummerCoatLinkGlob __glob)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public boolean supportsBang(TargetBang __bang)
		throws NullPointerException
	{
		if (__bang == null)
			throw new NullPointerException("NARG");
		
		return __bang.arch == TargetArchitecture.SUMMERCOAT &&
			__bang.archVariant == SummerCoatArchitectureVariant.C &&
			__bang.os == TargetOperatingSystem.SUMMERCOAT &&
			__bang.osVariant == TargetOperatingSystemVariant.NONE;
	}
}

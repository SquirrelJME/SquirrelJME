// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface for screens.
 *
 * @since 2024/03/24
 */
@SquirrelJMEVendorApi
public class FramebufferScreenInterface
	extends FramebufferBaseInterface
	implements ScritchScreenInterface
{
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferScreenInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
		throws NullPointerException
	{
		super(__self, __core);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int dpi(ScritchScreenBracket __screen)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int height(ScritchScreenBracket __screen)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean isBuiltIn(ScritchScreenBracket __screen)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean isPortrait(ScritchScreenBracket __screen)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int id(ScritchScreenBracket __screen)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int width(ScritchScreenBracket __screen)
	{
		throw Debugging.todo();
	}
}

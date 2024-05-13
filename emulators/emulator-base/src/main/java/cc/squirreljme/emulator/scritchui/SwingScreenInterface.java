// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Swing screen interface.
 *
 * @since 2024/03/13
 */
@Deprecated
public class SwingScreenInterface
	implements ScritchScreenInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int dpi(ScritchScreenBracket __screen)
	{
		if (__screen == null)
			throw new MLECallError("Null screen.");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int height(ScritchScreenBracket __screen)
	{
		if (__screen == null)
			throw new MLECallError("Null screen.");
	
		return ((SwingScreenObject)__screen).height();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public boolean isBuiltIn(ScritchScreenBracket __screen)
	{
		if (__screen == null)
			throw new MLECallError("Null screen.");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public boolean isPortrait(ScritchScreenBracket __screen)
	{
		if (__screen == null)
			throw new MLECallError("Null screen.");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int id(ScritchScreenBracket __screen)
	{
		if (__screen == null)
			throw new MLECallError("Null screen.");
		
		return ((SwingScreenObject)__screen).id();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int width(ScritchScreenBracket __screen)
	{
		if (__screen == null)
			throw new MLECallError("Null screen.");
		
		return ((SwingScreenObject)__screen).width();
	}
}

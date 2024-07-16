// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Not Described.
 *
 * @since 2024/04/02
 */
public class DylibEnvironmentInterface
	extends DylibBaseInterface
	implements ScritchEnvironmentInterface
{
	/** Look and feel interface. */
	protected ScritchLAFInterface lookAndFeel;
	
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibEnvironmentInterface(
		Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
		throws NullPointerException
	{
		super(__selfApi, __dyLib);
		
		this.lookAndFeel = new DylibLookAndFeelInterface(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/12
	 */
	@Override
	public @NotNull PencilFontBracket[] builtinFonts()
	{
		return this.dyLib.builtinFonts();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/14
	 */
	@Override
	public @NotNull PencilFontBracket fontDerive(
		@NotNull PencilFontBracket __font, int __style,
		@Range(from = 1, to = Integer.MAX_VALUE) int __pixelSize)
		throws MLECallError
	{
		if (__font == null)
			throw new MLECallError("NARG");
		
		return this.dyLib.fontDerive((DylibPencilFontObject)__font,
			__style, __pixelSize);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean isInhibitingSleep()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/16
	 */
	@Override
	public boolean isPanelOnly()
	{
		return this.dyLib.isPanelOnly();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public ScritchLAFInterface lookAndFeel()
	{
		return this.lookAndFeel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public ScritchScreenBracket[] screens()
	{
		return this.dyLib.screens();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void setInhibitSleep(boolean __inhibit)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int windowManagerType()
	{
		return this.dyLib.windowManagerType();
	}
}

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
import org.jetbrains.annotations.Nullable;
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
		// Forward
		return NativeScritchDylib.__builtinFonts(this.dyLib._stateP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/04/10
	 */
	@Override
	public PencilFontBracket fontByFace(int __inFace,
		@Nullable int[] __inParams,
		@Nullable int[] __outParams)
		throws MLECallError
	{
		// Locate font by face
		long fontP = NativeScritchDylib.__fontByFace(this.dyLib._stateP,
			__inFace, __inParams, __outParams);
		if (fontP == 0L)
			return null;
		
		// Wrap font
		return new DylibPencilFontObject(fontP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/14
	 */
	@Override
	public @NotNull PencilFontBracket fontDerive(
		@NotNull PencilFontBracket __font,
		@Nullable int[] __deriveParams,
		@Nullable int[] __newParams)
		throws MLECallError
	{
		if (__font == null)
			throw new MLECallError("NARG");
		
		long fontP = ((DylibPencilFontObject)__font).objectPointer();
		return new DylibPencilFontObject(NativeScritchDylib.__fontDerive(
			this.dyLib._stateP, fontP, __deriveParams, __newParams));
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
		// Read in screens
		int numScreens = DylibScreenInterface._REQUEST_SCREENS;
		for (;;)
		{
			// Request all screens
			long[] screenPs = new long[numScreens];
			numScreens = NativeScritchDylib.__screens(this.dyLib._stateP,
				screenPs);
			
			// Not big enough?
			if (numScreens > screenPs.length)
				continue;
			
			// Map them to objects
			ScritchScreenBracket[] result =
				new ScritchScreenBracket[numScreens];
			for (int i = 0; i < numScreens; i++)
			{
				if (screenPs[i] == 0)
					throw Debugging.oops(i);
				
				result[i] = new DylibScreenObject(screenPs[i]);
			}
			
			return result;
		}
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
		return NativeScritchDylib.__windowManagerType(this.dyLib._stateP);
	}
}

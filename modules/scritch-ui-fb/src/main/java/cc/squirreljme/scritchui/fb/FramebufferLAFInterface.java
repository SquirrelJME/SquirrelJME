// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFElementColor;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Provides an interface for the look and feel.
 *
 * @since 2024/03/26
 */
@Deprecated
public class FramebufferLAFInterface
	extends FramebufferBaseInterface
	implements ScritchLAFInterface
{
	/** The core look and feel interface. */
	protected final ScritchLAFInterface lafApi;
	
	/** Foreground color. */
	protected final int foreground;
	
	/** Background color. */
	protected final int background;
	
	/** Are we in dark mode? */
	protected final boolean darkMode;
	
	/**
	 * The core interface to use.
	 *
	 * @param __self The self interface.
	 * @param __core The core interface to wrap.
	 * @since 2024/03/26
	 */
	public FramebufferLAFInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
	{
		super(__self, __core);
		
		ScritchLAFInterface lafApi = __core.environment().lookAndFeel();
		this.lafApi = lafApi;
		
		// Are we in dark mode?
		boolean darkMode = lafApi.isDarkMode();
		this.darkMode = darkMode;
		if (darkMode)
		{
			this.foreground = 0xFFFFFFFF;
			this.background = 0xFF000000;
		}
		
		// Or light mode?
		else
		{
			this.foreground = 0xFF000000;
			this.background = 0xFFFFFFFF;
		}
	}
	
	@Override
	public @Nullable PencilFontBracket font(int __element)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public int elementColor(int __element)
	{
		switch (__element)
		{
			case ScritchLAFElementColor.BACKGROUND:
			case ScritchLAFElementColor.HIGHLIGHTED_BORDER:
			case ScritchLAFElementColor.HIGHLIGHTED_FOREGROUND:
			case ScritchLAFElementColor.IDLE_BACKGROUND:
			case ScritchLAFElementColor.IDLE_HIGHLIGHTED_FOREGROUND:
				return this.background;
				
			case ScritchLAFElementColor.FOREGROUND:
			case ScritchLAFElementColor.BORDER:
			case ScritchLAFElementColor.HIGHLIGHTED_BACKGROUND:
			case ScritchLAFElementColor.IDLE_FOREGROUND:
			case ScritchLAFElementColor.IDLE_HIGHLIGHTED_BACKGROUND:
				return this.foreground;
		}
		
		throw new MLECallError("INVL");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public int focusBorderStyle(boolean __focused)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public int imageSize(int __elem, boolean __height)
		throws MLECallError
	{
		// TODO FIXME: For now keep this fixed
		return 16;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public boolean isDarkMode()
	{
		return this.darkMode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public int panelBackgroundColor()
	{
		return this.background;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public int panelForegroundColor()
	{
		return this.foreground;
	}
}

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Objects;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Displayable;
import org.intellij.lang.annotations.MagicConstant;

@Api
public class Dialog
	extends Frame
{
	@Api
	public static final int	BUTTON_CANCEL = 2;
	
	@Api
	public static final int	BUTTON_NO = 8;
	
	@Api
	public static final int	BUTTON_OK = 1;
	
	@Api
	public static final int	BUTTON_YES = 4;
	
	@Api
	public static final int	DIALOG_ERROR = 2;
	
	@Api
	public static final int	DIALOG_INFO = 0;
	
	@Api
	public static final int	DIALOG_WARNING = 1;
	
	@Api
	public static final int	DIALOG_YESNO = 3;
	
	@Api
	public static final int	DIALOG_YESNOCANCEL = 4;
	
	/** The alert to use for the dialog. */
	@SquirrelJMEVendorApi
	final Alert _alert;
	
	@Api
	public Dialog(@MagicConstant(valuesFromClass = Dialog.class) int __type,
		String __title)
	{
		if (__type != Dialog.DIALOG_INFO &&
			__type != Dialog.DIALOG_ERROR &&
			__type != Dialog.DIALOG_WARNING &&
			__type != Dialog.DIALOG_YESNO &&
			__type != Dialog.DIALOG_YESNOCANCEL)
			throw new IllegalArgumentException("ILLA");
		
		Alert alert = new Alert(__title);
		
		this._alert = alert;
	}
	
	@Api
	public void setText(String __text)
	{
		this._alert.setString(Objects.toString(__text, ""));
	}
	
	/**
	 * Shows the dialog.
	 *
	 * @return The button which has been pressed.
	 * @throws UIException If the dialog is already being shown.
	 * @since 2025/03/27
	 */
	@Api
	public int show()
		throws UIException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/11/28
	 */
	@Override
	protected Displayable __squirreljmeDisplayable()
	{
		return this._alert;
	}
}

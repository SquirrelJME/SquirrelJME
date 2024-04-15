// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

import cc.squirreljme.runtime.cldc.annotation.Api;
import jdk.dio.modem.ModemSignalsControl;

@Api
public interface ModemUART
	extends UART, ModemSignalsControl<ModemUART>
{
}



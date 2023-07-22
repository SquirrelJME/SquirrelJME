// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.atcmd;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.nio.channels.ByteChannel;

@Api
public interface DataConnection
	extends ByteChannel
{
	@Api
	ATDevice getDevice();
}



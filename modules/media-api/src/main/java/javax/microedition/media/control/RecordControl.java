// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

@Api
public interface RecordControl
	extends Control
{
	@Api
	void commit()
		throws IOException;
	
	@Api
	String getContentType();
	
	@Api
	void reset()
		throws IOException;
	
	@Api
	void setRecordLocation(String __a)
		throws IOException, MediaException;
	
	@Api
	int setRecordSizeLimit(int __a)
		throws MediaException;
	
	@Api
	void setRecordStream(OutputStream __a);
	
	@Api
	void startRecord();
	
	@Api
	void stopRecord();
}



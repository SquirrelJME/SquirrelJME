// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

public interface RecordControl
	extends Control
{
	public abstract void commit()
		throws IOException;
	
	public abstract String getContentType();
	
	public abstract void reset()
		throws IOException;
	
	public abstract void setRecordLocation(String __a)
		throws IOException, MediaException;
	
	public abstract int setRecordSizeLimit(int __a)
		throws MediaException;
	
	public abstract void setRecordStream(OutputStream __a);
	
	public abstract void startRecord();
	
	public abstract void stopRecord();
}



// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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



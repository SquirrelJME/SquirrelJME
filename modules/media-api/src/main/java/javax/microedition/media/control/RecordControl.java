// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
	void commit()
		throws IOException;
	
	String getContentType();
	
	void reset()
		throws IOException;
	
	void setRecordLocation(String __a)
		throws IOException, MediaException;
	
	int setRecordSizeLimit(int __a)
		throws MediaException;
	
	void setRecordStream(OutputStream __a);
	
	void startRecord();
	
	void stopRecord();
}



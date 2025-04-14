// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import com.oracle.json.JsonArray;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonValue;
import com.oracle.json.spi.JsonProvider;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that JSON can be parsed properly.
 *
 * @since 2022/07/12
 */
public class TestJsonParse
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/07/12
	 */
	@Override
	public void test()
		throws IOException
	{
		try (InputStream in = TestJsonParse.class
			.getResourceAsStream("sample.json"))
		{
			// Read in the object
			JsonObject obj = JsonProvider.provider()
				.createReader(in).readObject();
			
			this.secondary("string", obj.getString("string"));
			this.secondary("false", obj.getBoolean("false"));
			this.secondary("true", obj.getBoolean("true"));
			this.secondary("int", obj.getInt("int"));
			this.secondary("long",
				obj.getJsonNumber("long").longValue());
			this.secondary("null",
				obj.get("null") == JsonValue.NULL);
				
			// The array within
			JsonArray array = obj.getJsonArray("array");
			
			this.secondary("array-length", array.size());
			for (int i = 0, n = array.size(); i < n; i++)
				this.secondary("array-" + i,
					array.get(i).getValueType().toString());
			
			// Sub object
			JsonObject sub = obj.getJsonObject("object");
			
			this.secondary("object-string", sub.getString("string"));
			this.secondary("object-false", sub.getBoolean("false"));
			this.secondary("object-true", sub.getBoolean("true"));
			this.secondary("object-int", sub.getInt("int"));
			this.secondary("object-long",
				sub.getJsonNumber("long").longValue());
			this.secondary("object-null",
				sub.get("null") == JsonValue.NULL);
		}
	}
}

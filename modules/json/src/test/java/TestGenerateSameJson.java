// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import com.oracle.json.JsonObject;
import com.oracle.json.spi.JsonProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.multiphasicapps.tac.TestBoolean;

/**
 * Tests that the same JSON file is generated, that is the writer can write
 * the JSON but then also parse the JSON it wrote.
 *
 * @since 2022/07/12
 */
public class TestGenerateSameJson
	extends TestBoolean
{
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public boolean test()
		throws Throwable
	{
		// Read initial object
		JsonObject first;
		try (InputStream in = TestJsonParse.class
			.getResourceAsStream("sample.json"))
		{
			// Read in the object
			first = JsonProvider.provider()
				.createReader(in).readObject();
		}
		
		// Write byte array
		byte[] rawData;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			JsonProvider.provider().createWriter(baos).write(first);
			
			rawData = baos.toByteArray();
		}
		
		// Read second object
		JsonObject second;
		try (InputStream in = new ByteArrayInputStream(rawData))
		{
			// Read in the object
			second = JsonProvider.provider()
				.createReader(in).readObject();
		}
		
		// These should be the same!
		return first.equals(second);
	}
}

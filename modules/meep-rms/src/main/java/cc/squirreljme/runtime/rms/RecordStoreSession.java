// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.oracle.json.Json;
import com.oracle.json.JsonArray;
import com.oracle.json.JsonNumber;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonReader;
import com.oracle.json.JsonString;
import com.oracle.json.JsonValue;
import com.oracle.json.stream.JsonGenerator;
import com.oracle.json.stream.JsonParsingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreInfo;

/**
 * This contains the session specifically for {@link RecordStore}'s metadata.
 *
 * @since 2025/04/20
 */
@SquirrelJMEVendorApi
public class RecordStoreSession
	extends RecordSession
{
	/** The authentication key. */
	@SquirrelJMEVendorApi
	public static final String AUTHENTICATION =
		"authentication";
	
	/** The other write key. */
	@SquirrelJMEVendorApi
	public static final String OTHER_WRITE =
		"otherWrite";
	
	/** The password key. */
	@SquirrelJMEVendorApi
	public static final String PASSWORD =
		"password";
	
	/** The owner name. */
	@SquirrelJMEVendorApi
	public static final String OWNER_NAME =
		"ownerName";
	
	/** The owner vendor. */
	@SquirrelJMEVendorApi
	public static final String OWNER_VENDOR =
		"ownerVendor";
	
	/** The owner version. */
	@SquirrelJMEVendorApi
	public static final String OWNER_VERSION =
		"ownerVersion";
	
	/** The name of this record. */
	@SquirrelJMEVendorApi
	public static final String RECORD_NAME =
		"recordName";
	
	/** The base name used for files. */
	@SquirrelJMEVendorApi
	public static final String BASE_NAME =
		"baseName";
	
	/** Record IDs. */
	@SquirrelJMEVendorApi
	public static final String IDS =
		"ids";
	
	/** Newly overwritten keys. */
	private final Map<String, JsonValue> _updates =
		new HashMap<>();
	
	/** The read JSON data. */
	private volatile JsonObject _json;
	
	/**
	 * Initializes the session.
	 *
	 * @param __bucket The bucket to access.
	 * @param __fileName The file name of the data.
	 * @param __lock The lock used for access.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordStoreSession(BucketBracket __bucket, String __fileName,
		Object __lock)
		throws NullPointerException
	{
		super(__bucket, __fileName, __lock);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/20
	 */
	@Override
	public void close()
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// If no updates were made, do nothing
			Map<String, JsonValue> updates = this._updates;
			if (updates.isEmpty())
				return;
			
			// Load in the JSON data, if available
			JsonObject base;
			try
			{
				base = this.__load();
			}
			catch (RecordStoreException ignored)
			{
				base = null;
			}
			
			// Write new JSON data with updated fields
			byte[] chunk;
			try (ByteArrayOutputStream raw = new ByteArrayOutputStream(
				1024); JsonGenerator out = Json.createGenerator(raw))
			{
				// Start object
				out.writeStartObject();
				
				// Place in base keys, assuming there is a base
				if (base != null)
					for (Map.Entry<String, JsonValue> entry : base.entrySet())
					{
						// Base key to operate on
						String key = entry.getKey();
						
						// If the value has been updated, do not write yet
						JsonValue updatedVal = updates.get(key);
						if (updatedVal != null)
							out.write(key, updatedVal);
					}
				
				// Write out all updated values
				for (Map.Entry<String, JsonValue> entry : updates.entrySet())
					out.write(entry.getKey(), entry.getValue());
				
				// End object
				out.writeEnd();
				
				// Flush out
				out.flush();
				
				// Get data to write out
				chunk = raw.toByteArray();
			}
			catch (IOException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreException(__e.getMessage()), __e);
			}
			
			// Set data to be written for the record
			this.writeAll(chunk);
			
			// Forward close
			super.close();
		}
	}
	
	/**
	 * Gets the array for the given key.
	 *
	 * @param __key The key to get.
	 * @return The resultant value.
	 * @throws RecordStoreException If the value is not an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public JsonArray getArray(String __key)
		throws RecordStoreException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		return this.getValue(JsonArray.class, __key);
	}
	
	/**
	 * Returns the integer value for a given key or a default value.
	 *
	 * @param __key The key to get the value of.
	 * @param __default The default value to return if it is not set.
	 * @return The resultant integer value.
	 * @throws RecordStoreException If the value is not an integer.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public int getInteger(String __key, int __default)
		throws RecordStoreException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		JsonNumber value = this.getValue(JsonNumber.class, __key);
		if (value != null)
			return value.intValue();
		return __default;
	}
	
	/**
	 * Returns the string value for a given key or a default value.
	 *
	 * @param __key The key to get the value of.
	 * @param __default The default value to return if it is not set.
	 * @return The resultant string value.
	 * @throws RecordStoreException If the value is not a string or could
	 * not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public String getString(String __key, String __default)
		throws RecordStoreException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		JsonString value = this.getValue(JsonString.class, __key);
		if (value != null)
			return value.getString();
		return __default;
	}
	
	/**
	 * Gets a general JSON Value.
	 *
	 * @param <V> The value type.
	 * @param __cl The value type.
	 * @param __key The key to get.
	 * @return The resultant value.
	 * @throws RecordStoreException If the value could not be read or is of
	 * the wrong type.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public <V extends JsonValue> V getValue(Class<V> __cl, String __key)
		throws RecordStoreException, NullPointerException
	{
		if (__cl == null || __key == null)
			throw new NullPointerException("NARG");
		
		synchronized (this.lock)
		{
			// Load in Json
			JsonObject json = this.__load();
			if (json == null)
				return null;
			
			// Overridden?
			JsonValue result = this._updates.get(__key);
			if (result != null)
				try
				{
					return __cl.cast(result);
				}
				catch (ClassCastException __e)
				{
					throw RecordUtils.wrap(
						new RecordStoreException(__e.getMessage()), __e);
				}
			
			// Is there a value here?
			try
			{
				return __cl.cast(json.get(__key));
			}
			catch(ClassCastException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __key The key to set.
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If the key could not be set.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public void set(String __key, int __val)
		throws NullPointerException, RecordStoreException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		synchronized (this.lock)
		{
			this._updates.put(__key,
				Json.createObjectBuilder().add("key", __val)
					.build().get("key"));
		}
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __key The key to set.
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If the key could not be set.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public void set(String __key, String __val)
		throws NullPointerException, RecordStoreException
	{
		if (__key == null || __val == null)
			throw new NullPointerException("NARG");
		
		synchronized (this.lock)
		{
			this._updates.put(__key,
				Json.createObjectBuilder().add("key", __val)
					.build().get("key"));
		}
	}
	
	/**
	 * Loads the stored JSON metadata.
	 *
	 * @return The loaded JSON.
	 * @throws RecordStoreException If the JSON data could not be read.
	 * @since 2025/04/20
	 */
	JsonObject __load()
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// Has the metadata already been read?
			JsonObject result = this._json;
			if (result != null)
				return result;
		
			// Read in meta JSON
			try (ByteArrayInputStream in = this.read();
				JsonReader reader = Json.createReader(in))
			{
				result = reader.readObject();
				
				// Cache it and use it
				this._json = result;
				return result;
			}
			catch (IOException|JsonParsingException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreException(__e.getMessage()), __e);
			}
		}
	}
}

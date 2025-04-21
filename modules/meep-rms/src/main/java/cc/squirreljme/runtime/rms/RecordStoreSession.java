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
import cc.squirreljme.jvm.suite.SuiteIdentifier;
import cc.squirreljme.jvm.suite.SuiteName;
import cc.squirreljme.jvm.suite.SuiteVendor;
import cc.squirreljme.jvm.suite.SuiteVersion;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.oracle.json.Json;
import com.oracle.json.JsonArray;
import com.oracle.json.JsonArrayBuilder;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

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
	
	/** The base name used for files. */
	@SquirrelJMEVendorApi
	public static final String BASE_NAME =
		"baseName";
	
	/** Record IDs. */
	@SquirrelJMEVendorApi
	public static final String IDS =
		"ids";
	
	/** The modification count of the record. */
	@SquirrelJMEVendorApi
	public static final String MODIFICATION_COUNT =
		"modificationCount";
	
	/** The other write key. */
	@SquirrelJMEVendorApi
	public static final String OTHER_WRITE =
		"otherWrite";
	
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
	
	/** The password key. */
	@SquirrelJMEVendorApi
	public static final String PASSWORD =
		"password";
	
	/** The name of this record. */
	@SquirrelJMEVendorApi
	public static final String RECORD_NAME =
		"recordName";
	
	/** Tag prefix. */
	@SquirrelJMEVendorApi
	public static final String TAG_PREFIX =
		"tag:";
	
	/** The base name used for records. */
	@SquirrelJMEVendorApi
	protected final String baseName;
	
	/** Newly overwritten keys. */
	private final Map<String, JsonValue> _updates =
		new HashMap<>();
	
	/** The read JSON data. */
	private volatile JsonObject _json;
	
	/** The name of this record. */
	private volatile String _name;
	
	/** The owner of this record. */
	private volatile SuiteIdentifier _owner;
	
	/**
	 * Initializes the session.
	 *
	 * @param __bucket The bucket to access.
	 * @param __fileName The file name of the data.
	 * @param __lock The lock used for access.
	 * @param __owner The record store owner.
	 * @param __name The recored store name.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordStoreSession(BucketBracket __bucket, String __fileName,
		Object __lock, SuiteIdentifier __owner, String __name)
		throws NullPointerException
	{
		super(__bucket, __fileName, __lock, -1);
		
		// Set these if available
		this._owner = __owner;
		this._name = __name;
		
		// Determine base name
		String fileName = this.fileName;
		int lastDot = fileName.lastIndexOf('.');
		if (lastDot >= 0)
			this.baseName = fileName.substring(0, lastDot);
		else
			this.baseName = fileName;
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
			// Make sure it is flushed
			this.flush();
			
			// Forward close
			super.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/21
	 */
	@Override
	public void flush()
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
			
			// Commit
			super.flush();
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
	 * Returns the tag for a given ID.
	 *
	 * @param __id The ID to get the tag for.
	 * @return The resultant tag.
	 * @throws RecordStoreException If the tag could not be obtained.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public int getTag(int __id)
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			return this.getInteger(RecordStoreSession.TAG_PREFIX + __id,
				0);
		}
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
	 * Returns the available record IDs.
	 *
	 * @return The record IDs.
	 * @throws RecordStoreException If the IDs could not be determined.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public int[] ids()
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// No records at all?
			JsonArray ids = this.getArray(RecordStoreSession.IDS);
			if (ids == null || ids.isEmpty())
				return new int[0];
			
			// Map in IDs
			int n = ids.size();
			int[] result = new int[n];
			for (int i = 0; i < n; i++)
				result[i] = ids.getInt(i, -i);
			
			// Make sure they are always sorted before returning
			Arrays.sort(result);
			return result;
		}
	}
	
	/**
	 * Returns the name of this record.
	 *
	 * @return The record name.
	 * @throws RecordStoreException If this information is not known.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public String name()
		throws RecordStoreException
	{
		// From manifest info?
		synchronized (this.lock)
		{
			// Has this been set from construction?
			String name = this._name;
			if (name != null)
				return null;
			
			// The record store must have a name
			/* {@squirreljme.error AD10 RecordStore has no name.} */
			name = this.getString(RecordStoreSession.RECORD_NAME,
				null);
			if (name == null)
				throw new RecordStoreException(
					__error__("AD11"));
			
			// Cache and use it
			this._name = name;
			return name;
		}
	}
	
	/**
	 * Returns the next ID.
	 *
	 * @param __allocate Should this ID be allocated?
	 * @return The resultant ID.
	 * @throws RecordStoreException If the record could not be allocated.
	 * @since 2025/04/21
	 */
	public int nextId(boolean __allocate)
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// Grab all known IDs
			int[] ids = this.ids();
			
			// Find the next ID which is not taken
			int nextId = 0;
			while (Arrays.binarySearch(ids, nextId) >= 0)
				nextId++;
			
			// If not allocating, return it now
			if (!__allocate)
				return nextId;
			
			// Otherwise, build an array from it
			JsonArrayBuilder builder = Json.createArrayBuilder();
			boolean injected = false;
			for (int i = 0, n = ids.length; i < n; i++)
			{
				// Add existing ID
				builder.add(ids[i]);
				
				// Is this the spot where the ID would be injected?
				if (!injected && nextId > ids[i])
				{
					builder.add(nextId);
					injected = true;
				}
			}
			
			// Was the ID never injected?
			if (!injected)
				builder.add(nextId);
			
			// Store new IDs
			this.set(RecordStoreSession.IDS, builder.build());
			
			// Return the newly allocated ID
			return nextId;
		}
	}
	
	/**
	 * Opens a record with the given ID.
	 *
	 * @param __id The ID to open.
	 * @return The session for the given record.
	 * @throws RecordStoreException If it could not be opened.
	 * @since 2025/04/21
	 */
	public RecordSession open(int __id)
		throws RecordStoreException
	{
		// Cannot be negative
		if (__id < 0)
			throw new RecordStoreException("NEGV");
		
		synchronized (this.lock)
		{
			// This is as simple as opening a new session
			return new RecordSession(this.bucket,
				this.baseName + "." + __id,
				this.lock, __id);
		}
	}
	
	/**
	 * Returns the owner of this suite.
	 *
	 * @return The suite owner.
	 * @throws RecordStoreException If this information is not known.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public SuiteIdentifier owner()
		throws RecordStoreException
	{
		// From manifest info?
		synchronized (this.lock)
		{
			// Has this been set from construction?
			SuiteIdentifier owner = this._owner;
			if (owner != null)
				return null;
			
			// Get all of these values
			String name = this.getString(RecordStoreSession.OWNER_NAME,
				null);
			String vendor = this.getString(RecordStoreSession.OWNER_VENDOR,
				null);
			String version = this.getString(RecordStoreSession.OWNER_VERSION,
				null);
			
			// If any are missing, this is not valid
			/* {@squirreljme.error AD10 RecordStore has no identifier.} */
			if (name == null || vendor == null || version == null)
				throw new RecordStoreException(
					__error__("AD10 %s %s %s", name, vendor, version));
			
			// Build
			owner = new SuiteIdentifier(new SuiteName(name),
				new SuiteVendor(vendor), new SuiteVersion(version));
			
			// Cache and use it
			this._owner = owner;
			return owner;
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
		
		this.set(__key, Json.createObjectBuilder().add("key", __val)
			.build().get("key"));
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
		
		this.set(__key, Json.createObjectBuilder().add("key", __val)
			.build().get("key"));
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __key The key to set.
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If the key could not be set.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void set(String __key, JsonValue __val)
		throws NullPointerException, RecordStoreException
	{
		if (__key == null || __val == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("set(%s, %s)", __key, __val);
		
		synchronized (this.lock)
		{
			// Put in new value
			this._updates.put(__key, __val);
			
			// Also update the modification count, if this is not that
			// otherwise this would recurse infinitely
			if (!__key.equals(RecordStoreSession.MODIFICATION_COUNT))
				this.set(RecordStoreSession.MODIFICATION_COUNT,
					this.getInteger(RecordStoreSession.MODIFICATION_COUNT,
						0) + 1);
			
			// Make sure data is written
			this.flush();
		}
	}
	
	/**
	 * Sets the access mode for this record store.
	 *
	 * @param __auth The authorization to use.
	 * @param __otherWrite If this can be written by others.
	 * @param __pass The password to use.
	 * @throws RecordStoreException If the record could not be opened.
	 * @since 2025/04/16
	 */
	@SquirrelJMEVendorApi
	public void setAccess(int __auth, boolean __otherWrite, String __pass)
		throws RecordStoreException
	{
		
		// Debug
		Debugging.debugNote("setAccess()");
		
		synchronized (this.lock)
		{
			// Write suite information
			SuiteIdentifier owner = this.owner();
			this.set(RecordStoreSession.OWNER_NAME,
				owner.name().toString());
			this.set(RecordStoreSession.OWNER_VENDOR,
				owner.vendor().toString());
			this.set(RecordStoreSession.OWNER_VERSION,
				owner.version().toString());
			
			// Write record information
			this.set(RecordStoreSession.RECORD_NAME,
				this.name());
			
			// Write base name, could be used for recovery?
			this.set(RecordStoreSession.BASE_NAME, this.baseName);
			
			// Write access information
			this.set(RecordStoreSession.AUTHENTICATION, __auth);
			this.set(RecordStoreSession.OTHER_WRITE,
				(__otherWrite ? 1 : 0));
			this.set(RecordStoreSession.PASSWORD,
				(__pass != null ? __pass : ""));
		}
	}
	
	/**
	 * Sets the tag for the given record.
	 *
	 * @param __id The ID of the record to set the tag of.
	 * @param __tag The tag to set.
	 * @throws RecordStoreException If the tag could not be set.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void setTag(int __id, int __tag)
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			this.set(RecordStoreSession.TAG_PREFIX + __id, __tag);
		}
	}
	
	/**
	 * Returns the total size of the record store.
	 *
	 * @return The total size of the record store.
	 * @throws RecordStoreException If the size could not be determined.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public long totalSize()
		throws RecordStoreException
	{
		throw Debugging.todo();
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

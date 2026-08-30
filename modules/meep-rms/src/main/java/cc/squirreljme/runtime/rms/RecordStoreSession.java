// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.jvm.launch.IModeApplication;
import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.StandardBucketType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	/** The current RMS version format. */
	@SquirrelJMEVendorApi
	public static final SuiteVersion CURRENT_RMS_VERSION =
		new SuiteVersion(1, 1, 0);
	
	/** The old DoJa record owner vendor. */
	@SquirrelJMEVendorApi
	public static final String OLD_DOJA_VENDOR =
		"SquirrelJME-DoJa";
	
	/** The version of the record store format. */
	@SquirrelJMEVendorApi
	public static final String RMS_VERSION =
		"rmsVersion";
	
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
	
	/** The last modification time. */
	@SquirrelJMEVendorApi
	public static final String LAST_MODIFIED =
		"lastModified";
	
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
	
	/** Compatible last ID for older MIDP. */
	@SquirrelJMEVendorApi
	public static final String COMPATIBLE_LAST_ID =
		"compatibleLastId";
	
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
	 * @param __name The recorded store name.
	 * @param __readOnly Is this session read-only?
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordStoreSession(BucketBracket __bucket, String __fileName,
		Object __lock, SuiteIdentifier __owner, String __name,
		boolean __readOnly)
		throws NullPointerException
	{
		super(__bucket, __fileName, __lock, -1, __readOnly);
		
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
		// Make sure this is flushed
		this.flush();
		
		// Forward close
		super.close();
	}
	
	/**
	 * Deletes the given record.
	 *
	 * @param __id The record ID to delete.
	 * @throws RecordStoreException If the record could not be deleted.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void delete(int __id)
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// Find index where this ID is
			int[] ids = this.ids();
			int at = 0;
			int n = ids.length;
			for (; at < n; at++)
				if (ids[at] == __id)
					break;
			
			// Not found? Then do nothing
			if (at >= n)
				return;
			
			// Keep other IDs
			JsonArrayBuilder builder = Json.createArrayBuilder();
			for (; at < n; at++)
				if (ids[at] != __id)
					builder.add(ids[at]);
			
			// Set new IDs and clear any tags for it
			this.set(RecordStoreSession.IDS, builder.build());
			
			// Delete the file on disk for that record, ignore failures
			try
			{
				BucketShelf.delete(this.bucket,
					this.baseName + "." + __id);
			}
			catch (MLECallError ignored)
			{
			}
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
		// Do nothing if read-only
		if (this.readOnly)
			return;
		
		if (Debugging.VERBOSE)
			Debugging.debugNote("flush()");
		
		synchronized (this.lock)
		{
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
			Map<String, JsonValue> updates = this._updates;
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
						
						// Only write values which have not been updated
						JsonValue updatedVal = updates.get(key);
						if (updatedVal == null)
							out.write(key, entry.getValue());
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
			
			// Write JSON data to disk
			this.writeAll(chunk);
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
		if (Debugging.VERBOSE)
			Debugging.debugNote("getTag(%d)", __id);
		
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
		
		if (Debugging.VERBOSE)
			Debugging.debugNote("getValue(%s)", __key);
		
		synchronized (this.lock)
		{
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
			
			// Load in Json
			JsonObject json = this.__load();
			if (json == null)
				return null;
			
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
		if (Debugging.VERBOSE)
			Debugging.debugNote("ids()");
		
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
		if (Debugging.VERBOSE)
			Debugging.debugNote("name()");
		
		// From manifest info?
		synchronized (this.lock)
		{
			// Has this been set from construction?
			String name = this._name;
			if (name != null)
				return name;
			
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
	 * @param __compatible Compatibility with older software before MEEP 8
	 * which relies on every ID to be unique.
	 * @return The resultant ID.
	 * @throws RecordStoreException If the record could not be allocated.
	 * @since 2025/04/21
	 */
	public int nextId(boolean __allocate, boolean __compatible)
		throws RecordStoreException
	{
		if (Debugging.VERBOSE)
			Debugging.debugNote("nextId(%b, %b)", __allocate,
				__compatible);
		
		synchronized (this.lock)
		{
			// Grab all known IDs
			int[] ids = this.ids();
			
			// Get the current last compatible ID number
			int compatId = this.getInteger(
				RecordStoreSession.COMPATIBLE_LAST_ID, 1);
			
			// Find the next ID which is not taken, always start at one
			int freeId = 1;
			while (Arrays.binarySearch(ids, freeId) >= 0)
				freeId++;
			
			// Compatibility mode where all new IDs are always higher?
			int useId;
			if (__compatible)
			{
				// Make sure the used ID is truly not used
				useId = Math.max(compatId, freeId);
				while (Arrays.binarySearch(ids, useId) >= 0)
					useId++;
			}
			else
				useId = freeId;
			
			// If not allocating, return it now
			if (!__allocate)
				return useId;
			
			// Otherwise, build an array from it
			JsonArrayBuilder builder = Json.createArrayBuilder();
			boolean injected = false;
			for (int i = 0, n = ids.length; i < n; i++)
			{
				// Add existing ID
				builder.add(ids[i]);
				
				// Is this the spot where the ID would be injected?
				if (!injected && useId > ids[i])
				{
					builder.add(useId);
					injected = true;
				}
			}
			
			// Was the ID never injected?
			if (!injected)
				builder.add(useId);
			
			// Use the highest of all the values for the compatible ID
			this.set(RecordStoreSession.COMPATIBLE_LAST_ID,
				Math.max(useId, Math.max(compatId, freeId)) + 1);
			
			// Store new IDs
			this.set(RecordStoreSession.IDS, builder.build());
			
			// Return the newly allocated ID
			return useId;
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
		
		if (Debugging.VERBOSE)
			Debugging.debugNote("open(%d)", __id);
		
		synchronized (this.lock)
		{
			// This is as simple as opening a new session
			return new RecordSession(this.bucket,
				this.baseName + "." + __id,
				this.lock, __id, false);
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
		if (Debugging.VERBOSE)
			Debugging.debugNote("owner()");
		
		// From manifest info?
		synchronized (this.lock)
		{
			// Has this been set from construction?
			SuiteIdentifier owner = this._owner;
			if (owner != null)
				return owner;
			
			// Get all of these values
			String name = this.getString(RecordStoreSession.OWNER_NAME,
				null);
			String vendor = this.getString(RecordStoreSession.OWNER_VENDOR,
				null);
			String version = "0.0.0";
			
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
	 * Purges this record store.
	 *
	 * @throws RecordStoreException If this could not be purged.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void purge()
		throws RecordStoreException
	{
		synchronized (this)
		{
			// Delete all IDs
			for (int id : this.ids())
				this.delete(id);
			
			// Clear all updates and use a blank JSON as the base
			// When the session is closed, nothing should remain
			this._updates.clear();
			this._json = Json.createObjectBuilder().build();
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
	 * @since 2025/05/29
	 */
	@SquirrelJMEVendorApi
	public void set(String __key, long __val)
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
		
		// Fail if readonly
		if (this.readOnly)
			throw new RecordStoreException("RORO");
		
		// Debug
		if (Debugging.VERBOSE)
			Debugging.debugNote("setValue(%s, %s)", __key, __val);
		
		synchronized (this.lock)
		{
			// Make sure original JSON is loaded
			this.__load();
			
			// If we are trying to write the old DoJa vendor, replace it with
			// the new one instead to migrate any RMS records
			if (__key.equals("ownerVendor") &&
				RecordStoreSession.OLD_DOJA_VENDOR.equals(__val.toString()))
				this._updates.put(__key, Json.createObjectBuilder()
					.add("key", IModeApplication.VENDOR)
					.build().get("key"));
			
			// Put in new value
			else
				this._updates.put(__key, __val);
			
			// Also update the modification count and time, if this is not that
			// otherwise this would recurse infinitely
			if (!__key.equals(RecordStoreSession.MODIFICATION_COUNT) &&
				!__key.equals(RecordStoreSession.LAST_MODIFIED) &&
				!__key.equals(RecordStoreSession.OWNER_VERSION) &&
				!__key.equals(RecordStoreSession.RMS_VERSION))
			{
				// Store the RMS record version always
				this.set(RecordStoreSession.RMS_VERSION,
					RecordStoreSession.CURRENT_RMS_VERSION.toString());
				
				// Set time accordingly
				this.set(RecordStoreSession.MODIFICATION_COUNT,
					this.getInteger(RecordStoreSession.MODIFICATION_COUNT,
						0) + 1);
				this.set(RecordStoreSession.LAST_MODIFIED,
					System.currentTimeMillis());
				
				// Is there an ownerVersion already set? This is ignored by
				// SquirrelJME due to MIDP 3
				String ownerVersion = this.getString(
					RecordStoreSession.OWNER_VERSION, null);
				if (ownerVersion == null)
					this.set(RecordStoreSession.OWNER_VERSION, "0.0.0");
			}
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
		if (Debugging.VERBOSE)
			Debugging.debugNote("setAccess(%d, %b, %s)",
				__auth, __otherWrite, __pass);
		
		synchronized (this.lock)
		{
			// Write suite information
			SuiteIdentifier owner = this.owner();
			this.set(RecordStoreSession.OWNER_NAME,
				owner.name().toString());
			this.set(RecordStoreSession.OWNER_VENDOR,
				owner.vendor().toString());
			
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
		if (Debugging.VERBOSE)
			Debugging.debugNote("setTag(%d, %d)", __id, __tag);
		
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
		long total = 0;
		synchronized (this.lock)
		{
			// Add initial size of self
			total += this.length();
			
			// Count up lengths
			for (int id : this.ids())
				try (RecordSession sub = this.open(id))
				{
					total += sub.length();
					if (total < 0)
						total = Long.MAX_VALUE;
				}
		}
		
		return total;
	}
	
	/**
	 * Checks if this record store actually exists on the disk and is
	 * considered valid.
	 *
	 * @return If this actually exists and valid.
	 * @throws RecordStoreException If this could not be determined.
	 * @since 2025/04/16
	 */
	@SquirrelJMEVendorApi
	public boolean valid()
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// Does not exist on the disk
			if (!BucketShelf.exists(this.bucket, this.fileName))
				return false;
			
			// Length on the disk is too tiny
			if (BucketShelf.length(this.bucket, this.fileName) < 12)
				return false;
			
			// These keys are not set
			if (this.getString(RecordStoreSession.OWNER_NAME,
				null) == null)
				return false;
			
			// Considered valid otherwise
			return true;
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
			
			// Grab all bytes, if there is nothing, use a blank object
			byte[] raw = this.readAll();
			if (raw == null || raw.length == 0)
			{
				// Make a blank object
				result = Json.createObjectBuilder().build();
				
				// Cache it and use it
				this._json = result;
				return result;
			}
		
			// Read in meta JSON
			try (ByteArrayInputStream in = new ByteArrayInputStream(raw);
				JsonReader reader = Json.createReader(in))
			{
				result = reader.readObject();
				
				// Cache it and use it
				this._json = result;
				return result;
			}
			catch (IOException|JsonParsingException __e)
			{
				__e.printStackTrace();
				throw RecordUtils.wrap(
					new RecordStoreException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Iterates and locates all record stores.
	 *
	 * @return All record stores that were found during an iteration.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public static RecordIteration[] locateAll()
	{
		return RecordStoreSession.locateAll(BucketShelf.bucket(
			StandardBucketType.DATA_BUCKET));
	}
	
	/**
	 * Iterates and locates all record stores.
	 *
	 * @return All record stores that were found during an iteration.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public static RecordIteration[] locateAll(BucketBracket __bucket)
		throws NullPointerException
	{
		if (__bucket == null)
			throw new NullPointerException("NARG");
		
		List<RecordIteration> result = new ArrayList<>();
		
		// Go through all items and process them
		Object lock = new Object();
		for (String item : BucketShelf.list(__bucket, false,
			null, null, ".rms"))
			try (RecordStoreSession session = new RecordStoreSession(
				__bucket, item, lock, null, null,
				true))
			{
				// The iteration is essentially how to rebuild it
				result.add(new RecordIteration(__bucket,
					session.baseName,
					session.owner(), session.name()));
			}
			catch (RecordStoreException|NullPointerException ignored)
			{
				// Ignore records considered to be invalid
			}
		
		// Return the result of the iteration, for later enumeration
		return result.toArray(new RecordIteration[result.size()]);
	}
}

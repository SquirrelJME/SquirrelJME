// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.palmos;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to build PalmOS databases and resource databases.
 *
 * @since 2019/07/13
 */
public final class PalmDatabaseBuilder
{
	/** The limit to the name length. */
	private static final int _NAME_LIMIT =
		32;
	
	/** Difference in the epoch in seconds. */
	private static final long _EPOCH_DIFF_SECONDS =
		2082844800L;
	
	/** Difference in the epoch in milliseconds. */
	private static final long _EPOCH_DIFF_MILLISECONDS =
		2082844800_000L;
	
	/** The type of database to create. */
	protected final PalmDatabaseType dbtype;
	
	/** Attributes within the database. */
	private final Set<PalmDatabaseAttribute> _attributes =
		new HashSet<>();
	
	/** The entries within the database. */
	private final List<PalmRecord> _records =
		new ArrayList<>();
	
	/** The creator of the database. */
	private String _creator =
		"????";
	
	/** The type of the database. */
	private String _type =
		"????";
	
	/** The name of the database. */
	private String _name =
		"Untitled";
	
	/** Creation time. */
	private long _createtime =
		System.currentTimeMillis();
	
	/** Modification time */
	private long _modtime =
		this._createtime;
	
	/** Backup time. */
	private long _backuptime =
		0;
	
	/** Modification number. */
	private int _modcount =
		0;
	
	/** The version. */
	private int _version =
		0;
	
	/**
	 * Initializes the database builder.
	 *
	 * @param __type The database type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public PalmDatabaseBuilder(PalmDatabaseType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.dbtype = __type;
	}
	
	/**
	 * Adds the specified entry.
	 *
	 * @param __type The entry type.
	 * @param __id The ID to use.
	 * @return The stream to the entry data.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final OutputStream addEntry(String __type, int __id)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Create a record writer to write there
		return new __RecordWriter__(__type, __id, this._records);
	}
	
	/**
	 * Sets the given attributes.
	 *
	 * @param __a The attribute to set.
	 * @return {@code this}.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setAttributes(
		PalmDatabaseAttribute... __a)
	{
		if (__a == null)
			return this;
		
		// Add the attributes
		Set<PalmDatabaseAttribute> attributes = this._attributes;
		for (PalmDatabaseAttribute attr : __a)
			if (attr != null)
				attributes.add(attr);
		
		// Return self
		return this;
	}
	
	/**
	 * Sets the backup time.
	 *
	 * @param __jt Java milliseconds time.
	 * @return {@code this}.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setBackupTime(long __jt)
	{
		this._backuptime = __jt;
		return this;
	}
	
	/**
	 * Sets the creation time.
	 *
	 * @param __jt Java milliseconds time.
	 * @return {@code this}.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setCreateTime(long __jt)
	{
		this._createtime = __jt;
		return this;
	}
	
	/**
	 * Sets the creator of the database.
	 *
	 * @param __creat The creator to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setCreator(String __creat)
		throws NullPointerException
	{
		if (__creat == null)
			throw new NullPointerException("NARG");
		
		this._creator = __creat;
		return this;
	}
	
	/**
	 * Sets the modification count.
	 *
	 * @param __c The count to use.
	 * @return {@code this}.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setModificationCount(int __c)
	{
		this._modcount = __c;
		return this;
	}
	
	/**
	 * Sets the modification time.
	 *
	 * @param __jt Java milliseconds time.
	 * @return {@code this}.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setModificationTime(long __jt)
	{
		this._modtime = __jt;
		return this;
	}
	
	/**
	 * Sets the name of the database.
	 *
	 * @param __name The name to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this._name = __name;
		return this;
	}
	
	/**
	 * Sets the type of the database.
	 *
	 * @param __type The type to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setType(String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this._type = __type;
		return this;
	}
	
	/**
	 * Sets the version number.
	 *
	 * @param __v The version number.
	 * @return {@code this}.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setVersion(int __v)
	{
		this._version = __v;
		return this;
	}
	
	/**
	 * Returns the byte array representing the database.
	 *
	 * @return The byte array of the database.
	 * @since 2019/07/13
	 */
	public final byte[] toByteArray()
	{
		// Just write to a stream
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Write the database info
			this.writeTo(baos);
			
			// Return the resulting array
			return baos.toByteArray();
		}
		
		/* {@squirreljme.error BP01 Could not write the database.} */
		catch (IOException e)
		{
			throw new RuntimeException("BP01", e);
		}
	}
	
	/**
	 * Writes the database to the output.
	 *
	 * @param __out The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final void writeTo(OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Open data output to write to
		DataOutputStream dos = new DataOutputStream(__out);
		
		// Write the name bytes
		byte[] namebytes = this._name.getBytes("utf-8");
		int namelen = namebytes.length;
		dos.write(namebytes, 0, Math.min(namelen,
			PalmDatabaseBuilder._NAME_LIMIT));
		
		// Pad shorter names
		for (int i = namelen; i < PalmDatabaseBuilder._NAME_LIMIT; i++)
			dos.write(0);
		
		// Determine attribute bit-field
		int attrs = 0;
		for (PalmDatabaseAttribute attr : this._attributes)
			attrs |= attr.bit;
		
		// Make sure the attribute flag is always correct because if it was
		// specified it would make the database not valid to be handled
		PalmDatabaseType dbtype = this.dbtype;
		boolean isrc;
		if ((isrc = (dbtype == PalmDatabaseType.RESOURCE)))
			attrs |= PalmDatabaseAttribute.RESOURCE_DATABASE.bit;
		else
			attrs &= ~PalmDatabaseAttribute.RESOURCE_DATABASE.bit;
		
		// Write attributes
		dos.writeShort(attrs);
		
		// Write version
		dos.writeShort(this._version);
		
		// Creation/Modification/Backup Time (In Mac OS epoch seconds)
		dos.writeInt(
			(int)((this._createtime + PalmDatabaseBuilder._EPOCH_DIFF_MILLISECONDS) / 1000L));
		dos.writeInt(
			(int)((this._modtime + PalmDatabaseBuilder._EPOCH_DIFF_MILLISECONDS) / 1000L));
		dos.writeInt(
			(int)((this._backuptime + PalmDatabaseBuilder._EPOCH_DIFF_MILLISECONDS) / 1000L));
		
		// Modification count
		dos.writeInt(this._modcount);
		
		// Application Info
		dos.writeInt(0);
		
		// Sorting info
		dos.writeInt(0);
		
		// Type
		dos.writeInt(PalmDatabaseBuilder.__fourToInt(this._type));
		
		// Creator
		dos.writeInt(PalmDatabaseBuilder.__fourToInt(this._creator));
		
		// Unique ID
		dos.writeInt(0);
		
		// Next record list (unused in files)
		dos.writeInt(0);
		
		// Need to work with records now
		List<PalmRecord> records = this._records;
		int numrecords = records.size();
		
		// Write record count
		dos.writeShort(numrecords);
		
		// Determine the base offset for entry data
		int offset = 78 + ((isrc ? 10 : 8) * numrecords);
		
		// Write table data for records
		for (int i = 0; i < numrecords; i++)
		{
			PalmRecord pr = records.get(i);
			
			// Resource entry
			if (isrc)
			{
				// Type
				dos.writeInt(PalmDatabaseBuilder.__fourToInt(pr.type));
				
				// ID
				dos.writeShort(pr.id);
				
				// Offset
				dos.writeInt(offset);
			}
			
			// Database entry
			else
			{
				// Offset
				dos.writeInt(offset);
				
				// No attributes
				dos.write(0);
				
				// Unique ID (padding)
				dos.write(0);
				dos.write(0);
				dos.write(0);
			}
			
			// Offset is increased by length
			offset += pr.length;
		}
		
		// Write all records
		for (int i = 0; i < numrecords; i++)
			dos.write(records.get(i)._data);
	}
	
	/**
	 * Converts a four string to an integer.
	 *
	 * @param __four The string to convert.
	 * @return The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	private static int __fourToInt(String __four)
		throws NullPointerException
	{
		if (__four == null)
			throw new NullPointerException("NARG");
		
		// There must be at least four characters
		String use = __four;
		if (__four.length() < 4)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(__four);
			sb.setLength(4);
			use = sb.toString();
		}
		
		// Convert
		return ((__four.charAt(0) & 0xFF) << 24) |
			((__four.charAt(1) & 0xFF) << 16) |
			((__four.charAt(2) & 0xFF) << 8) |
			((__four.charAt(3) & 0xFF));
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.fs;

/**
 * Most filesystems have a means of controlling access to data, these
 * permissions specificy which operations are valid.
 *
 * @since 2016/08/21
 */
public enum NativeAclPermission
{
	/** Append data to a file. */
	APPEND_DATA,
	
	/** Delete file. */
	DELETE,
	
	/** Ability to delete a file/directory contained within the directory. */
	DELETE_CHILD,
	
	/** Can execute the file as an executable or script. */
	EXECUTE,
	
	/** Permitted to read ACLs. */
	READ_ACL,
	
	/** Can read non-ACL file attributes. */
	READ_ATTRIBUTES,
	
	/** Can read the file data. */
	READ_DATA,
	
	/** Can read named attributes. */
	READ_NAMED_ATTRS,
	
	/** Can synchronously access data. */
	SYNCHRONIZE,
	
	/** Can write new ACL permissions. */
	WRITE_ACL,
	
	/** Can write attributes to the file. */
	WRITE_ATTRIBUTES,
	
	/** Can write data to the file. */
	WRITE_DATA,
	
	/** Can write named attributes. */
	WRITE_NAMED_ATTRS,
	
	/** Can change the owner of the file. */
	WRITE_OWNER,
	
	/** End. */
	;
}


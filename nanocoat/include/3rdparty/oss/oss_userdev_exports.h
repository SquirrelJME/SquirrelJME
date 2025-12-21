/*
 * Purpose: Definitions for server applications using the oss_userdev driver.
 *
 * This file is part of the oss_userdev driver included in Open Sound
 * System. However this file is not part of the OSS API. 
 *
 * The ioctl calls defined in this file can only be used in dedicated server
 * applications that provide virtual audio device services to other
 * applications. For example the userdev driver can be used to create virtual
 * audio device that connects to the actual soundcard in another system
 * over internet.
 *
 * Applications that use the client devices will use only the OSS ioctl calls
 * defined in soundcard.h. They cannot use anything from this file.
 */

#ifndef OSS_USERDEV_EXPORTS_H
#define OSS_USERDEV_EXPORTS_H
/*
 * This file is part of Open Sound System
 *
 * Copyright (C) 4Front Technologies 1996-2008.
 *
 * This software is released under the BSD license.
 * See the COPYING file included in the main directory of this source
 * distribution for the license terms and conditions
 */


typedef struct
{
	char name[50]; /* Audio device name to be shown to the users */
	unsigned int flags; /* Not suported for the time being */
#define USERDEV_F_VMIX_ATTACH		0x00000001	/* Attach vmix */
#define USERDEV_F_VMIX_PRECREATE	0x00000002	/* Precreate vmix channels */
#define USERDEV_F_ERROR_ON_NO_CLIENT	0x00000004	/* Return EIO from server read/write if no client is connected. */
#define USERDEV_F_VMIX_PRIVATENODE	0x00000008	/* Create private device file for the client */

	oss_devnode_t devnode;	/* Returns the device file name that clients should open */

	unsigned int match_method;
#define UD_MATCH_ANY			0
#define UD_MATCH_UID			1
#define UD_MATCH_GID			2
#define UD_MATCH_PGID			3
	unsigned int match_key;

	/*
	 * Poll interval in milliseconds. Poll interval determines
	 * the fragment size to be used by the device.
	 */
	unsigned int poll_interval;
} userdev_create_t;

#define USERDEV_MAX_MIXERS	64

typedef struct
{
	char name[16];
	int parent;
	int num;	/* Return parameter */
} userdev_mixgroup_t;

typedef struct
{
	char name[16];
	int parent;
	int num;	/* Return parameter */

	int type;	/* MIXT_* */
	int flags;	/* MIXF_* */
	int index;	/* Index to the values[] array */
	int maxvalue;
	int offset;
	char enum_choises[2048];
  	unsigned char enum_present[32];	/* Mask of allowed enum values */
  	int control_no;		/* SOUND_MIXER_VOLUME..SOUND_MIXER_MIDI */
	int rgbcolor;		/* OSS_RGB_* */
} userdev_mixctl_t;

typedef struct
{
	int values[USERDEV_MAX_MIXERS];
} userdev_mixvalues_t;

#define USERDEV_CREATE_INSTANCE		__SIOWR('u', 1, userdev_create_t)
#define USERDEV_GET_CLIENTCOUNT		__SIOR ('u', 2, int)

#define USERDEV_CREATE_MIXGROUP		__SIOWR('u', 3, userdev_mixgroup_t)
#define USERDEV_CREATE_MIXCTL		__SIOWR('u', 4, userdev_mixctl_t)
#define USERDEV_GET_MIX_CHANGECOUNT	__SIOWR('u', 5, int)
#define USERDEV_SET_MIXERS		__SIOWR('u', 6, userdev_mixvalues_t)
#define USERDEV_GET_MIXERS		__SIOWR('u', 7, userdev_mixvalues_t)

#endif

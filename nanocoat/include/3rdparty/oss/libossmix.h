/*
 * Declaration file for the ossmix library.
 */
/*
 * This file is part of Open Sound System
 *
 * Copyright (C) 4Front Technologies 1996-2008.
 *
 * This software is released under the BSD license.
 * See the COPYING file included in the main directory of this source
 * distribution for the license terms and conditions
 */


typedef struct _ossmix_callback_parm ossmix_callback_parm_t;

typedef void (*ossmix_select_poll_t)(void);
typedef void (*ossmix_callback_t)(ossmix_callback_parm_t *parms);

extern int ossmix_init(void);
extern void ossmix_close(void);
extern void ossmix_timertick(void);

extern int ossmix_connect(const char *hostname, int port);
extern int ossmix_get_fd(ossmix_select_poll_t *cb);
extern void ossmix_set_callback(ossmix_callback_t cb);
extern void ossmix_disconnect(void);

extern int ossmix_get_nmixers(void);
extern int ossmix_get_mixerinfo(int mixernum, oss_mixerinfo *mi);

extern int ossmix_open_mixer(int mixernum);
extern void ossmix_close_mixer(int mixernum);
extern int ossmix_get_nrext(int mixernum);
extern int ossmix_get_nodeinfo(int mixernum, int node, oss_mixext *ext);
extern int ossmix_get_enuminfo(int mixernum, int node, oss_mixer_enuminfo *ei);
extern int ossmix_get_description(int mixernum, int node, oss_mixer_enuminfo *desc);

extern int ossmix_get_value(int mixernum, int ctl, int timestamp);
extern void ossmix_set_value(int mixernum, int ctl, int timestamp, int value);

struct _ossmix_callback_parm
{
	int event;
#define OSSMIX_EVENT_VALUE	1000001	// p1=mixnum, p2=node, p3=value
#define OSSMIX_EVENT_NEWMIXER	1000002	// p1=nmixers

	int p1, p2, p3, p4, p5;
};

#ifdef OSSMIX_REMOTE
/*
 * This block contains definitions that cannot be used by client applications
 */

typedef struct
{
	int cmd;
#define OSSMIX_CMD_HALOO			0	/* Haloo means hello in finnish */
#define OSSMIX_CMD_OK				0	
#define OSSMIX_CMD_ERROR			-1
#define OSSMIX_CMD_INIT				1
#define OSSMIX_CMD_EXIT				2
#define OSSMIX_CMD_GET_NMIXERS			3
#define OSSMIX_CMD_GET_MIXERINFO		4
#define OSSMIX_CMD_OPEN_MIXER			5
#define OSSMIX_CMD_CLOSE_MIXER			6
#define OSSMIX_CMD_GET_NREXT			7
#define OSSMIX_CMD_GET_NODEINFO			8
#define OSSMIX_CMD_GET_ENUMINFO			9
#define OSSMIX_CMD_GET_DESCRIPTION		10
#define OSSMIX_CMD_GET_VALUE			11
#define OSSMIX_CMD_SET_VALUE			12
#define OSSMIX_CMD_GET_ALL_VALUES		13
#define OSSMIX_CMD_START_EVENTS			14

	int p1, p2, p3, p4, p5;

#define OSSMIX_P1_MAGIC				0x12345678
	
	int ack_rq;
	int unsolicited;
	int payload_size;
} ossmix_commad_packet_t;
#define MAX_NODES	32
#define MAX_TMP_MIXER	64	/* Must be multiple of 8 */
#define MAX_TMP_NODES	1024	/* Must be multiple of 8 */

typedef struct
{
	int node;
	int value;
} value_record_t;

typedef value_record_t value_packet_t[MAX_TMP_NODES];

typedef struct
{
	int nrext;
	oss_mixext *nodes[MAX_TMP_NODES];
	int values[MAX_TMP_NODES];
	unsigned char changemask[MAX_TMP_NODES/8];
} local_mixer_t;

extern void mixc_add_node(int mixernum, int node, oss_mixext *ext);
extern oss_mixext *mixc_get_node(int mixernum, int node);
extern void mixc_set_value(int mixernum, int node, int value);
extern void mixc_clear_changeflags(int mixernum);
extern int mixc_get_value(int mixernum, int node);
extern int mixc_get_all_values(int mixernum, value_packet_t value_packet, int changechec);
#endif

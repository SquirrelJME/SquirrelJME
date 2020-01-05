# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Makefile for building SquirrelJME RatufaCoat.

CFLAGS=-g1 -O3 -Wall -Wextra
FILES=sjmerc.c sjmercnm.c sjmercvm.c sys_stdc.c

squirreljme-stdc:	$(FILES)
			$(CC) -o $@ $^ -DSQUIRRELJME_PORT_STDC $(CFLAGS) $(LIBS)

clean:				
			rm -f squirreljme-stdc sjmerc.o sys_stdc.o

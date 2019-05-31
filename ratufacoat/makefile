# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Makefile for building SquirrelJME RatufaCoat.

CC=gcc

ratufacoat:	rc_main.o rc_args.o rci_stdc.o
		$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

%.o:		%.c
		$(CC) -c -o $@ $< $(CFLAGS)

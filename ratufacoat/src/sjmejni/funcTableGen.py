# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Script that turns function table CSV to source code

import csv


class FuncTableEntry:
	def __init__(self, row_entry):
		self.table = row_entry['table']
		self.index = int(row_entry['index'])
		self.function = row_entry['function']
		self.returnType = row_entry['returnType']

		# Read in arguments
		self.args = []
		try:
			for i in range(0, 99):
				item = row_entry['arg' + str(i)]
				if item is not None and len(item) > 0:
					self.args.append(row_entry['arg' + str(i)])
		except KeyError:
			pass

	def __repr__(self):
		return str([self.table, str(self.index), self.function if self.function is not None else "null", self.returnType if self.returnType is not None else "null", self.args])


# Header start
header_intro = '/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-\n\
// ---------------------------------------------------------------------------\n\
// SquirrelJME\n\
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>\n\
// ---------------------------------------------------------------------------\n\
// SquirrelJME is under the GNU General Public License v3+, or later.\n\
// See license.mkd for licensing and copyright information.\n\
// -------------------------------------------------------------------------*/\n\
\n'

# Setup file load
csvfile = open('funcTable.csv', newline='')
reader = csv.DictReader(csvfile)

# Read in each row and fill a mapping accordingly
entries = []
entries_by_tab_dex = {}#dict[str, dict[int, entries]]
for row in reader:
	# Load in entry
	current = FuncTableEntry(row)
	entries.append(current)

	# Setup major table for the group
	if entries_by_tab_dex.get(current.table) is None:
		entries_by_tab_dex[current.table] = {}
	major_table = entries_by_tab_dex.get(current.table)

	# Add entries to sub-table
	major_table[current.index] = current

# Go through each table and generate source code for it
for table_name in entries_by_tab_dex:
	index_tables = entries_by_tab_dex[table_name]

	# Temporary strings for header/source files
	header_function_prototypes = '' + header_intro
	header_struct_fields = '' + header_intro

	# Handle each index accordingly
	try:
		for index in range(0, 999):
			# Load in index
			entry: FuncTableEntry = index_tables[index]

			# Nulls are blank specials
			if entry.function == 'NULL':
				header_struct_fields += '/** Do not use, reserved. */\n'
				header_struct_fields += 'void* reserved%d,\n\n' % entry.index

			# Otherwise do normal generation logic
			else:
				print(entry)
	except KeyError:
		pass

	print(header_struct_fields)
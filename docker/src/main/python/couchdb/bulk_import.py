import json
import couchdb

# Connection parameters
COUCHDB_PROTOCOL = 'http'
COUCHDB_HOST     = 'localhost'
COUCHDB_PORT     = '5984'
COUCHDB_USER     = 'changeme'
COUCHDB_PASS     = 'changeme'
DB_NAME          = 'company'

# Connect (with Basic Auth) and select/create the database
server = couchdb.Server(
   f'{COUCHDB_PROTOCOL}://{COUCHDB_USER}:{COUCHDB_PASS}@{COUCHDB_HOST}:{COUCHDB_PORT}/'
)
if DB_NAME not in server:
    server.create(DB_NAME)
db = server[DB_NAME]

# Load existing matrices and branchesfrom JSON
with open('headquarters.json', 'r', encoding='utf-8') as f:
    docs = json.load(f)
with open('branches.json', 'r', encoding='utf-8') as f:
    docs += json.load(f)

# Set their _id as duns
for doc in docs:
    doc['_id'] = doc['duns']

# Bulk insert
#    - db.update() uses _bulk_docs
#    - retorns tuple (id, rev) list or raises in failures
results = db.update(docs)

# Print a summary of successes/failures
for success, docid, rev_or_exc in results:
    if success:
        print(f'OK:   {docid} → {rev_or_exc}')
    else:
        print(f'FAIL: {docid} → {rev_or_exc}')

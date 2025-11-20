import json
import re
import sys
import couchdb
from pathlib import Path


# -------------------------------
# Configuration
# -------------------------------
COUCHDB_PROTOCOL = 'http'
COUCHDB_HOST     = 'localhost'
COUCHDB_PORT     = '5984'
COUCHDB_USER     = 'changeme'
COUCHDB_PASS     = 'changeme'
DB_NAME          = 'company'

HEADQUARTERS_JSON = Path('headquarters.json')
BRANCHES_JSON     = Path('branches.json')

# -------------------------------
# Helpers
# -------------------------------
DIGITS_ONLY = re.compile(r'\D+')  # remove everything that is not a digit

def normalize_duns(duns: str) -> str:
    """
    Remove hyphens and any non-digit characters from DUNS.
    Example: '000-633-159-0001' -> '0006331590001'
    """
    if not duns:
        return ""
    return DIGITS_ONLY.sub("", str(duns))

def load_docs(*json_paths: Path) -> list[dict]:
    """Load and concatenate JSON arrays from the given files."""
    docs: list[dict] = []
    for p in json_paths:
        if not p.exists():
            raise FileNotFoundError(f"JSON file not found: {p}")
        with p.open('r', encoding='utf-8') as f:
            part = json.load(f)
            if not isinstance(part, list):
                raise ValueError(f"JSON root must be a list in {p}")
            docs += part
    return docs

# -------------------------------
# Main
# -------------------------------
def main() -> int:
    # Connect to CouchDB (Basic Auth) and select/create the database
    server = couchdb.Server(
        f'{COUCHDB_PROTOCOL}://{COUCHDB_USER}:{COUCHDB_PASS}@{COUCHDB_HOST}:{COUCHDB_PORT}/'
    )
    if DB_NAME not in server:
        server.create(DB_NAME)
        print(f"Created database: {DB_NAME}")
    db = server[DB_NAME]

    # Load input documents
    docs = load_docs(HEADQUARTERS_JSON, BRANCHES_JSON)

    # Prepare: set _id as normalized DUNS and validate
    prepared: list[dict] = []
    seen_ids: set[str] = set()

    for idx, doc in enumerate(docs, start=1):
        original_duns = doc.get('duns', '')
        nid = normalize_duns(original_duns)

        if not nid:
            raise ValueError(f"Missing/invalid DUNS in document #{idx}: {doc}")

        if nid in seen_ids:
            # If you prefer skipping duplicates instead of raising, replace by `continue`
            raise ValueError(f"Duplicate _id (normalized DUNS) in input: {nid}")
        seen_ids.add(nid)

        # Set _id and keep the original 'duns' field as-is
        doc['_id'] = nid
        prepared.append(doc)

    # Sort by _id ascending (numeric order)
    # Using int(_id) preserves the expected numeric ordering even with different lengths.
    prepared.sort(key=lambda d: int(d['_id']))

    # Bulk insert/update
    # - db.update() performs a _bulk_docs call.
    # - Each result item is a tuple: (success: bool, id: str, rev_or_exc: str|Exception)
    results = db.update(prepared)

    # Print a summary
    ok = fail = 0
    for success, docid, rev_or_exc in results:
        if success:
            ok += 1
            print(f'OK:   {docid} → {rev_or_exc}')
        else:
            fail += 1
            print(f'FAIL: {docid} → {rev_or_exc}')

    print(f"\nSummary: {ok} succeeded, {fail} failed.")
    return 0 if fail == 0 else 1


if __name__ == "__main__":
    try:
        sys.exit(main())
    except Exception as exc:
        print(f"ERROR: {exc}", file=sys.stderr)
        sys.exit(2)

"""
Sitaram Ayurveda Medicine Catalogue — Central Database Layer (SQLite)
Shared backend persistence for the Admin Website and future Android applications.
"""

import sqlite3
import os
import hashlib
import secrets
import json
from datetime import datetime, timedelta

DB_PATH = os.path.abspath("./data/sitaram.db")

def get_connection():
    os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)
    conn = sqlite3.connect(DB_PATH, check_same_thread=False)
    conn.row_factory = sqlite3.Row
    return conn

def hash_password(password: str, salt: str = None) -> tuple:
    if not salt:
        salt = secrets.token_hex(16)
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode('utf-8'),
        salt.encode('utf-8'),
        100000
    )
    return key.hex(), salt

def verify_password(password: str, salt: str, password_hash: str) -> bool:
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode('utf-8'),
        salt.encode('utf-8'),
        100000
    )
    return key.hex() == password_hash

def init_db():
    conn = get_connection()
    c = conn.cursor()

    # 1. Admins Table
    c.execute("""
    CREATE TABLE IF NOT EXISTS admins (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT UNIQUE NOT NULL,
        email TEXT UNIQUE NOT NULL,
        password_hash TEXT NOT NULL,
        salt TEXT NOT NULL,
        name TEXT NOT NULL,
        role TEXT NOT NULL,
        avatar TEXT,
        created_at TEXT NOT NULL
    )
    """)

    # 2. Server Sessions Table
    c.execute("""
    CREATE TABLE IF NOT EXISTS sessions (
        token TEXT PRIMARY KEY,
        admin_id INTEGER NOT NULL,
        created_at TEXT NOT NULL,
        expires_at TEXT NOT NULL,
        FOREIGN KEY (admin_id) REFERENCES admins (id) ON DELETE CASCADE
    )
    """)

    # 3. Categories Table (24 Classical Handbook Categories)
    c.execute("""
    CREATE TABLE IF NOT EXISTS categories (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT UNIQUE NOT NULL,
        code TEXT UNIQUE NOT NULL,
        description TEXT,
        sort_order INTEGER NOT NULL,
        status TEXT NOT NULL DEFAULT 'Active'
    )
    """)

    # 4. Products Table
    c.execute("""
    CREATE TABLE IF NOT EXISTS products (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        code TEXT UNIQUE NOT NULL,
        name TEXT NOT NULL,
        category_id INTEGER NOT NULL,
        classical_reference TEXT,
        packings_json TEXT NOT NULL,
        ingredients_json TEXT NOT NULL,
        usage TEXT,
        indications TEXT,
        description TEXT,
        image_url TEXT,
        status TEXT NOT NULL DEFAULT 'Active',
        featured INTEGER NOT NULL DEFAULT 0,
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        FOREIGN KEY (category_id) REFERENCES categories (id)
    )
    """)

    # 5. Ingredients Master Table
    c.execute("""
    CREATE TABLE IF NOT EXISTS ingredients (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT UNIQUE NOT NULL,
        botanical_name TEXT,
        sanskrit_name TEXT,
        part_used TEXT
    )
    """)

    # 6. Manufacturers Table
    c.execute("""
    CREATE TABLE IF NOT EXISTS manufacturers (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        code TEXT UNIQUE NOT NULL,
        license TEXT,
        contact_person TEXT,
        email TEXT,
        phone TEXT,
        address TEXT,
        status TEXT NOT NULL DEFAULT 'Active'
    )
    """)

    # 7. Audit Logs Table
    c.execute("""
    CREATE TABLE IF NOT EXISTS audit_logs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        admin_id INTEGER,
        admin_email TEXT NOT NULL,
        action TEXT NOT NULL,
        entity_type TEXT NOT NULL,
        entity_id TEXT,
        details TEXT NOT NULL,
        ip_address TEXT,
        timestamp TEXT NOT NULL
    )
    """)

    conn.commit()

    # Seed Default Administrators if none exist
    c.execute("SELECT COUNT(*) FROM admins")
    if c.fetchone()[0] == 0:
        now = datetime.utcnow().isoformat()
        
        # User requested admin: sys.jerin@gmail.com
        pwd_hash1, salt1 = hash_password("admin123")
        c.execute("""
            INSERT INTO admins (username, email, password_hash, salt, name, role, avatar, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """, ("jerin_admin", "sys.jerin@gmail.com", pwd_hash1, salt1, "Jerin Administrator", "Lead Systems Administrator", "", now))

        # Primary Sitaram Administrator
        pwd_hash2, salt2 = hash_password("Sitaram@1921")
        c.execute("""
            INSERT INTO admins (username, email, password_hash, salt, name, role, avatar, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """, ("sitaram_admin", "admin@sitaramayurveda.com", pwd_hash2, salt2, "Dr. D. Ramanathan", "Chief Medical Administrator", "", now))
        
        conn.commit()

    # Seed Categories if none exist
    c.execute("SELECT COUNT(*) FROM categories")
    if c.fetchone()[0] == 0:
        handbook_categories = [
            ("Arishtam", "ARI", "Self-generated herbal fermented elixirs and tonics.", 1),
            ("Asavam", "ASA", "Fermented infusions prepared without boiling water.", 2),
            ("Arkam", "ARK", "Distilled herbal extracts capturing essential volatile actives.", 3),
            ("Bhasmas / Ksharams", "BHA", "Calcined mineral preparations and alkaline botanical ashes.", 4),
            ("Choornams", "CHO", "Micro-pulverized medicated herbal powders.", 5),
            ("Gulika, Gulika Tablets, Capsules", "GUL", "Classical pills, compressed tablets and veg capsules.", 6),
            ("Single Herb Veg Capsules", "SHC", "Pure single botanical standard extracts in vegetarian shells.", 7),
            ("Kashayams", "KAS", "Concentrated classical herbal decoctions.", 8),
            ("Kashayam Tablets", "KTB", "Aqueous decoctions spray-dried into convenient tablets.", 9),
            ("Preservative Free Kashayam Sachet", "KSC", "Pure vacuum-sealed decoctions with zero artificial preservatives.", 10),
            ("Lehyams", "LEH", "Semi-solid nutritive herbal jams prepared in raw jaggery and ghee.", 11),
            ("Ghruthams", "GHR", "Medicated cow's ghee preparations traversing the blood-brain barrier.", 12),
            ("Avartis", "AVA", "Repeatedly potentiated herbal lipid formulations (e.g. 101 Avarti).", 13),
            ("Soft Gel Capsules", "SGC", "Lipid soluble classical medicated oils encapsulated for exact dosing.", 14),
            ("Seviyams / Vasthi Thailams", "SVT", "Internal administration and panchakarma enema oils.", 15),
            ("Erand", "ERA", "Purified castor-oil based formulations for deep purgation and vata.", 16),
            ("Tailams / Keratailams", "THI", "Classical medicated sesame and coconut oils for abhyanga and shirodhara.", 17),
            ("Kuzhambu", "KUZ", "Viscous poly-herbal lipid formulations for musculoskeletal disorders.", 18),
            ("Lepam", "LEP", "Medicated herbal pastes for external dermatological application.", 19),
            ("Ointments / Creams", "OIN", "Modern topical emollient bases infused with classical actives.", 20),
            ("Patent / Proprietary Formulations", "PAT", "Research-backed proprietary clinical formulations by Sitaram.", 21),
            ("Drops", "DRP", "Nasal (Nasya), ear (Karnapoorana) and eye drop formulations.", 22),
            ("Syrups / Tonics", "SYR", "Palatable sweet medicinal syrups for pediatric and geriatric care.", 23),
            ("Miscellaneous Products", "MIS", "General wellness and traditional Ayurvedic adjuncts.", 24)
        ]
        for name, code, desc, order in handbook_categories:
            c.execute("""
                INSERT INTO categories (name, code, description, sort_order, status)
                VALUES (?, ?, ?, ?, 'Active')
            """, (name, code, desc, order))
        conn.commit()

    # Seed Manufacturers
    c.execute("SELECT COUNT(*) FROM manufacturers")
    if c.fetchone()[0] == 0:
        c.execute("""
            INSERT INTO manufacturers (name, code, license, contact_person, email, phone, address, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, 'Active')
        """, (
            "Sitaram Ayurveda Pvt. Ltd. (Main Unit)",
            "SAT-KL-01",
            "AYUSH-GMP/KL/2004/0018",
            "Dr. V. Radhakrishnan",
            "production@sitaramayurveda.com",
            "+91 487 2381238",
            "Round South, Thrissur, Kerala - 680001, India"
        ))
        conn.commit()

    # Seed Initial Products if empty
    c.execute("SELECT COUNT(*) FROM products")
    if c.fetchone()[0] == 0:
        c.execute("SELECT id FROM categories WHERE name = 'Arishtam'")
        cat_row = c.fetchone()
        cat_id = cat_row[0] if cat_row else 1
        now = datetime.utcnow().isoformat()

        initial_formulations = [
            (
                "SA-00001",
                "Abhayarishtam",
                cat_id,
                "Ashtangahrudayam, Arshorogadhikaram",
                json.dumps(["450 ml", "200 ml"]),
                json.dumps(["Abhaya (Terminalia chebula)", "Dhatri (Emblica officinalis)", "Kapitha (Feronia elephantum)", "Vishala (Citrullus colocynthis)"]),
                "15 to 25 ml twice daily after meals with equal quantity of warm water.",
                "Arshas (Hemorrhoids), Udara (Abdominal disorders), Vibanda (Constipation), Agnimandya (Impaired digestion).",
                "Classic Ayurvedic fermented formulation indicated primarily for hemorrhoids, sluggish digestion, and chronic constipation.",
                "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=600",
                "Active",
                1
            ),
            (
                "SA-00002",
                "Amritarishtam",
                cat_id,
                "Bhaishajya Ratnavali, Jwaradhikaram",
                json.dumps(["450 ml"]),
                json.dumps(["Amrita / Guduchi (Tinospora cordifolia)", "Bilva (Aegle marmelos)", "Agnimantha (Premna integrifolia)", "Shyonaka (Oroxylum indicum)"]),
                "15 to 25 ml twice daily after food.",
                "Jwara (Chronic & intermittent fevers), Jeerna Jwara, Ajeerna, Yakrit roga (Hepatic sluggishness).",
                "Potent immunomodulatory elixir that detoxifies Ama, strengthens hepatic function, and relieves recurrent pyrexia.",
                "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=600",
                "Active",
                1
            ),
            (
                "SA-00003",
                "Ashokarishtam",
                cat_id,
                "Bhaishajya Ratnavali, Pradaradhikaram",
                json.dumps(["450 ml", "200 ml"]),
                json.dumps(["Ashoka (Saraca asoca)", "Dhataki (Woodfordia fruticosa)", "Musta (Cyperus rotundus)", "Haritaki (Terminalia chebula)"]),
                "15 to 25 ml twice daily after food or as directed by the physician.",
                "Asrigdara (Menorrhagia), Pradara (Leucorrhea), Katishoola (Low back pain), Shweta Pradara.",
                "Classical uterine tonic indicated for hormonal harmony, excessive menstrual bleeding, and pelvic comfort.",
                "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=600",
                "Active",
                0
            )
        ]

        for p in initial_formulations:
            c.execute("""
                INSERT INTO products (code, name, category_id, classical_reference, packings_json, ingredients_json, usage, indications, description, image_url, status, featured, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (*p, now, now))
        
        conn.commit()

    # Seed initial botanicals
    c.execute("SELECT COUNT(*) FROM ingredients")
    if c.fetchone()[0] == 0:
        botanicals = [
            ("Abhaya", "Terminalia chebula", "Haritaki", "Fruit rind"),
            ("Amrita / Guduchi", "Tinospora cordifolia", "Guduchi", "Stem"),
            ("Ashoka", "Saraca asoca", "Ashoka", "Stem bark"),
            ("Dhatri", "Emblica officinalis", "Amalaki", "Pericarp"),
            ("Bilva", "Aegle marmelos", "Bilva", "Root / Fruit"),
            ("Musta", "Cyperus rotundus", "Mustaka", "Rhizome")
        ]
        for b in botanicals:
            c.execute("""
                INSERT INTO ingredients (name, botanical_name, sanskrit_name, part_used)
                VALUES (?, ?, ?, ?)
            """, b)
        conn.commit()

    conn.close()

# Auth Helpers
def authenticate_admin(identifier, password):
    conn = get_connection()
    c = conn.cursor()
    clean_id = (identifier or "").strip().lower()
    c.execute("SELECT id, username, email, password_hash, salt, name, role, avatar FROM admins WHERE lower(username) = ? OR lower(email) = ?", (clean_id, clean_id))
    row = c.fetchone()
    if not row:
        conn.close()
        return None

    if verify_password(password, row['salt'], row['password_hash']):
        admin_data = {
            "id": row['id'],
            "username": row['username'],
            "email": row['email'],
            "name": row['name'],
            "role": row['role'],
            "avatar": row['avatar'] or "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=120"
        }
        conn.close()
        return admin_data
    conn.close()
    return None

def create_session(admin_id, duration_hours=24):
    conn = get_connection()
    c = conn.cursor()
    token = "sat_sec_" + secrets.token_urlsafe(32)
    now = datetime.utcnow()
    expires = (now + timedelta(hours=duration_hours)).isoformat()
    c.execute("""
        INSERT INTO sessions (token, admin_id, created_at, expires_at)
        VALUES (?, ?, ?, ?)
    """, (token, admin_id, now.isoformat(), expires))
    conn.commit()
    conn.close()
    return token, expires

def get_session_admin(token):
    if not token:
        return None
    conn = get_connection()
    c = conn.cursor()
    now = datetime.utcnow().isoformat()
    c.execute("""
        SELECT a.id, a.username, a.email, a.name, a.role, a.avatar, s.expires_at
        FROM sessions s
        JOIN admins a ON s.admin_id = a.id
        WHERE s.token = ? AND s.expires_at > ?
    """, (token, now))
    row = c.fetchone()
    conn.close()
    if row:
        return {
            "id": row['id'],
            "username": row['username'],
            "email": row['email'],
            "name": row['name'],
            "role": row['role'],
            "avatar": row['avatar'] or "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=120"
        }
    return None

def destroy_session(token):
    if not token:
        return
    conn = get_connection()
    c = conn.cursor()
    c.execute("DELETE FROM sessions WHERE token = ?", (token,))
    conn.commit()
    conn.close()

def log_audit(admin_email, action, entity_type, entity_id, details, ip="127.0.0.1"):
    conn = get_connection()
    c = conn.cursor()
    c.execute("""
        INSERT INTO audit_logs (admin_email, action, entity_type, entity_id, details, ip_address, timestamp)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """, (admin_email, action, entity_type, str(entity_id), details, ip, datetime.utcnow().isoformat()))
    conn.commit()
    conn.close()

# Dashboard Queries
def get_dashboard_metrics():
    conn = get_connection()
    c = conn.cursor()

    c.execute("SELECT COUNT(*) FROM products")
    total_products = c.fetchone()[0]

    c.execute("SELECT COUNT(*) FROM products WHERE status = 'Active'")
    active_products = c.fetchone()[0]

    c.execute("SELECT COUNT(*) FROM products WHERE status = 'Inactive'")
    inactive_products = c.fetchone()[0]

    c.execute("SELECT COUNT(*) FROM categories")
    categories_count = c.fetchone()[0]

    c.execute("SELECT COUNT(*) FROM ingredients")
    ingredients_count = c.fetchone()[0]

    c.execute("SELECT COUNT(*) FROM manufacturers")
    manufacturers_count = c.fetchone()[0]

    conn.close()

    return {
        "totalProducts": total_products,
        "activeProducts": active_products,
        "inactiveProducts": inactive_products,
        "categories": categories_count,
        "ingredients": ingredients_count,
        "manufacturers": manufacturers_count
    }

def get_recent_products(limit=5):
    conn = get_connection()
    c = conn.cursor()
    c.execute("""
        SELECT p.id, p.code, p.name, c.name as category_name, p.status, p.created_at
        FROM products p
        JOIN categories c ON p.category_id = c.id
        ORDER BY p.id DESC
        LIMIT ?
    """, (limit,))
    rows = c.fetchall()
    conn.close()
    return [dict(r) for r in rows]

def get_recently_updated_products(limit=5):
    conn = get_connection()
    c = conn.cursor()
    c.execute("""
        SELECT p.id, p.code, p.name, c.name as category_name, p.status, p.updated_at
        FROM products p
        JOIN categories c ON p.category_id = c.id
        ORDER BY p.updated_at DESC
        LIMIT ?
    """, (limit,))
    rows = c.fetchall()
    conn.close()
    return [dict(r) for r in rows]

def get_category_summary():
    conn = get_connection()
    c = conn.cursor()
    c.execute("""
        SELECT c.id, c.name, c.code, c.description, c.sort_order,
               COUNT(p.id) as product_count
        FROM categories c
        LEFT JOIN products p ON c.id = p.category_id
        GROUP BY c.id
        ORDER BY c.sort_order ASC
    """)
    rows = c.fetchall()
    conn.close()
    return [dict(r) for r in rows]

# Initialize tables on load
init_db()

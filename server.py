"""
Sitaram Ayurveda Medicine Catalogue — Administrative Web Server & Auth Gateway
Enforces strict backend authentication, session cookies, and route redirection.
"""

import http.server
import socketserver
import os
import json
import urllib.parse
from http import cookies
import backend.db as db

PORT = 3000
PUBLIC_DIR = os.path.abspath("./public")

class SitaramAdminHandler(http.server.BaseHTTPRequestHandler):
    def get_session_token(self):
        # 1. Check Authorization Bearer header
        auth_header = self.headers.get("Authorization")
        if auth_header and auth_header.startswith("Bearer "):
            return auth_header.split(" ", 1)[1].strip()

        # 2. Check Cookie
        cookie_header = self.headers.get("Cookie")
        if cookie_header:
            c = cookies.SimpleCookie()
            try:
                c.load(cookie_header)
                if "sitaram_session" in c:
                    return c["sitaram_session"].value
            except Exception:
                pass
        return None

    def get_current_admin(self):
        token = self.get_session_token()
        if not token:
            return None
        return db.get_session_admin(token)

    def send_json(self, status_code, data, extra_headers=None):
        payload = json.dumps(data).encode("utf-8")
        self.send_response(status_code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(payload)))
        self.send_header("Cache-Control", "no-store, no-cache, must-revalidate")
        if extra_headers:
            for k, v in extra_headers.items():
                self.send_header(k, v)
        self.end_headers()
        self.wfile.write(payload)

    def send_redirect(self, target_url, extra_headers=None):
        self.send_response(302)
        self.send_header("Location", target_url)
        self.send_header("Cache-Control", "no-store, no-cache, must-revalidate")
        if extra_headers:
            for k, v in extra_headers.items():
                self.send_header(k, v)
        self.end_headers()

    def serve_static_file(self, filepath, content_type=None):
        if not os.path.exists(filepath) or os.path.isdir(filepath):
            self.send_error(404, "File Not Found")
            return

        if not content_type:
            ext = os.path.splitext(filepath)[1].lower()
            types = {
                ".html": "text/html; charset=utf-8",
                ".js": "application/javascript; charset=utf-8",
                ".css": "text/css; charset=utf-8",
                ".json": "application/json; charset=utf-8",
                ".png": "image/png",
                ".jpg": "image/jpeg",
                ".jpeg": "image/jpeg",
                ".svg": "image/svg+xml",
                ".ico": "image/x-icon"
            }
            content_type = types.get(ext, "application/octet-stream")

        with open(filepath, "rb") as f:
            content = f.read()

        self.send_response(200)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Length", str(len(content)))
        self.send_header("Cache-Control", "no-cache")
        self.end_headers()
        self.wfile.write(content)

    def read_json_body(self):
        try:
            content_len = int(self.headers.get("Content-Length", 0))
            if content_len > 0:
                raw = self.rfile.read(content_len).decode("utf-8")
                return json.loads(raw)
        except Exception:
            pass
        return {}

    def do_GET(self):
        parsed = urllib.parse.urlparse(self.path)
        path = parsed.path

        # 1. API Endpoints
        if path.startswith("/api/"):
            # Check session status
            if path == "/api/auth/session":
                admin = self.get_current_admin()
                if admin:
                    self.send_json(200, {"authenticated": True, "admin": admin})
                else:
                    self.send_json(401, {"authenticated": False, "message": "No active administrator session."})
                return

            # Protected API endpoints
            admin = self.get_current_admin()
            if not admin:
                self.send_json(401, {"error": "Unauthorized. Please authenticate as an administrator."})
                return

            if path == "/api/dashboard/metrics":
                metrics = db.get_dashboard_metrics()
                self.send_json(200, {"success": True, "data": metrics})
                return

            elif path == "/api/dashboard/recent-products":
                recent = db.get_recent_products(5)
                self.send_json(200, {"success": True, "data": recent})
                return

            elif path == "/api/dashboard/recently-updated":
                updated = db.get_recently_updated_products(5)
                self.send_json(200, {"success": True, "data": updated})
                return

            elif path == "/api/dashboard/category-summary":
                summary = db.get_category_summary()
                self.send_json(200, {"success": True, "data": summary})
                return

            self.send_json(404, {"error": "API route not found."})
            return

        # 2. Static Asset Requests
        if path.startswith("/js/") or path.startswith("/css/") or path.startswith("/images/") or path.endswith(".ico") or path.endswith(".png") or path.endswith(".svg") or path == "/admin.html":
            file_rel = path.lstrip("/")
            file_full = os.path.join(PUBLIC_DIR, file_rel)
            self.serve_static_file(file_full)
            return

        # 3. Route Authentication & Authorization Gateway
        admin = self.get_current_admin()

        # Login page handling
        if path == "/admin/login":
            if admin:
                # Already authenticated -> redirect to dashboard
                self.send_redirect("/admin/dashboard")
                return
            # Serve the login page
            self.serve_static_file(os.path.join(PUBLIC_DIR, "index.html"), "text/html; charset=utf-8")
            return

        # Root redirect
        if path in ["/", "/admin", "/admin/"]:
            if admin:
                self.send_redirect("/admin/dashboard")
            else:
                self.send_redirect("/admin/login")
            return

        # Any admin route: /admin/dashboard, /admin/products, /admin/categories, etc.
        if path.startswith("/admin/"):
            if not admin:
                # STRICT BACKEND AUTHORIZATION: Redirect to /admin/login
                self.send_redirect("/admin/login")
                return
            # Authorized admin -> serve SPA application shell
            self.serve_static_file(os.path.join(PUBLIC_DIR, "index.html"), "text/html; charset=utf-8")
            return

        # Catch-all: If visitor accesses any unknown route, redirect based on session
        if admin:
            self.send_redirect("/admin/dashboard")
        else:
            self.send_redirect("/admin/login")

    def do_POST(self):
        parsed = urllib.parse.urlparse(self.path)
        path = parsed.path

        # 1. Login Endpoint
        if path == "/api/auth/login":
            body = self.read_json_body()
            identifier = body.get("identifier", "")
            password = body.get("password", "")

            admin = db.authenticate_admin(identifier, password)
            if admin:
                token, expires = db.create_session(admin["id"])
                client_ip = self.client_address[0] if self.client_address else "127.0.0.1"
                db.log_audit(admin["email"], "ADMIN_LOGIN", "AUTH", admin["id"], "Administrator logged in via credentials.", client_ip)

                # Set secure HttpOnly cookie
                cookie = cookies.SimpleCookie()
                cookie["sitaram_session"] = token
                cookie["sitaram_session"]["path"] = "/"
                cookie["sitaram_session"]["httponly"] = True
                cookie["sitaram_session"]["samesite"] = "Lax"
                cookie["sitaram_session"]["max-age"] = 86400  # 24 hours

                cookie_str = cookie.output(header="").strip()
                self.send_json(200, {
                    "success": True,
                    "token": token,
                    "admin": admin
                }, extra_headers={"Set-Cookie": cookie_str})
            else:
                self.send_json(401, {
                    "success": False,
                    "message": "Invalid Administrator credentials. Please check your username/email and password."
                })
            return

        # 2. Logout Endpoint
        if path == "/api/auth/logout":
            token = self.get_session_token()
            if token:
                db.destroy_session(token)

            # Expire session cookie
            expired_cookie = "sitaram_session=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Max-Age=0; HttpOnly; SameSite=Lax"
            self.send_json(200, {"success": True}, extra_headers={"Set-Cookie": expired_cookie})
            return

        # 3. Forgot Password Endpoint
        if path == "/api/auth/forgot-password":
            body = self.read_json_body()
            email = (body.get("email", "")).strip().lower()

            conn = db.get_connection()
            c = conn.cursor()
            c.execute("SELECT id, name, email FROM admins WHERE lower(email) = ?", (email,))
            admin_row = c.fetchone()
            conn.close()

            if admin_row:
                client_ip = self.client_address[0] if self.client_address else "127.0.0.1"
                db.log_audit(admin_row["email"], "PASSWORD_RESET_REQUEST", "ADMIN", admin_row["id"], f"Password recovery requested for {admin_row['email']}.", client_ip)
                self.send_json(200, {
                    "success": True,
                    "message": f"A secure password recovery instructions email has been queued for {admin_row['email']}."
                })
            else:
                self.send_json(404, {
                    "success": False,
                    "message": "The provided email address is not registered as an authorized administrator."
                })
            return

        self.send_json(404, {"error": "API route not found."})

    def log_message(self, format, *args):
        # Keep background server logs concise
        pass

if __name__ == "__main__":
    socketserver.TCPServer.allow_reuse_address = True
    httpd = socketserver.TCPServer(("", PORT), SitaramAdminHandler)
    print(f"Sitaram Ayurveda Admin Gateway running on port {PORT}", flush=True)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        httpd.server_close()

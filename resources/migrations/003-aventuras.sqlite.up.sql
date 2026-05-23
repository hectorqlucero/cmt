CREATE TABLE IF NOT EXISTS aventuras (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  aventura TEXT,
  fecha TEXT,
  leader_email TEXT,
  enlace TEXT,
  enlacev TEXT,
  cmt_id INTEGER NOT NULL DEFAULT 0,
  FOREIGN KEY (cmt_id) REFERENCES cmt (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS fk_aventuras_cmt ON aventuras (cmt_id);

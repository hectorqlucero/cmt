CREATE TABLE IF NOT EXISTS aventuras_link (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  aventuras_id INTEGER NOT NULL,
  comments TEXT,
  nombre TEXT,
  FOREIGN KEY (aventuras_id) REFERENCES aventuras (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS fk_aventuras_link_aventuras ON aventuras_link (aventuras_id);

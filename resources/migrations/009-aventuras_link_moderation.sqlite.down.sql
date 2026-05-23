PRAGMA foreign_keys=OFF;

CREATE TABLE aventuras_link_new (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  aventuras_id INTEGER NOT NULL,
  comments TEXT,
  nombre TEXT,
  CONSTRAINT fk_aventuras_link_aventuras FOREIGN KEY (aventuras_id) REFERENCES aventuras(id) ON DELETE CASCADE
);

INSERT INTO aventuras_link_new (id, aventuras_id, comments, nombre)
SELECT id, aventuras_id, comments, nombre
FROM aventuras_link;

DROP TABLE aventuras_link;
ALTER TABLE aventuras_link_new RENAME TO aventuras_link;

PRAGMA foreign_keys=ON;

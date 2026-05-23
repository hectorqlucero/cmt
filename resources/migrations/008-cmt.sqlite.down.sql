CREATE TABLE IF NOT EXISTS cmt_tmp (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT,
  comments TEXT,
  maximo INTEGER DEFAULT 0
);

INSERT INTO cmt_tmp (id, nombre, comments, maximo)
SELECT id, nombre, comments, maximo
FROM cmt;

DROP TABLE cmt;
ALTER TABLE cmt_tmp RENAME TO cmt;

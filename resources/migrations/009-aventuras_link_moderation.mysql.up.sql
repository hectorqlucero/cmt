ALTER TABLE aventuras_link
  ADD COLUMN commenter_email VARCHAR(255) NULL,
  ADD COLUMN approved TINYINT(1) NOT NULL DEFAULT 0;

UPDATE aventuras_link
SET approved = 1;

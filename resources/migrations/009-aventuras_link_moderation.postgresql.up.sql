ALTER TABLE aventuras_link
  ADD COLUMN commenter_email VARCHAR(255),
  ADD COLUMN approved BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE aventuras_link
SET approved = TRUE;

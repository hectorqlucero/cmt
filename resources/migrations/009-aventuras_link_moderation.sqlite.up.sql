ALTER TABLE aventuras_link ADD COLUMN commenter_email TEXT;
ALTER TABLE aventuras_link ADD COLUMN approved INTEGER NOT NULL DEFAULT 0;

UPDATE aventuras_link
SET approved = 1;

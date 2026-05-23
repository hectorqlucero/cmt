CREATE INDEX idx_aventuras_link_approved ON aventuras_link (approved);
CREATE INDEX idx_aventuras_link_aventuras_approved ON aventuras_link (aventuras_id, approved);

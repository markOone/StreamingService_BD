alter table "user"
add column phone text not null;

alter table "user"
add constraint phone_format check (phone ~ '^\+?[0-9]{10,15}$');

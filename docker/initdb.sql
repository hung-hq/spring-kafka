create extension if not exists "uuid-ossp";
create table if not exists events
(
    id         uuid default uuid_generate_v4() primary key,
    event_type varchar(255),
    message    varchar(255),
    created_at timestamp not null,
    updated_at timestamp
)
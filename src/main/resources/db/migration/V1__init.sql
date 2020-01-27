create table userprofile (
    id  bigserial not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    age int4 check (age<=120 AND age>=18),
    email varchar(40) not null,
    first_name varchar(20),
    is_blocked boolean default false not null,
    is_deleted boolean default false not null,
    last_name varchar(20),
    password varchar(128) not null,
    username varchar(20) not null,
    primary key (id)
);

alter table userprofile add constraint username_index unique (username);
alter table userprofile add constraint email_index unique (email);
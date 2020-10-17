-- !Ups

create table person
(
    id       serial       not null primary key,
    name     varchar(255) not null,
    age      smallint     not null
);

-- !Downs

drop table if exists person cascade;

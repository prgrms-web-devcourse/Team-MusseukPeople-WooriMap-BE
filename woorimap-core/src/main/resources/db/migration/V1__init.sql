CREATE TABLE invite_code
(
    code             varchar(255) NOT NULL,
    inviter_id       bigint       NOT NULL,
    expire_date_time datetime     NOT NULL,
    primary key (code)
) engine = InnoDB
  default charset = utf8mb4;

CREATE TABLE couple
(
    id                bigint   NOT NULL auto_increment,
    start_date        date     NOT NULL,
    created_date_time datetime NOT NULL,
    updated_date_time datetime NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

CREATE TABLE member
(
    id                bigint       NOT NULL auto_increment,
    couple_id         bigint       NULL,
    email             varchar(255) NOT NULL,
    password          varchar(255) NOT NULL,
    nickname          varchar(255) NOT NULL,
    image_url         varchar(255) NULL,
    created_date_time datetime     NOT NULL,
    updated_date_time datetime     NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table member
    add unique key (email),
    add constraint fk_member_to_couple
        foreign key (couple_id) references couple (id);

CREATE TABLE post
(
    id                bigint         NOT NULL auto_increment,
    couple_id         bigint         NOT NULL,
    title             varchar(255)   NOT NULL,
    content           longtext       NOT NULL,
    latitude          DECIMAL(10, 8) NOT NULL,
    longitude         DECIMAL(11, 8) NOT NULL,
    created_date_time datetime       NOT NULL,
    updated_date_time datetime       NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table post
    add constraint fk_post_to_couple
        foreign key (couple_id) references couple (id);

CREATE TABLE post_image
(
    id        bigint       NOT NULL auto_increment,
    post_id   bigint       NOT NULL,
    image_url varchar(255) NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table post_image
    add constraint fk_post_image_to_post
        foreign key (post_id) references post (id);

CREATE TABLE tag
(
    id        bigint      NOT NULL auto_increment,
    name      varchar(10) NOT NULL,
    color     varchar(10) NOT NULL,
    couple_id bigint      NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table tag
    add constraint fk_tag_to_couple
        foreign key (couple_id) references couple (id);

CREATE TABLE post_tag
(
    id      bigint NOT NULL auto_increment,
    post_id bigint NOT NULL,
    tag_id  bigint NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

alter table post_tag
    add constraint fk_post_tag_to_post
        foreign key (post_id) references post (id),
    add constraint fk_post_tag_to_tag
        foreign key (tag_id) references tag (id);


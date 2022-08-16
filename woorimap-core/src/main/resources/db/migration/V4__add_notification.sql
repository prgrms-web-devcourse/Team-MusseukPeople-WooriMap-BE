CREATE TABLE notification
(
    id                bigint       NOT NULL auto_increment,
    content           varchar(255) NOT NULL,
    notification_type varchar(255) NOT NULL,
    is_read           bit(1)       NOT NULL,
    send_member_id    bigint       NOT NULL,
    receive_member_id bigint       NOT NULL,
    content_id        bigint       NOT NULL,
    created_date_time datetime     NOT NULL,
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4;

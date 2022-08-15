alter table notification
    change column send_member_id send_member_nick_name varchar(255) not null;

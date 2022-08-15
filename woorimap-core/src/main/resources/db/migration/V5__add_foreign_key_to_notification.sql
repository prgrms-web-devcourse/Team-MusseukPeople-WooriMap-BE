alter table notification
    add constraint fk_receive_member_to_notification
        foreign key (receive_member_id) references member (id),
    add constraint fk_send_member_to_notification
        foreign key (send_member_id) references member (id),
    add constraint fk_content_to_notification
        foreign key (content_id) references post (id);

alter table notification
    drop constraint fk_receive_member_to_notification,
    drop constraint fk_send_member_to_notification,
    drop constraint fk_content_to_notification;

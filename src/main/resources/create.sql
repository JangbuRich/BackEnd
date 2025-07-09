create table orders
(
    id           bigint auto_increment primary key,
    store_id     bigint                                        null,
    team_id      bigint                                        null,
    user_id      bigint                                        null,
    order_status enum ('CANCELLED', 'RECEIVED', 'TICKET_USED') null comment '주문 상태',
    order_price  int                                           null comment '주문 가격',
    version      bigint                                        null,
    created_at   datetime(6)                                   null,
    status       enum ('ACTIVE', 'INACTIVE')                   null,
    updated_at   datetime(6)                                   null
    constraint FK5n14sr4mswfdtaoiwj7rkt0mw
        foreign key (store_id) references store (id),
    constraint FKdt89yeqha9hqufdeg91ck5k3o
        foreign key (team_id) references team (id),
    constraint FKel9kyl84ego2otj2accfd8mr7
        foreign key (user_id) references user (id)
);

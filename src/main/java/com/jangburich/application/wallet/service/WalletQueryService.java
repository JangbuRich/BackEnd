package com.jangburich.application.wallet.service;

import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.repository.PointTransactionRepository;
import com.jangburich.domain.repository.OrdersRepository;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.repository.UserRepository;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.PageInfo;
import com.jangburich.presentation.wallet.dto.response.AvailableOrder;
import com.jangburich.presentation.wallet.dto.response.PointTransactionList;
import com.jangburich.presentation.wallet.dto.response.PointTransactionItem;
import com.jangburich.presentation.wallet.dto.response.WalletResponse;
import com.jangburich.utils.DateTimeFormatterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletQueryService {

    private final OrdersRepository ordersRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final UserRepository userRepository;

    public WalletResponse getMyWallet(String userId, Pageable pageable) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_USER_ID));

        Page<Orders> orders = ordersRepository.findByUserAndOrderStatus(user, OrderStatus.TICKET_ISSUED, pageable);

        List<AvailableOrder> availableOrders = orders.stream()
                .map(availableOrder -> new AvailableOrder(
                        DateTimeFormatterUtil.formatToKoreanDateTime(availableOrder.getCreatedAt()),
                        availableOrder.getOrderPrice(),
                        availableOrder.getStore() != null ? availableOrder.getStore().getName() : "장부리치 지갑",
                        availableOrder.getStore().getName()))
                .toList();

        Integer point = storeTeamRepository.sumRemainPointByUserAndStatus(user);

        PageInfo pageInfo = new PageInfo(orders.getNumber(), orders.getSize(), orders.getTotalPages(), orders.getTotalElements(), orders.hasNext(), orders.hasPrevious());

        return new WalletResponse(point, user.getName(),availableOrders, pageInfo);
    }

    public PointTransactionList getPointList(String userId, Boolean prePay, LocalDate createdAfter, LocalDate createdBefore, Pageable pageable){
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_USER_ID));

        if(createdAfter==null){
            createdAfter =  LocalDate.of(1970, 1, 1);
        }
        if(createdBefore == null){
            createdBefore= LocalDate.of(2999, 12, 31);
        }

        Page<PointTransactionItem> pointTransactions;
        if(prePay) {
            pointTransactions = pointTransactionRepository.findAllPrepayByCreatedAfterAndCreatedBefore(user, createdAfter.atStartOfDay(), createdBefore.atTime(LocalTime.MAX), pageable);
        }
        else{
            pointTransactions = ordersRepository.findAllTicketByCreatedAfterAndCreatedBefore(user, createdAfter.atStartOfDay(), createdBefore.atTime(LocalTime.MAX), pageable);
        }

        if(pointTransactions == null){
            throw new DefaultException(ErrorCode.INVALID_TRANSACTION_ID);
        }

        PageInfo pageInfo = new PageInfo(pointTransactions.getNumber(),pointTransactions.getSize(),pointTransactions.getTotalPages(),pointTransactions.getTotalElements(), pointTransactions.hasNext(), pointTransactions.hasPrevious());

        return new PointTransactionList(pointTransactions.stream().toList(), pageInfo);
    }

    public PointTransactionItem getPointDetail (String userId, Long transactionId, boolean prePay){
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_USER_ID));

        PointTransactionItem pointTransactionItem;
        if(prePay){
            pointTransactionItem = pointTransactionRepository.findByIdAndUser(transactionId, user);
        }
        else{
            pointTransactionItem = ordersRepository.findByIdAndUser(transactionId, user);
        }

        if(pointTransactionItem == null){
            throw new DefaultException(ErrorCode.INVALID_TRANSACTION_ID);
        }

        return pointTransactionItem;
    }
}
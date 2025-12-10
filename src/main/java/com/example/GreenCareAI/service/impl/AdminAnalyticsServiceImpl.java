package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.dto.response.RevenueStatsResponse;
import com.example.GreenCareAI.dto.response.SubscriptionStatsResponse;
import com.example.GreenCareAI.dto.response.UserStatsResponse;
import com.example.GreenCareAI.enums.Role;
import com.example.GreenCareAI.enums.SubscriptionStatus;
import com.example.GreenCareAI.repository.PaymentOrderRepository;
import com.example.GreenCareAI.repository.SubscriptionRepository;
import com.example.GreenCareAI.repository.UserRepository;
import com.example.GreenCareAI.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public RevenueStatsResponse getRevenueStats() {
        // Calculate revenue statistics
        Double totalRevenue = paymentOrderRepository.sumAmountByStatus("SUCCESS");

        // Revenue today
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        Double revenueToday = paymentOrderRepository.sumAmountByStatusAndDateAfter("SUCCESS", startOfToday);

        // Revenue last 7 days
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Double revenueLast7Days = paymentOrderRepository.sumAmountByStatusAndDateAfter("SUCCESS", sevenDaysAgo);

        // Revenue last 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Double revenueLast30Days = paymentOrderRepository.sumAmountByStatusAndDateAfter("SUCCESS", thirtyDaysAgo);

        // Payment counts
        Long totalSuccessful = paymentOrderRepository.countByStatus("SUCCESS");
        Long totalPending = paymentOrderRepository.countByStatus("PENDING");
        Long totalCanceled = paymentOrderRepository.countByStatus("CANCELED");

        return RevenueStatsResponse.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .revenueToday(revenueToday != null ? revenueToday : 0.0)
                .revenueLast7Days(revenueLast7Days != null ? revenueLast7Days : 0.0)
                .revenueLast30Days(revenueLast30Days != null ? revenueLast30Days : 0.0)
                .totalSuccessfulPayments(totalSuccessful != null ? totalSuccessful : 0L)
                .totalPendingPayments(totalPending != null ? totalPending : 0L)
                .totalCanceledPayments(totalCanceled != null ? totalCanceled : 0L)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Override
    public UserStatsResponse getUserStats() {
        // Total users
        Long totalUsers = userRepository.count();

        // Users by role
        Long totalAdmins = userRepository.countByRole(Role.ADMIN);
        Long totalRegularUsers = userRepository.countByRole(Role.USER);

        // Note: User entity doesn't have createdAt field, so new user metrics are set
        // to 0
        // In production, you should add a createdAt timestamp to the User entity
        Long newUsersToday = 0L;
        Long newUsersLast7Days = 0L;
        Long newUsersLast30Days = 0L;

        return UserStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalAdmins(totalAdmins != null ? totalAdmins : 0L)
                .totalRegularUsers(totalRegularUsers != null ? totalRegularUsers : 0L)
                .newUsersToday(newUsersToday)
                .newUsersLast7Days(newUsersLast7Days)
                .newUsersLast30Days(newUsersLast30Days)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Override
    public SubscriptionStatsResponse getSubscriptionStats() {
        // Total subscriptions
        Long totalSubscriptions = subscriptionRepository.count();

        // Subscriptions by status
        Long activeSubscriptions = subscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE);
        Long expiredSubscriptions = subscriptionRepository.countByStatus(SubscriptionStatus.EXPIRED);

        // Subscriptions by plan
        Long freeSubscriptions = subscriptionRepository.countByPlanName("Free");
        Long premiumSubscriptions = subscriptionRepository.countByPlanName("Premium");

        // Calculate conversion rate (Premium / Total users * 100)
        Long totalUsers = userRepository.count();
        Double conversionRate = 0.0;
        if (totalUsers > 0 && premiumSubscriptions != null) {
            conversionRate = (premiumSubscriptions.doubleValue() / totalUsers.doubleValue()) * 100.0;
        }

        return SubscriptionStatsResponse.builder()
                .totalSubscriptions(totalSubscriptions)
                .activeSubscriptions(activeSubscriptions != null ? activeSubscriptions : 0L)
                .expiredSubscriptions(expiredSubscriptions != null ? expiredSubscriptions : 0L)
                .freeSubscriptions(freeSubscriptions != null ? freeSubscriptions : 0L)
                .premiumSubscriptions(premiumSubscriptions != null ? premiumSubscriptions : 0L)
                .conversionRate(conversionRate)
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}
